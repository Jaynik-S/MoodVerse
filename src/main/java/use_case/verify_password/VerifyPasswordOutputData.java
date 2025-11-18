package use_case.verify_password;

public class VerifyPasswordOutputData {
    private final String passwordStatus;

    public VerifyPasswordOutputData(String passwordStatus) {
        this.passwordStatus = passwordStatus;
    }

    public String passwordStatus() {  return passwordStatus;  }
}
