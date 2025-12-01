package interface_adapter.home_menu;

import java.util.ArrayList;
import java.util.List;

/**
 * State object for the home menu view.
 * This class stores the data needed to render the home menu table, including
 * the entry titles, creation and update dates, extracted keywords, any
 * error message to show to the user, and the storage paths for each entry.
 */

public class HomeMenuState {
    private List<String> titles = new ArrayList<>();
    private List<String> createdDates = new ArrayList<>();
    private List<String> updatedDates = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    private String errorMessage = "";

    private List<String> storagePaths = new ArrayList<>();


    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getCreatedDates() {
        return createdDates;
    }

    public void setCreatedDates(List<String> createdDates) {
        this.createdDates = createdDates;
    }

    public List<String> getUpdatedDates() {
        return updatedDates;
    }

    public void setUpdatedDates(List<String> updatedDates) {
        this.updatedDates = updatedDates;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public List<String> getStoragePaths() {
        return storagePaths;
    }

    public void setStoragePaths(List<String> storagePaths) {
        this.storagePaths = storagePaths;
    }
}