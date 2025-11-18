package interface_adapter.lock_screen;

import interface_adapter.ViewModel;
import use_case.verify_password.VerifyPasswordOutputBoundary;
import use_case.verify_password.VerifyPasswordOutputData;

public class LockScreenPresenter implements VerifyPasswordOutputBoundary{

    private LockScreenViewModel lockScreenViewModel;

    public LockScreenPresenter(LockScreenViewModel lockScreenViewModel) {
        this.lockScreenViewModel = lockScreenViewModel;
    }

    public void prepareSuccessView(VerifyPasswordOutputData outputData){
        final LockScreenState state = lockScreenViewModel.getState();
        state.setError(null);
        lockScreenViewModel.firePropertyChanged();
    }

    public void prepareFailView(String errorMessage){
        final LockScreenState state = lockScreenViewModel.getState();
        state.setError(errorMessage);
        lockScreenViewModel.firePropertyChanged();
    }
}



