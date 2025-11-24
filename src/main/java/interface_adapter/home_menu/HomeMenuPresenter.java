package interface_adapter.home_menu;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeMenuPresenter{

    private final HomeMenuViewModel viewModel;

    public HomeMenuPresenter(HomeMenuViewModel viewModel) {
        this.viewModel = viewModel;
    }

    private String objectToString(Object value) {
        return value == null ? "" : value.toString();
    }


    private String keywordsToDisplay(Object keywordsObj) {
        if (keywordsObj == null) {
            return "";
        }
        if (keywordsObj instanceof java.util.List<?>) {
            java.util.List<?> list = (java.util.List<?>) keywordsObj;
            return list.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
        }
        return keywordsObj.toString();
    }

    public void presentEntries(List<String> titles,
                               List<String> createdDates,
                               List<String> updatedDates,
                               List<String> keywords,
                               List<String> storagePaths) {

        HomeMenuState state = viewModel.getState();

        state.setTitles(titles);
        state.setCreatedDates(createdDates);
        state.setUpdatedDates(updatedDates);
        state.setKeywords(keywords);
        state.setStoragePaths(storagePaths);

        state.setErrorMessage("");

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    //Error message
    public void presentError(String errorMessage) {
        HomeMenuState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    public void presentEntriesFromData(List<Map<String, Object>> rawEntries) {
        if (rawEntries == null) {
            rawEntries = Collections.emptyList();
        }
        List<String> titles = new ArrayList<>();
        List<String> createdDates = new ArrayList<>();
        List<String> updatedDates = new ArrayList<>();
        List<String> keywords = new ArrayList<>();
        List<String> storagePaths = new ArrayList<>();

        for (Map<String, Object> data : rawEntries) {
            // convert to string
            titles.add(objectToString(data.get("title")));
            createdDates.add(objectToString(data.get("createdDate")));
            updatedDates.add(objectToString(data.get("updatedDate")));
            keywords.add(keywordsToDisplay(data.get("keywords")));
            storagePaths.add(objectToString(data.get("storagePath")));
        }

        // update state + viewModel
        presentEntries(titles, createdDates, updatedDates, keywords, storagePaths);
    }
}
