package data_access;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class VerifyPasswordDataAccessObjectTest {

    private static final Path ENV_TEST_PATH = Paths.get(".env-test");

    @BeforeEach
    public void setUp() throws Exception {
        VerifyPasswordDataAccessObject.setEnvPathForTesting(ENV_TEST_PATH);
        Files.writeString(ENV_TEST_PATH, "PASSWORD=\n");
        VerifyPasswordDataAccessObject.setSysPasswordForTesting(null);
    }

    @AfterEach
    public void tearDown() throws Exception {
        // clean up temp env file
        if (Files.exists(ENV_TEST_PATH)) {
            Files.delete(ENV_TEST_PATH);
        }
    }

    @Test
    public void testSavingNewPasswordCreatesEntryInEnv() throws Exception {
        VerifyPasswordDataAccessObject dao = new VerifyPasswordDataAccessObject();

        String result = dao.verifyPassword("newSecret");

        assertEquals("Created new password.", result);
        String content = Files.readString(ENV_TEST_PATH);
        assertTrue(content.contains("PASSWORD=newSecret"));
    }

    @Test
    public void testIncorrectPassword() throws Exception {
        // Simulate an existing password
        VerifyPasswordDataAccessObject.setSysPasswordForTesting("correctPass");
        VerifyPasswordDataAccessObject dao = new VerifyPasswordDataAccessObject();

        String result = dao.verifyPassword("wrongPass");

        assertEquals("Incorrect Password", result);
    }

    @Test
    public void testCorrectPassword() throws Exception {
        // Simulate an existing password
        VerifyPasswordDataAccessObject.setSysPasswordForTesting("correctPass");
        VerifyPasswordDataAccessObject dao = new VerifyPasswordDataAccessObject();

        String result = dao.verifyPassword("correctPass");

        assertEquals("Correct Password", result);
    }
}
