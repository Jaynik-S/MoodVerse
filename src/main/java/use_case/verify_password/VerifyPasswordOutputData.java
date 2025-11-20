package use_case.verify_password;

import java.util.List;

public class VerifyPasswordOutputData {
    private final String passwordStatus;
    private final List<List<String>> allEntries;

    public VerifyPasswordOutputData(String passwordStatus, List<List<String>> allEntries) {
        this.passwordStatus = passwordStatus;
        this.allEntries = allEntries;
    }

    public String passwordStatus() {  return passwordStatus;  }
    public List<List<String>> getAllEntries() {return allEntries;}
}
