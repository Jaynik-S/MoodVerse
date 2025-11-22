package use_case.verify_password;

import java.io.IOException;
import java.util.List;

public interface VerifyPasswordUserDataAccessInterface {
    String verifyPassword(String password) throws IOException;
    List<Object> getAll() throws IOException;
}
