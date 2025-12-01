package use_case.analyze_keywords;

public class AnalyzeKeywordsInputData {
    private final String textBody;

    public AnalyzeKeywordsInputData(String textBody) {
        this.textBody = textBody;
    }

    public String getTextBody() {
        return textBody;
    }
}
