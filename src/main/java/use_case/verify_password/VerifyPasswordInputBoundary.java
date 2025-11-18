package use_case.verify_password;

public interface VerifyPasswordInputBoundary {
    void execute(VerifyPasswordInputData VerifyPasswordInputData);
    void switchToHomeMenu();
}
