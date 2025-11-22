package interface_adapter.home_menu;
import java.util.List;

public class HomeMenuPresenter{

    private final HomeMenuViewModel viewModel;

    public HomeMenuPresenter(HomeMenuViewModel viewModel) {
        this.viewModel = viewModel;
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
}