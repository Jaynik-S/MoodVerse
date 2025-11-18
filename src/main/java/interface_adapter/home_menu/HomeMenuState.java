package interface_adapter.home_menu;

import java.util.ArrayList;
import java.util.List;

public class HomeMenuState {
    private List<String> titles = new ArrayList<>();
    private List<String> dates = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private String errorMessage = "";

//    public HomeMenuState(List<String> titles, List<String> dates, List<String> moods, String errorMessage) {
//        this.titles = titles;
//        this.dates = dates;
//        this.moods = moods;
//        this.errorMessage = errorMessage;
//    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> moods) {
        this.tags = moods;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}