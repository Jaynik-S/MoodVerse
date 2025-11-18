package use_case.verify_password;

public class VerifyPasswordInteractor implements VerifyPasswordInputBoundary {
    private final VerifyPasswordUserDataAccessInterface userDataAccess;
    private final VerifyPasswordOutputBoundary userPresenter;

    public VerifyPasswordInteractor(VerifyPasswordUserDataAccessInterface userDataAccessInterface,
                                   VerifyPasswordOutputBoundary verifyPasswordOutputBoundary) {
        this.userDataAccess = userDataAccessInterface;
        this.userPresenter = verifyPasswordOutputBoundary;
    }

    @Override
    public void execute(VerifyPasswordInputData inputData) {
        String password = inputData.getPassword();
        try {
            String passwordStatus = userDataAccess.verifyPassword(password);
            VerifyPasswordOutputData outputData = new VerifyPasswordOutputData(passwordStatus);
            userPresenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            userPresenter.prepareFailView("Failed to verify password: " + e.getMessage());
        }
    }

    @Override
    public void switchToHomeMenu() {
        userPresenter.switchToHomeMenu();
    }

}
