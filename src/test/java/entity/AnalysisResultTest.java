package entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AnalysisResultTest {

    @Test
    public void testAnalysisResultCopiesListDefensively() {
        List<Keyword> source = new ArrayList<>();
        source.add(new Keyword("a", 0.5));
        AnalysisResult result = new AnalysisResult(source);
        source.add(new Keyword("b", 0.7));

        assertEquals(1, result.keywords().size());
        assertEquals("a", result.keywords().get(0).text());
    }

    @Test
    public void testAnalysisResultReturnsUnmodifiableList() {
        AnalysisResult result = new AnalysisResult(List.of(new Keyword("x", 0.1)));
        assertThrows(UnsupportedOperationException.class, () -> result.keywords().add(new Keyword("y", 0.2)));
    }
}

