package use_case.get_recommendations;

public class GetRecommendationsInputData {
    private final String textBody;

    public GetRecommendationsInputData(String textBody) {
        this.textBody = textBody;
    }

    public String getTextBody() {  return textBody; }
}

