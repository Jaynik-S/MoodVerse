package use_case.go_back;

public class GoBackInteractor implements GoBackInputBoundary{

    private final GoBackOutputBoundary gobackPresenter;

    public GoBackInteractor(GoBackOutputBoundary gobackPresenter) {
        this.gobackPresenter = gobackPresenter;
    }
    @Override
    public void execute() {
        gobackPresenter.prepareSuccessView();
    }
}
