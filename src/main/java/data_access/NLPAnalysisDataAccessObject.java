package data_access;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import entity.AnalysisResult;
import entity.Keyword;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Adapter responsible for extracting keywords from diary text using Stanford CoreNLP.
 * This sits in the data access layer because it shields the rest of the system from
 * the external NLP dependency while returning domain entities (keywords) to the
 * use cases.
 */
public final class NLPAnalysisDataAccessObject {
    private final StanfordCoreNLP pipeline;

    public NLPAnalysisDataAccessObject(StanfordCoreNLP pipeline) {
        this.pipeline = pipeline;
    }

    /**
     * Returns the top 10 noun-phrase keywords found in the provided text.
     */
    public AnalysisResult analyze(String text) {
        if (text == null || text.isBlank()) {
            return new AnalysisResult(List.of());
        }

        Annotation doc = new Annotation(text);
        synchronized (pipeline) {
            pipeline.annotate(doc);
        }

        Map<String, Integer> counts = new HashMap<>();
        for (CoreMap sent : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
            List<CoreLabel> tokens = sent.get(CoreAnnotations.TokensAnnotation.class);
            List<String> words = new ArrayList<>(tokens.size());
            List<String> posTags = new ArrayList<>(tokens.size());
            for (CoreLabel token : tokens) {
                words.add(token.word());
                posTags.add(token.tag());
            }

            String[] pos = posTags.toArray(String[]::new);
            // Heuristic: we want adjective-noun blocks (i.e. quiet morning run; busy work week, etc.)
            for (int i = 0; i < pos.length; i++) {
                int j = i;
                while (j < pos.length && pos[j].startsWith("JJ")) {
                    j++;
                }
                if (j < pos.length && pos[j].startsWith("NN")) {
                    int k = j + 1;
                    while (k < pos.length && pos[k].startsWith("NN")) {
                        k++;
                    }
                    String phrase = String.join(" ", words.subList(i, k)).toLowerCase(Locale.ROOT);
                    phrase = phrase.replaceAll("^[^a-z0-9]+|[^a-z0-9]+$", "");
                    if (phrase.length() > 2) {
                        counts.merge(phrase, 1, Integer::sum);
                    }
                    i = k - 1;
                }
            }
        }

        int total = counts.values().stream().mapToInt(Integer::intValue).sum();
        List<Keyword> keywords = counts.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                        .reversed()
                        .thenComparing(Map.Entry::getKey))
                .limit(10)
                .map(entry -> new Keyword(entry.getKey(),
                        total == 0 ? 0.0 : entry.getValue() / (double) total))
                .toList();

        return new AnalysisResult(keywords);
    }
}
