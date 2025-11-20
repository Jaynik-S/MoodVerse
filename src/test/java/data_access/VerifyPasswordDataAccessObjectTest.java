package data_access;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VerifyPasswordDataAccessObjectTest {

    private Path createTempEnvFile(List<String> lines) throws IOException {
        Path temp = Paths.get(".env-test");
        Files.write(temp, lines);
        return temp;
    }

    @Test
    public void testWriteEnvValue_NewKeyAppended() throws IOException {
        Path envPath = Paths.get(".env");
        Files.write(envPath, List.of("EXISTING=1"));

        VerifyPasswordDataAccessObject.writeEnvValue("NEW_KEY", "VALUE");

        List<String> lines = Files.readAllLines(envPath);
        assertTrue(lines.contains("EXISTING=1"));
        assertTrue(lines.contains("NEW_KEY=VALUE"));
    }

    @Test
    public void testWriteEnvValue_ExistingKeyUpdated() throws IOException {
        Path envPath = Paths.get(".env");
        Files.write(envPath, List.of("PASSWORD=old", "OTHER=2"));

        VerifyPasswordDataAccessObject.writeEnvValue("PASSWORD", "newpass");

        List<String> lines = Files.readAllLines(envPath);
        assertTrue(lines.contains("PASSWORD=newpass"));
        assertTrue(lines.contains("OTHER=2"));
    }
}

