package use_case.analyze_keywords;

import java.util.List;

public class AnalyzeKeywordsOutputData {
    private final List<String> keywords;

    public AnalyzeKeywordsOutputData(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
