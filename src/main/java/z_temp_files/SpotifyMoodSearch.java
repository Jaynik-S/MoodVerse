package z_temp_files;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.ArrayList; // added

public class SpotifyMoodSearch {
    static Dotenv dotenv = Dotenv.load();
    private static final String CLIENT_ID = dotenv.get("SPOTIFY_CLIENT_ID");
    private static final String CLIENT_SECRET = dotenv.get("SPOTIFY_CLIENT_SECRET");
    private static String accessToken;

    private static List<String> keywords = List.of(
            "breakfast coffee ",
            "happiness anxiety lonely motivation focus progress achievement argument friends family plans hope frustration reflection improvement",
            "journal morning commute work school relationship stress exercise study dinner weekend rain gratitude"
    );

    public static void main(String[] args) throws IOException, InterruptedException {
        accessToken = getAccessToken();
        String term = keywords.get(0);
        String years = "2006-2025";
        List<JSONObject> songs = getSongByMood(term, years, 10);

        System.out.printf("%n🎧 Songs for '%s' (%s)%n%n", term, years);
        for (JSONObject track : songs) {
            String songName = track.optString("name", "Unknown");
            String artistName = track.optJSONArray("artists") != null && track.getJSONArray("artists").length() > 0
                    ? track.getJSONArray("artists").getJSONObject(0).optString("name", "Unknown")
                    : "Unknown";
            String releaseYear = "Unknown";
            if (track.has("album")) {
                JSONObject album = track.getJSONObject("album");
                String releaseDate = album.optString("release_date", "");
                if (!releaseDate.isEmpty()) {
                    releaseYear = releaseDate.substring(0, Math.min(4, releaseDate.length()));
                }
            }
            String externalUrl = track.optJSONObject("external_urls") != null
                    ? track.getJSONObject("external_urls").optString("spotify", "")
                    : "";
            String coverUrl = "";
            if (track.has("album")) {
                JSONObject album = track.getJSONObject("album");
                JSONArray images = album.optJSONArray("images");
                if (images != null && images.length() > 0) {
                    coverUrl = images.getJSONObject(0).optString("url", "");
                }
            }
            int popularity = track.optInt("popularity", -1);

            System.out.println(
                    songName + " | " +
                    artistName + " | " +
                    releaseYear + " | " +
                    popularity + " | \n" +
                    externalUrl + "\n" +
                    coverUrl + "\n"
            );
        }
    }

    // Retrieve Spotify API token using Client Credentials Flow
    private static String getAccessToken() throws IOException, InterruptedException {
        String authUrl = "https://accounts.spotify.com/api/token";
        String body = "grant_type=client_credentials";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(authUrl))
                .header("Authorization", "Basic " + java.util.Base64.getEncoder()
                        .encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        return json.getString("access_token");
    }

    // changed: now returns List<JSONObject> and no printing
    private static List<JSONObject> getSongByMood(String keywords, String yearRange, int limit)
            throws IOException, InterruptedException {

        if (limit <= 0) return List.of();

        String q = String.format("(%s) year:%s", keywords, yearRange);
        System.out.println(q);
        String encoded = URLEncoder.encode(q, StandardCharsets.UTF_8);
        String url = String.format("https://api.spotify.com/v1/search?q=%s&type=track&limit=%d", encoded, limit);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Spotify search failed: " + response.statusCode());
        }

        JSONObject json = new JSONObject(response.body());
        JSONArray tracks = json.optJSONObject("tracks") != null
                ? json.getJSONObject("tracks").optJSONArray("items")
                : null;

        List<JSONObject> results = new ArrayList<>();
        if (tracks != null) {
            for (int i = 0; i < tracks.length(); i++) {
                results.add(tracks.getJSONObject(i));
            }
        }
        return results;
    }
}