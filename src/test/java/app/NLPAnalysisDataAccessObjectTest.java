package app;

import data_access.NLPAnalysisDataAccessObject;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import entity.Keyword;
import entity.SentimentLabel;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import entity.Keyword;
import entity.SentimentLabel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class NLPAnalysisDataAccessObjectTest {

    private static NLPAnalysisDataAccessObject nlpDao;

    @BeforeAll
    public static void setup() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        nlpDao = new NLPAnalysisDataAccessObject(pipeline);
    }

    @Test
    public void testAnalyze_NullOrBlankText_ReturnsNeutral() {
        var resultNull = nlpDao.analyze(null);
        var resultBlank = nlpDao.analyze("   ");

        System.out.println("Result for null text: " + resultNull);
        System.out.println("Result for blank text: " + resultBlank);

        assertEquals(SentimentLabel.NEUTRAL, resultNull.overall());
        assertEquals(0, resultNull.keywords().size());

        assertEquals(SentimentLabel.NEUTRAL, resultBlank.overall());
        assertEquals(0, resultBlank.keywords().size());
    }

    @Test
    public void testAnalyze_PositiveText_ReturnsPositiveSentiment() {
        String text = "I love this beautiful day!";
        var result = nlpDao.analyze(text);

        System.out.println("Result for positive text: " + result);

        assertTrue(result.overall() == SentimentLabel.POSITIVE || result.overall() == SentimentLabel.VERY_POSITIVE);
        assertTrue(result.confidence() > 0);
    }

    @Test
    public void testAnalyze_ExtractsKeywords() {
        String text = "Java programming and software design patterns are powerful";
        var result = nlpDao.analyze(text);

        System.out.println("Keywords extracted: " + result.keywords());

        List<String> keywordStrings = result.keywords().stream().map(Keyword::text).toList();

        assertTrue(keywordStrings.stream().anyMatch(k -> k.contains("java")));
        assertTrue(keywordStrings.stream().anyMatch(k -> k.contains("programming") || k.contains("software")));
    }
}

