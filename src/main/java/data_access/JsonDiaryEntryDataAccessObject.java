package data_access;

import entity.DiaryEntry;
import use_case.create_entry.CreateEntryUserDataAccessInterface;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class JsonDiaryEntryDataAccessObject implements CreateEntryUserDataAccessInterface {
    private final Path filePath;
    private final DateTimeFormatter formatter;

    public JsonDiaryEntryDataAccessObject() {
        this(Paths.get(DiaryEntry.BASE_DIR, "entries.json"));
    }
    public JsonDiaryEntryDataAccessObject(Path filePath) {
        this.filePath = filePath;
        this.formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    }

    @Override
    public void save(DiaryEntry entry) {
        try {
            String json = readOrInit();
            List<String> objects = sliceObjectsFromTopArray(json);

            String idMarker = "\"entry_id\": " + entry.getEntryId();
            int i = 0;
            boolean replaced = false;
            while (i < objects.size()) {
                String obj =  objects.get(i);
                if (obj.contains(idMarker)) {
                    objects.set(i, serializeEntry(entry));
                    replaced = true;
                }
                i = i + 1;
            }
            if (!replaced) {
                objects.add(serializeEntry(entry));
            }

            String newDoc = wrapDocument(objects);
            write(newDoc);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save diary entry", e);
        }
    }
    @Override
    public boolean existsById(int entryId) {
        try {
            String json = readOrInit();
            String marker = "\"entry_id\": " + entryId;
            return json.contains(marker);
        } catch (IOException e) {
            return false;
        }
    }
    private String readOrInit() throws IOException {
        if (Files.notExists(filePath)) {
            if (Files.notExists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            String fresh = "{\n  \"entries\": []\n}";
            write(fresh);
            return fresh;
        }
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }
    private void write(String content) throws IOException {
        Files.writeString(filePath, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
    private static List<String> sliceObjectsFromTopArray(String json) {
        List<String> result = new ArrayList<>();

        int arrStart = json.indexOf('[');
        int arrEnd = json.lastIndexOf(']');
        if (arrStart < 0 || arrEnd < arrStart) {
            return result;
        }

        String arr = json.substring(arrStart + 1, arrEnd);
        int depth = 0;
        int start = -1;
        int i = 0;

        while (i < arr.length()) {
            char c = arr.charAt(i);
            if (c == '{') {
                if (depth == 0) {
                    start = i;
                }
                depth = depth + 1;
            } else if (c == '}') {
                depth = depth - 1;
                if (depth == 0 && start >= 0) {
                    String obj = arr.substring(start, i + 1).trim();
                    result.add(obj);
                    start = -1;
                }
            }
            i = i + 1;
        }
        return result;
    }

    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        String r = s.replace("\\", "\\\\");
        r = r.replace("\"", "\\\"");
        r = r.replace("\n", "\\n");
        r = r.replace("\r", "\\r");
        return r;
    }

    private static String serializeStringArray(List<String> list) {
        StringBuilder b = new StringBuilder();
        b.append('[');
        int i = 0;
        while (i < list.size()) {
            if (i > 0) {
                b.append(", ");
            }
            b.append("\"").append(escape(list.get(i))).append('\"');
            i = i + 1;
        }
        b.append(']');
        return b.toString();
    }

    private String formatTime(java.time.LocalDateTime time) {
        if  (time == null) {
            return "";
        }
        return formatter.format(time);
    }
    private String serializeEntry(DiaryEntry entry) {
        StringBuilder b = new StringBuilder();

        b.append("{\n");
        b.append("  \"entry_id\": ").append(entry.getEntryId()).append(",\n");
        b.append("  \"title\": \"").append(escape(entry.getTitle())).append("\",\n");
        b.append("  \"text\": \"").append(escape(entry.getText())).append("\",\n");

        String text = entry.getText();
        String preview;
        if (text.length() <= 60) {
            preview = text;
        } else {
            preview = text.substring(0, 57) + "...";
        }
        b.append("  \"content_preview\": \"").append(escape(preview)).append("\",\n");

        b.append("  \"created_at\": \"").append(formatTime(entry.getCreatedAt())).append("\",\n");
        b.append("  \"updated_at\": \"").append(formatTime(entry.getUpdatedAt())).append("\",\n");

        List<String> tags = entry.getKeyword();
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        b.append("  \"tags\": ").append(serializeStringArray(tags)).append(",\n");

        b.append("  \"mood_profile\": {\n");
        b.append("    \"primary\": \"neutral\",\n");
        b.append("    \"emotions\": {\n");
        b.append("      \"joy\": 0.0,\n");
        b.append("      \"sadness\": 0.0,\n");
        b.append("      \"anger\": 0.0,\n");
        b.append("      \"fear\": 0.0,\n");
        b.append("      \"surprise\": 0.0\n");
        b.append("    }\n");
        b.append("  },\n");

        b.append("  \"recommendations\": {\n");
        b.append("    \"spotify_tracks\": [],\n");
        b.append("    \"movies\": []\n");
        b.append("  }\n");

        b.append("}");
        return b.toString();
    }
    private static String wrapDocument(List<String> objects) {
        StringBuilder b = new StringBuilder();
        b.append("{\n");
        b.append("  \"entries\": [\n");
        int i = 0;
        while (i < objects.size()) {
            if (i > 0) {
                b.append(",\n");
            }
            String indented = objects.get(i).replace("\n", "\n    ");
            b.append("    ").append(indented);
            i = i + 1;
        }
        b.append("\n  ]\n");
        b.append("}\n");
        return b.toString();
    }
}