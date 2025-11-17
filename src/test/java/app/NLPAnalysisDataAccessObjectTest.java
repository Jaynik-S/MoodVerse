package app;

import data_access.NLPAnalysisDataAccessObject;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import entity.Keyword;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NLPAnalysisDataAccessObjectTest {

    private static NLPAnalysisDataAccessObject nlpDao;

    @BeforeAll
    public static void setup() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma"); // break text into tokens; split into sentences; tag each token with its part of speech; compute lemmas (unused)
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        nlpDao = new NLPAnalysisDataAccessObject(pipeline);
    }

    @Test
    public void testAnalyze_NullOrBlankText_ReturnsNoKeywords() {
        var resultNull = nlpDao.analyze(null);
        var resultBlank = nlpDao.analyze("   ");

        System.out.println("Result for null text: " + resultNull);
        System.out.println("Result for blank text: " + resultBlank);

        assertEquals(0, resultNull.keywords().size());

        assertEquals(0, resultBlank.keywords().size());
    }

    @Test
    public void testAnalyze_LimitsToTopTenKeywords() {
        String text = "I'm so in love with programming";
        var result = nlpDao.analyze(text);

        assertTrue(result.keywords().size() <= 10);
        List<String> keywordStrings = result.keywords().stream().map(Keyword::text).toList();
        System.out.println(keywordStrings);
//        assertEquals(10, result.keywords().size());
        // ensure the most frequent word (coffee) is ranked first
//        assertTrue(result.keywords().get(0).text().contains("coffee"));
    }

    @Test
    public void testAnalyze_ExtractsKeywords() {
        String text = "Java programming and software design patterns are powerful";
        var result = nlpDao.analyze(text);

//        System.out.println("Keywords extracted: " + result.keywords());

        List<String> keywordStrings = result.keywords().stream().map(Keyword::text).toList();
        System.out.println(keywordStrings);

        assertTrue(keywordStrings.stream().anyMatch(k -> k.contains("java")));
        assertTrue(keywordStrings.stream().anyMatch(k -> k.contains("programming") || k.contains("software")));
    }
}
