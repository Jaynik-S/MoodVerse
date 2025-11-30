package data_access;

import io.github.cdimascio.dotenv.Dotenv;
import use_case.verify_password.VerifyPasswordUserDataAccessInterface;

import java.nio.file.*;
import java.util.*;

public class VerifyPasswordDataAccessObject implements VerifyPasswordUserDataAccessInterface {
    public String passwordStatus;
    private static final Dotenv dotenv = Dotenv.load();
    private static String SYS_PASSWORD = dotenv.get("PASSWORD");
    private static Path envPath = Paths.get(".env");

    public static void setSysPasswordForTesting(String password) {
        SYS_PASSWORD = password;
    }
    public static void setEnvPathForTesting(Path path) { envPath = path;  }

    public static void writeEnvValue(String key, String value) throws Exception {
        if (!Files.exists(envPath)) {
            Files.createFile(envPath);
            Files.write(envPath, Collections.singletonList("PASSWORD="));
        }

        List<String> lines = Files.readAllLines(envPath);
        boolean keyFound = false;

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith(key + "=")) {
                lines.set(i, key + "=" + value);
                keyFound = true;
                break;
            }
        }
        if (!keyFound) {
            lines.add(key + "=" + value);
        }
        Files.write(envPath, lines);
    }

    public String verifyPassword(String password) throws Exception {
        if (SYS_PASSWORD != null && SYS_PASSWORD.equals(password)) {
            passwordStatus = "Correct Password";

        } else if (SYS_PASSWORD == null || SYS_PASSWORD.isEmpty()) {
            try {
                writeEnvValue("PASSWORD", password);
                passwordStatus = "Created new password.";
            } catch (Exception e) {
                throw new Exception("Failed to set new password: ", e);
            }

        } else {
            passwordStatus = "Incorrect Password";
        }
        return passwordStatus;
    }
}