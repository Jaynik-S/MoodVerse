package use_case.verify_password;

import java.io.IOException;

public interface VerifyPasswordUserDataAccessInterface {
    String verifyPassword(String password) throws Exception;
}
