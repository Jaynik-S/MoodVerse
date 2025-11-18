package interface_adapter;

import interface_adapter.ViewManagerModel;
import use_case.go_back.GoBackOutputBoundary;

public class GoBackPresenter implements GoBackOutputBoundary{

    private final ViewManagerModel viewManagerModel;

    public GoBackPresenter(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }
    @Override
    public void prepareSuccessView() {
        viewManagerModel.setState("Home");
        viewManagerModel.firePropertyChanged();
    }
}
