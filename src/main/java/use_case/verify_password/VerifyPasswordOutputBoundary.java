package use_case.verify_password;

public interface VerifyPasswordOutputBoundary {
    void prepareSuccessView(VerifyPasswordOutputData outputData);
    void prepareFailView(String errorMessage);
    // void switchToHomeMenu(); // TODO: check flow later
}
