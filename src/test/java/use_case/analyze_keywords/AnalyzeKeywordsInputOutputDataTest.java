package use_case.analyze_keywords;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnalyzeKeywordsInputOutputDataTest {

    @Test
    void inputDataStoresAndReturnsTextBody() {
        AnalyzeKeywordsInputData data = new AnalyzeKeywordsInputData("some text");
        assertEquals("some text", data.getTextBody());
    }

    @Test
    void outputDataStoresAndReturnsKeywordsList() {
        AnalyzeKeywordsOutputData data = new AnalyzeKeywordsOutputData(List.of("a", "b"));
        assertEquals(List.of("a", "b"), data.getKeywords());
    }
}

