package interface_adapter.home_menu;

import java.util.ArrayList;
import java.util.List;

public class HomeMenuState {
    private List<String> titles = new ArrayList<>();
    private List<String> dates = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private String errorMessage = "";
    private List<Integer> entryIDs = new ArrayList<>(); //Old ID Used for jumping to corresponding document

    private List<String> storagePaths = new ArrayList<>();

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

    public List<Integer> getEntryIDs() {
        return entryIDs;
    }

    public void setEntryIDs(List<Integer> entryIDs) {
        this.entryIDs = entryIDs;
    }

    public List<String> getStoragePaths() {
        return storagePaths;
    }

    public void setStoragePaths(List<String> storagePaths) {
        this.storagePaths = storagePaths;
    }
}