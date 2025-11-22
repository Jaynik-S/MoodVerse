package interface_adapter;

import interface_adapter.home_menu.HomeMenuViewModel;
import use_case.go_back.GoBackOutputBoundary;

public class GoBackPresenter implements GoBackOutputBoundary{

    private final ViewManagerModel viewManagerModel;
    private final HomeMenuViewModel homeMenuViewModel;

    public GoBackPresenter(ViewManagerModel viewManagerModel, HomeMenuViewModel homeMenuViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.homeMenuViewModel = homeMenuViewModel;
    }
    @Override
    public void prepareSuccessView() {
        viewManagerModel.setState(homeMenuViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
