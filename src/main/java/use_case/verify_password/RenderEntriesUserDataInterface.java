package use_case.verify_password;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface RenderEntriesUserDataInterface {
    List<Map<String, Object>> getAll() throws IOException;
}
