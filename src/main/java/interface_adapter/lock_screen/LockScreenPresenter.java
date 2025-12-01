package interface_adapter.lock_screen;

import interface_adapter.ViewManagerModel;
import interface_adapter.home_menu.HomeMenuPresenter;
import interface_adapter.home_menu.HomeMenuViewModel;
import use_case.verify_password.VerifyPasswordOutputBoundary;
import use_case.verify_password.VerifyPasswordOutputData;

public class LockScreenPresenter implements VerifyPasswordOutputBoundary {

    private final LockScreenViewModel lockScreenViewModel;
    private final ViewManagerModel viewManagerModel;
    private final HomeMenuViewModel homeMenuViewModel;
    private final HomeMenuPresenter homeMenuPresenter;

    public LockScreenPresenter(LockScreenViewModel lockScreenViewModel,
                               ViewManagerModel viewManagerModel,
                               HomeMenuViewModel homeMenuViewModel,
                               HomeMenuPresenter homeMenuPresenter) {
        this.lockScreenViewModel = lockScreenViewModel;
        this.viewManagerModel = viewManagerModel;
        this.homeMenuViewModel = homeMenuViewModel;
        this.homeMenuPresenter = homeMenuPresenter;
    }

    public void prepareSuccessView(VerifyPasswordOutputData outputData) {
        if (homeMenuPresenter != null && outputData != null) {
            homeMenuPresenter.presentEntriesFromData(outputData.getAllEntries());
        }
        final LockScreenState state = lockScreenViewModel.getState();
        state.setError(null);
        lockScreenViewModel.firePropertyChanged();

        this.viewManagerModel.setState(homeMenuViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    public void prepareFailView(String errorMessage) {
        final LockScreenState state = lockScreenViewModel.getState();
        state.setError(errorMessage);
        lockScreenViewModel.firePropertyChanged();
    }
}