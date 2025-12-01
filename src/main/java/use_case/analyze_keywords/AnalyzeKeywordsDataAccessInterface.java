package use_case.analyze_keywords;

import entity.AnalysisResult;

public interface AnalyzeKeywordsDataAccessInterface {
    AnalysisResult analyze(String textBody);
}
