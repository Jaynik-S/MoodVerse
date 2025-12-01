package use_case.analyze_keywords;

import entity.AnalysisResult;
import entity.DiaryEntry;
import entity.Keyword;

import java.util.List;

public class AnalyzeKeywordsInteractor implements AnalyzeKeywordsInputBoundary {

    private final AnalyzeKeywordsDataAccessInterface analysisDataAccessObject;
    private final AnalyzeKeywordsOutputBoundary presenter;

    public AnalyzeKeywordsInteractor(AnalyzeKeywordsDataAccessInterface analysisDataAccessObject,
                                     AnalyzeKeywordsOutputBoundary presenter) {
        this.analysisDataAccessObject = analysisDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(AnalyzeKeywordsInputData inputData) {
        String textBody = inputData.getTextBody();
        if (textBody == null || textBody.isBlank()) {
            presenter.prepareFailView("Add some text to analyze keywords.");
            return;
        }
        if (textBody.length() < DiaryEntry.MIN_TEXT_LENGTH) {
            presenter.prepareFailView("Diary entry is too short to extract keywords.");
            return;
        }
        if (textBody.length() > DiaryEntry.MAX_TEXT_LENGTH) {
            presenter.prepareFailView("Diary entry is too long to extract keywords.");
            return;
        }

        try {
            AnalysisResult analysisResult = analysisDataAccessObject.analyze(textBody);
            List<String> keywords = analysisResult.keywords()
                    .stream()
                    .map(Keyword::text)
                    .toList();
            presenter.prepareSuccessView(new AnalyzeKeywordsOutputData(keywords));
        } catch (Exception e) {
            presenter.prepareFailView("Failed to analyze keywords: " + e.getMessage());
        }
    }
}
