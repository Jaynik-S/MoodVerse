package use_case.verify_password;

import java.util.List;
import java.util.Map;

public class VerifyPasswordOutputData {
    private final String passwordStatus;
    private final List<Object> allEntries;

    public VerifyPasswordOutputData(String passwordStatus, List<Map<String, Object>> allEntries) {
        this.passwordStatus = passwordStatus;
        this.allEntries = allEntries;
    }

    public String passwordStatus() {  return passwordStatus;  }
    public List<Object> getAllEntries() {return allEntries;}
}
