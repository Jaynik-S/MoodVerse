package data_access;


import entity.DiaryEntry;
import org.json.JSONArray;
import use_case.delete_entry.DeleteEntryUserDataAccessInterface;
import use_case.load_entry.LoadEntryUserDataAccessInterface;
import use_case.save_entry.SaveEntryUserDataAccessInterface;
import use_case.verify_password.RenderEntriesUserDataInterface;

import java.nio.file.DirectoryStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.util.Map;

public class DBNoteDataObject implements DeleteEntryUserDataAccessInterface, LoadEntryUserDataAccessInterface,
        SaveEntryUserDataAccessInterface, RenderEntriesUserDataInterface {

    @Override
    public DiaryEntry getByPath(String entryPath) throws Exception {
        try {
            Map<String, Object> data = parseEntry(entryPath);
            return new DiaryEntry(
                    (String) data.get("title"),
                    (String) data.get("text"),
                    (LocalDateTime) data.get("createdDate")
            );

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean deleteByPath(String entryPath) {
        if (!existsByPath(entryPath)) {
            return false;
        }
        try {
            Path path = Paths.get(entryPath);
            Files.delete(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean save(DiaryEntry entry) throws Exception {
        try {
            JSONObject json = new JSONObject();
            json.put("title", entry.getTitle());
            json.put("text", entry.getText());
            json.put("keywords", entry.getKeywords());
            json.put("created_date", entry.getCreatedAt());
            json.put("updated_date", entry.getUpdatedAt());
            String storagePath = entry.getStoragePath();
            json.put("storage_path", storagePath);

            Path path = Paths.get(storagePath);
            Files.writeString(path, json.toString(4));
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getAll() throws Exception {
        List<Map<String, Object>> allEntries = new ArrayList<>();
        String BASE_DIR = DiaryEntry.BASE_DIR;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(BASE_DIR), "*.json")) {
            for (Path filePath : stream) {
                Map<String, Object> entry = parseEntry(filePath.toString());
                if (entry != null) {
                    allEntries.add(entry);
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return allEntries;
    }

    private boolean existsByPath(String entryPath)  {
        Path path = Paths.get(entryPath);
        return Files.exists(path);
    }

    private Map<String, Object> parseEntry(String entryPath) throws Exception {
        if (!existsByPath(entryPath)) { return null;  }
        try {
            String content = Files.readString(Paths.get(entryPath));
            JSONObject json = new JSONObject(content);

            String title = json.getString("title");
            String text = json.getString("text");
            JSONArray keywordArray = json.getJSONArray("keywords");
            String createdDateStr = json.getString("created_date");
            String updatedDataStr = json.getString("updated_date");
            String storagePath = json.getString("storage_path");

            List<String> keywords =
                    keywordArray.toList().stream()
                            .map(Object::toString)
                            .toList();
            LocalDateTime createdDate = LocalDateTime.parse(createdDateStr);
            LocalDateTime updatedDate = LocalDateTime.parse(updatedDataStr);

            Map<String, Object> result = new HashMap<>();
            result.put("title", title);
            result.put("text", text);
            result.put("keywords", keywords);
            result.put("createdDate", createdDate);
            result.put("updatedDate", updatedDate);
            result.put("storagePath", storagePath);

            return result;

        } catch (Exception e) {
            throw e;
        }
    }



}
