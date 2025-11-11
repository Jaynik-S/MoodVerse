package data_access;

// data_access/StanfordCoreNlpAnalysisAdapter.java
//import use_case.note.ports.NlpAnalysisPort;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.CoreMap;
import entity.*;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;

import java.util.*;
import java.util.regex.*;

public final class NLPAnalysisDataAccessObject { // implements NlpAnalysisPort {
    private final StanfordCoreNLP pipeline;

    public NLPAnalysisDataAccessObject(StanfordCoreNLP pipeline) { this.pipeline = pipeline; }

    public AnalysisResult analyze(String text) { // should override the nlpanalysisport method
        if (text == null || text.isBlank()) {
            return new AnalysisResult(SentimentLabel.NEUTRAL, 0.0, List.of());
        }
        Annotation doc = new Annotation(text);
        synchronized (pipeline) { // CoreNLP pipeline is not guaranteed thread-safe; guard or pool per thread
            pipeline.annotate(doc);
        }

        // --- Sentiment: length-weighted average over sentences
        double sum = 0, weight = 0;
        for (CoreMap sent : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sent.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int cls = RNNCoreAnnotations.getPredictedClass(tree); // 0..4
            int len = sent.get(TokensAnnotation.class).size();
            sum += cls * len;
            weight += len;
        }
        double avg = (weight == 0) ? 2.0 : (sum / weight); // 2 ~ NEUTRAL
        SentimentLabel label = switch ((int)Math.round(avg)) {
            case 0 -> SentimentLabel.VERY_NEGATIVE;
            case 1 -> SentimentLabel.NEGATIVE;
            case 3 -> SentimentLabel.POSITIVE;
            case 4 -> SentimentLabel.VERY_POSITIVE;
            default -> SentimentLabel.NEUTRAL;
        };
        double confidence = Math.abs(avg - 2.0) / 2.0; // crude 0..1 distance from neutral

        // --- Keywords: simple NP chunking from POS tags + frequency
        // Pattern: (JJ)*(NN|NNS|NNP|NNPS)+
        Pattern np = Pattern.compile("(?:(JJ) )*(NN|NNS|NNP|NNPS)(?: (NN|NNS|NNP|NNPS))*");
        Map<String,Integer> counts = new HashMap<>();
        for (CoreMap sent : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
            List<CoreLabel> toks = sent.get(TokensAnnotation.class);
            // Build "word/POS" string to run the regex over POS while keeping surface words
            StringBuilder posLine = new StringBuilder();
            List<String> words = new ArrayList<>();
            for (CoreLabel tok : toks) {
                posLine.append(tok.tag()).append(' ');
                words.add(tok.word());
            }
            String[] pos = posLine.toString().trim().split(" ");
            // Scan contiguous spans using the POS array (manual to keep indices)
            for (int i = 0; i < pos.length; i++) {
                int j = i;
                // adjectives
                while (j < pos.length && pos[j].startsWith("JJ")) j++;
                // noun(s)
                if (j < pos.length && pos[j].startsWith("NN")) {
                    int k = j + 1;
                    while (k < pos.length && pos[k].startsWith("NN")) k++;
                    // build phrase text from words[i..k)
                    String phrase = String.join(" ", words.subList(i, k)).toLowerCase(Locale.ROOT);
                    phrase = phrase.replaceAll("^[^a-z0-9]+|[^a-z0-9]+$", ""); // trim punctuation
                    if (phrase.length() > 2) counts.merge(phrase, 1, Integer::sum);
                    i = k - 1; // advance
                }
            }
        }
        int total = counts.values().stream().mapToInt(Integer::intValue).sum();
        List<Keyword> keywords = counts.entrySet().stream()
                .sorted((a,b)->Integer.compare(b.getValue(), a.getValue()))
                .limit(20)
                .map(e -> new Keyword(e.getKey(), total == 0 ? 0.0 : e.getValue() / (double) total))
                .toList();

        return new AnalysisResult(label, confidence, keywords);
    }
}
