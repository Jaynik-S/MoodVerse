package interface_adapter;

import interface_adapter.home_menu.HomeMenuViewModel;
import interface_adapter.ViewManagerModel;
import use_case.go_back.GoBackOutputBoundary;

public class GoBackPresenter implements GoBackOutputBoundary{

    private final ViewManagerModel viewManagerModel;
    private final HomeMenuModel homeMenuViewModel;

    public GoBackPresenter(ViewManagerModel viewManagerModel, HomeMenuViewModel homeMenuViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.homeMenuViewModel = homeMenuViewModel;
    }
    @Override
    public void prepareSuccessView() {
        viewManagerModel.setState(homeMenuViewModel.geViewName());
        viewManagerModel.firePropertyChanged();
    }
}
