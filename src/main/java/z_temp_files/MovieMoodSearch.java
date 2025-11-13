package z_temp_files;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.cdimascio.dotenv.Dotenv;

public class MovieMoodSearch {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String TMDB_API_KEY = dotenv.get("TMDB_API_KEY");

    private static final List<List<String>> KEYWORDS = List.of(
            List.of("journal", "morning", "commute", "work", "school", "relationship", "stress",
                    "exercise", "study", "dinner", "weekend", "rain", "gratitude"),
            List.of("breakfast", "coffee", "alarm", "late", "deadline", "meeting",
                    "lunch", "emails", "project", "groceries", "cleaning", "cooking",
                    "relax", "sleep", "tired"),
            List.of("happiness", "anxiety", "lonely", "motivation", "focus", "progress",
                    "achievement", "argument", "friends", "family", "plans", "hope",
                    "frustration", "reflection", "improvement")
    );
    private static int limit = 7;

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> ids = getKeywordIds(KEYWORDS.get(2));
        List<JSONObject> movies = discoverMovies(ids);

        for (JSONObject m : movies) {
            String title = m.optString("title", "—");
            String year = m.optString("release_date", "—");
            year = year.isEmpty() ? "—" : year.substring(0, Math.min(4, year.length()));

            double voteAvg = m.optDouble("vote_average", 0.0);
            int voteCount = m.optInt("vote_count", 0);
            String overview = m.optString("overview", "");
            String posterPath = m.optString("poster_path", null);
            String posterUrl = (posterPath == null || posterPath.isEmpty())
                    ? "—"
                    : "https://image.tmdb.org/t/p/original" + posterPath;

            System.out.printf("%s (%s)  ★ %.1f  votes:%d%n", title, year, voteAvg, voteCount);
            System.out.println(overview);
            System.out.println(posterUrl);
            System.out.println();
        }
    }

    private static List<String> getKeywordIds(List<String> terms) throws IOException, InterruptedException {
        List<String> ids = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();

        for (String t : terms) {
            String url = String.format(
                    "https://api.themoviedb.org/3/search/keyword?api_key=%s&query=%s&page=1",
                    TMDB_API_KEY,
                    URLEncoder.encode(t, StandardCharsets.UTF_8)
            );

            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() >= 200 && res.statusCode() < 300) {
                JSONArray results = new JSONObject(res.body()).optJSONArray("results");
                if (results != null && results.length() > 0) {
                    String id = String.valueOf(results.getJSONObject(0).optInt("id"));
                    ids.add(id);
                }
            } else {
                throw new IOException("TMDb keyword search failed: " + res.statusCode());
            }
        }
        return ids;
    }

    private static List<JSONObject> discoverMovies(List<String> ids)
            throws IOException, InterruptedException {
        if (ids == null || ids.isEmpty()) return List.of();

        HttpClient client = HttpClient.newHttpClient();
        Set<JSONObject> collected = new LinkedHashSet<>();

        List<String> queries = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            for (int j = i + 1; j < ids.size(); j++) {
                queries.add(ids.get(i) + "," + ids.get(j));
            }
        }
        queries.add(String.join("|", ids));

        for (String keywordStr : queries) {
            if (collected.size() >= limit) break;
            String encoded = URLEncoder.encode(keywordStr, StandardCharsets.UTF_8);

            String url = String.format(
                    "https://api.themoviedb.org/3/discover/movie?api_key=%s&with_keywords=%s&include_adult=false&sort_by=vote_average.desc&vote_count.gte=350&language=en-US&page=1",
                    TMDB_API_KEY,
                    encoded
            );

            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() < 200 || res.statusCode() >= 300) {
                throw new IOException("TMDb discover failed: " + res.statusCode());
            }

            JSONArray results = new JSONObject(res.body()).optJSONArray("results");
            if (results == null) continue;

            for (int i = 0; i < results.length(); i++) {
                collected.add(results.getJSONObject(i));
                if (collected.size() >= limit) break;
            }
        }

        return new ArrayList<>(collected).subList(0, Math.min(collected.size(), limit));
    }
}
