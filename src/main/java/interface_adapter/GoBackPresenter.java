package interface_adapter;

import use_case.go_back.GoBackOutputBoundary;

public class GoBackPresenter implements GoBackOutputBoundary{

    private final ViewManagerModel viewManagerModel;
    private final ViewModel<?> destinationViewModel;

    public GoBackPresenter(ViewManagerModel viewManagerModel, ViewModel<?> destinationViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.destinationViewModel = destinationViewModel;
    }
    @Override
    public void prepareSuccessView() {
        viewManagerModel.setState(destinationViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
