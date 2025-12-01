package use_case.analyze_keywords;

public interface AnalyzeKeywordsOutputBoundary {
    void prepareSuccessView(AnalyzeKeywordsOutputData outputData);

    void prepareFailView(String errorMessage);
}
