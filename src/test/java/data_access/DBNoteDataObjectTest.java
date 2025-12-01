package data_access;

import entity.DiaryEntry;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DBNoteDataObjectTest {

    private Path tempBaseDir;
    private final List<String> testFilesToCleanup = new java.util.ArrayList<>();

    @BeforeEach
    public void setUp() throws IOException {
        // Create a temporary directory under the system temp directory
        tempBaseDir = Files.createTempDirectory("diary_entry_database_test_");
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Clean up temporary directory used by tests
        if (tempBaseDir != null && Files.exists(tempBaseDir)) {
            Files.walk(tempBaseDir)
                    .sorted((p1, p2) -> p2.compareTo(p1))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                        }
                    });
        }

        // Clean up any test files created under the real DiaryEntry.BASE_DIR
        Path baseDir = Paths.get(DiaryEntry.BASE_DIR);
        if (Files.exists(baseDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(baseDir, "*.json")) {
                for (Path file : stream) {
                    String fileName = file.getFileName().toString();
                    // Delete test files created by testGetAllReturnsAllEntries and testSavePersistsDiaryEntryToFile
                    if (fileName.contains("Entry One") ||
                        fileName.contains("Entry Two") ||
                        fileName.contains("Save Test")) {
                        try {
                            Files.deleteIfExists(file);
                        } catch (IOException ignored) {
                        }
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Helper to create a JSON diary entry file and return its path as String.
     */
    private String createEntryFile(String fileName, Map<String, Object> fields) throws IOException {
        JSONObject json = new JSONObject();
        json.put("title", fields.getOrDefault("title", "Test Title"));
        json.put("text", fields.getOrDefault("text", "This is some test diary text."));
        json.put("keywords", fields.getOrDefault("keywords", List.of("happy", "test")));
        json.put("created_date", fields.getOrDefault("created_date", LocalDateTime.now().toString()));
        json.put("updated_date", fields.getOrDefault("updated_date", LocalDateTime.now().toString()));
        json.put("storage_path", fields.getOrDefault("storage_path", tempBaseDir.resolve(fileName).toString()));

        Path filePath = tempBaseDir.resolve(fileName);
        Files.writeString(filePath, json.toString(4));
        return filePath.toString();
    }

    @Test
    public void testGetByPathLoadsDiaryEntry() throws Exception {
        DBNoteDataObject dao = new DBNoteDataObject();

        String title = "My Day";
        String text = "Today was a good day.";
        LocalDateTime created = LocalDateTime.of(2024, 1, 1, 12, 0);

        Map<String, Object> fields = new HashMap<>();
        fields.put("title", title);
        fields.put("text", text);
        fields.put("created_date", created.toString());

        String path = createEntryFile("entry1.json", fields);

        DiaryEntry entry = dao.getByPath(path);

        assertNotNull(entry);
        assertEquals(title, entry.getTitle());
        assertEquals(text, entry.getText());
        assertEquals(created, entry.getCreatedAt());
    }

    @Test
    public void testDeleteByPathDeletesExistingFile() throws Exception {
        DBNoteDataObject dao = new DBNoteDataObject();

        String path = createEntryFile("entry_to_delete.json", new HashMap<>());
        assertTrue(Files.exists(Paths.get(path)));

        boolean deleted = dao.deleteByPath(path);

        assertTrue(deleted);
        assertFalse(Files.exists(Paths.get(path)));
    }

    @Test
    public void testDeleteByPathReturnsFalseWhenFileMissing() {
        DBNoteDataObject dao = new DBNoteDataObject();

        String nonExistentPath = tempBaseDir.resolve("no_such_file.json").toString();

        boolean deleted = dao.deleteByPath(nonExistentPath);

        assertFalse(deleted);
    }

    @Test
    public void testGetAllReturnsAllEntries() throws Exception {
        DBNoteDataObject dao = new DBNoteDataObject();

        // Create two entry files under the default BASE_DIR location
        Path baseDir = Paths.get(DiaryEntry.BASE_DIR);
        Files.createDirectories(baseDir);

        Map<String, Object> fields1 = new HashMap<>();
        fields1.put("title", "Entry One");
        fields1.put("text", "First entry text");
        fields1.put("created_date", LocalDateTime.of(2024, 1, 1, 10, 0).toString());
        String path1 = baseDir.resolve("1) Entry One.json").toString();
        fields1.put("storage_path", path1);

        Map<String, Object> fields2 = new HashMap<>();
        fields2.put("title", "Entry Two");
        fields2.put("text", "Second entry text");
        fields2.put("created_date", LocalDateTime.of(2024, 1, 2, 11, 0).toString());
        String path2 = baseDir.resolve("2) Entry Two.json").toString();
        fields2.put("storage_path", path2);

        // Actually write the files
        createEntryFileInBaseDir(baseDir, "1) Entry One.json", fields1);
        createEntryFileInBaseDir(baseDir, "2) Entry Two.json", fields2);

        List<Map<String, Object>> all = dao.getAll();

        assertNotNull(all);
        assertTrue(all.size() >= 2);
        assertTrue(all.stream().anyMatch(e -> "Entry One".equals(e.get("title"))));
        assertTrue(all.stream().anyMatch(e -> "Entry Two".equals(e.get("title"))));
    }

    /**
     * Helper used in getAll test to write directly under DiaryEntry.BASE_DIR.
     */
    private void createEntryFileInBaseDir(Path baseDir, String fileName, Map<String, Object> fields) throws IOException {
        JSONObject json = new JSONObject();
        json.put("title", fields.getOrDefault("title", "Test Title"));
        json.put("text", fields.getOrDefault("text", "This is some test diary text."));
        json.put("keywords", fields.getOrDefault("keywords", List.of("happy", "test")));
        json.put("created_date", fields.getOrDefault("created_date", LocalDateTime.now().toString()));
        json.put("updated_date", fields.getOrDefault("updated_date", LocalDateTime.now().toString()));
        json.put("storage_path", fields.getOrDefault("storage_path", baseDir.resolve(fileName).toString()));

        Path filePath = baseDir.resolve(fileName);
        Files.writeString(filePath, json.toString(4));
    }

    @Test
    public void testSavePersistsDiaryEntryToFile() throws Exception {
        DBNoteDataObject dao = new DBNoteDataObject();

        DiaryEntry entry = new DiaryEntry("Save Test", "This is a test body that is sufficiently long to be realistic.", LocalDateTime.of(2024, 1, 3, 9, 0));

        boolean saved = dao.save(entry);

        assertTrue(saved);

        String storagePath = entry.getStoragePath();
        assertTrue(Files.exists(Paths.get(storagePath)));

        String content = Files.readString(Paths.get(storagePath));
        JSONObject json = new JSONObject(content);

        assertEquals("Save Test", json.getString("title"));
        assertEquals("This is a test body that is sufficiently long to be realistic.", json.getString("text"));
        assertEquals(storagePath, json.getString("storage_path"));
    }
}

