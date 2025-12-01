package interface_adapter.home_menu;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeMenuPresenter {

    private final HomeMenuViewModel viewModel;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy h:mm a");

    public HomeMenuPresenter(HomeMenuViewModel viewModel) {
        this.viewModel = viewModel;
    }

    private String objectToString(Object value) {
        return value == null ? "" : value.toString();
    }

    private static String formatDateValue(Object value) {
        if (value == null) {
            return "";
        }

        String s = value.toString();
        try {
            LocalDateTime dt = LocalDateTime.parse(s);
            return dt.format(formatter);
        }
        catch (Exception error) {
            return s;
        }
    }

    private static String keywordsToDisplay(Object keywordsObj) {
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
    }

    //Error message
    public void presentError(String errorMessage) {
        HomeMenuState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.setState(state);
    }

    public void presentEntriesFromData(List<Map<String, Object>> rawEntries) {
        if (rawEntries == null || rawEntries.isEmpty()) {
            presentEntries(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                    new ArrayList<>(), new ArrayList<>());
            return;
        }
        List<Map<String, Object>> sortedEntries = new ArrayList<>(rawEntries);

        sortedEntries.sort((a, b) -> {
            Object ua = a.get("updatedDate");
            Object ub = b.get("updatedDate");

            if (ua instanceof LocalDateTime && ub instanceof LocalDateTime) {
                return ((LocalDateTime) ub).compareTo((LocalDateTime) ua);
            }
            return 0;
        });

        List<String> titles = new ArrayList<>();
        List<String> createdDates = new ArrayList<>();
        List<String> updatedDates = new ArrayList<>();
        List<String> keywords = new ArrayList<>();
        List<String> storagePaths = new ArrayList<>();

        for (Map<String, Object> data : sortedEntries) {
            // convert to string
            titles.add(objectToString(data.get("title")));
            createdDates.add(formatDateValue(data.get("createdDate")));
            updatedDates.add(formatDateValue(data.get("updatedDate")));
            keywords.add(keywordsToDisplay(data.get("keywords")));
            storagePaths.add(objectToString(data.get("storagePath")));
        }

        // update state + viewModel
        presentEntries(titles, createdDates, updatedDates, keywords, storagePaths);
    }
}