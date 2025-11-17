package entity;

import java.util.List;

/**
 * Immutable domain entity representing the extracted keywords for a diary entry.
 */
public final class AnalysisResult {
    private final List<Keyword> keywords;

    public AnalysisResult(List<Keyword> keywords) {
        this.keywords = List.copyOf(keywords);
    }

    public List<Keyword> keywords() { return keywords; }
}
