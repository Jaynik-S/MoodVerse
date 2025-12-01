package use_case.verify_password;

import java.util.List;
import java.util.Map;

public class VerifyPasswordInteractor implements VerifyPasswordInputBoundary {
    private final VerifyPasswordUserDataAccessInterface userDataAccess;
    private final RenderEntriesUserDataInterface renderEntriesDataAccess;
    private final VerifyPasswordOutputBoundary userPresenter;

    public VerifyPasswordInteractor(VerifyPasswordUserDataAccessInterface userDataAccessInterface,
                                   VerifyPasswordOutputBoundary verifyPasswordOutputBoundary,
                                    RenderEntriesUserDataInterface renderEntriesDataAccess) {
        this.userDataAccess = userDataAccessInterface;
        this.userPresenter = verifyPasswordOutputBoundary;
        this.renderEntriesDataAccess = renderEntriesDataAccess;
    }

    @Override
    public void execute(VerifyPasswordInputData inputData) {
        String password = inputData.getPassword();
        try {
            String passwordStatus = userDataAccess.verifyPassword(password);
            if (passwordStatus.equals("Incorrect Password")) {
                userPresenter.prepareFailView("Incorrect Password");
            }
            else {
                List<Map<String, Object>> allEntries = renderEntriesDataAccess.getAll();
                VerifyPasswordOutputData outputData = new VerifyPasswordOutputData(passwordStatus, allEntries);
                userPresenter.prepareSuccessView(outputData);
            }
        }
        catch (Exception error) {
            userPresenter.prepareFailView("Failed to verify password: " + error.getMessage());
        }
    }
//
//    @Override
//    public void switchToHomeMenu() {
//    }

}
