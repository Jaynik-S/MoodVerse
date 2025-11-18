package use_case.create_entry;
import java.util.List;

public class CreateEntryInputData {
    private String title;
    private String text;
    private List<String> keywords;

    public CreateEntryInputData(String title, String text, List<String> keywords) {
        this.title = title;
        this.text = text;
        this.keywords = keywords;
    }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public List<String> getKeywords() { return keywords; }
}

