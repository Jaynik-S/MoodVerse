package data_access;

import io.github.cdimascio.dotenv.Dotenv;
import use_case.verify_password.VerifyPasswordUserDataAccessInterface;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class VerifyPasswordDataAccessObject implements VerifyPasswordUserDataAccessInterface {
    public String passwordStatus;
    private static Dotenv dotenv = Dotenv.load();
    private static String SYS_PASSWORD = dotenv.get("PASSWORD");

    public static void writeEnvValue(String key, String value) throws IOException {
        Path envPath = Paths.get(".env");
        List<String> lines = Files.readAllLines(envPath);
        boolean keyFound = false;

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith(key + "=")) {
                lines.set(i, key + "=" + value);
                keyFound = true;
                break;
            }
        }
        if (!keyFound) { lines.add(key + "=" + value);  }
        Files.write(envPath, lines);
    }

    public String verifyPassword(String password) throws IOException {
        if (SYS_PASSWORD.equals(password)) {
            passwordStatus = "Correct Password";

        } else if (SYS_PASSWORD == null || SYS_PASSWORD.isEmpty()) {
            try {
                writeEnvValue("PASSWORD", password);
                passwordStatus = "Created new password.";
            } catch (Exception e) {
                throw e;
            }

        } else {  passwordStatus = "Incorrect Password";  }
        return passwordStatus;
    }
}