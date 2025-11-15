package data_access;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import entity.SongRecommendation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;

public class SpotifyAPIAccessObject {
    private static Dotenv dotenv = Dotenv.load();
    private static final String CLIENT_ID = dotenv.get("SPOTIFY_CLIENT_ID");
    private static final String CLIENT_SECRET = dotenv.get("SPOTIFY_CLIENT_SECRET");
    private static String accessToken;
    private static String yearRange = "2006-2025";
    private static int limit = 4;
    private List<String> terms;

    public SpotifyAPIAccessObject(List<String> terms) {
        this.terms = terms;
    }

    // Retrieve Spotify API token using Client Credentials Flow
    private static String getAccessToken() throws IOException, InterruptedException {
        final String AUTH_URL = "https://accounts.spotify.com/api/token";
        final String BODY = "grant_type=client_credentials";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AUTH_URL))
                .header("Authorization", "Basic " + java.util.Base64.getEncoder()
                        .encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(BODY))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        return json.getString("access_token");
    }

    private List<JSONObject> getSongsByKeywords(List<String> keywords)
            throws IOException, InterruptedException {
        if (keywords == null || keywords.isEmpty() || limit <= 0) {
            return List.of();
        }

        HttpClient client = HttpClient.newHttpClient();
        List<JSONObject> collected = new ArrayList<>();
        Set<String> seenIds = new LinkedHashSet<>();

        List<String> queries = new ArrayList<>();
        for (int i = 0; i < keywords.size(); i++) {
            for (int j = i + 1; j < keywords.size(); j++) {
                queries.add(keywords.get(i) + " " + keywords.get(j));
            }
        }
        queries.add(String.join(" OR ", keywords));

        String yearClause = (yearRange != null && !yearRange.isBlank())
                ? " year:" + yearRange.trim()
                : "";

        for (String keywordStr : queries) {
            if (collected.size() >= limit) break;

            String q = "(" + keywordStr + ")" + yearClause;
            System.out.println(q);
            String encoded = URLEncoder.encode(q, StandardCharsets.UTF_8);
            int perRequest = Math.min(50, limit - collected.size()); // Spotify limit is 50

            String url = String.format(
                    "https://api.spotify.com/v1/search?q=%s&type=track&limit=%d",
                    encoded, perRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException("Spotify search failed: " + response.statusCode());
            }

            JSONObject json = new JSONObject(response.body());
            JSONObject tracksObj = json.optJSONObject("tracks");
            JSONArray items = tracksObj != null ? tracksObj.optJSONArray("items") : null;
            if (items == null) continue;

            for (int i = 0; i < items.length(); i++) {
                JSONObject track = items.getJSONObject(i);
                String id = track.optString("id", null);
                if (id == null || id.isEmpty()) continue;

                if (seenIds.add(id)) {
                    collected.add(track);
                    if (collected.size() >= limit) break;
                }
            }
        }
        return collected;
    }

    public SongRecommendation JSONtoSongRecommendation(JSONObject track) {
        String songName = track.optString("name", "Unknown");
        String artistName = track.optJSONArray("artists") != null && track.getJSONArray("artists").length() > 0
                ? track.getJSONArray("artists").getJSONObject(0).optString("name", "Unknown")
                : "Unknown";
        String releaseYear = "Unknown";
        if (track.has("album")) {
            JSONObject album = track.getJSONObject("album");
            String releaseDate = album.optString("release_date", "—");
            if (!releaseDate.isEmpty()) {
                releaseYear = releaseDate.substring(0, Math.min(4, releaseDate.length()));
            }
        }
        String externalUrl = track.optJSONObject("external_urls") != null
                ? track.getJSONObject("external_urls").optString("spotify", "—")
                : "";
        String coverUrl = "";
        if (track.has("album")) {
            JSONObject album = track.getJSONObject("album");
            JSONArray images = album.optJSONArray("images");
            if (images != null && images.length() > 0) {
                coverUrl = images.getJSONObject(0).optString("url", "—");
            }
        }
        String popularity = track.optString("popularity", "");
        popularity = popularity + "/100";

        return new SongRecommendation(releaseYear, coverUrl, songName, artistName, popularity, externalUrl);
    }

    public List<SongRecommendation> fetchSongRecommendations() throws IOException, InterruptedException {
        try {
            accessToken = getAccessToken();
            List<JSONObject> songs = getSongsByKeywords(terms);
            List<SongRecommendation> songList = new ArrayList<>();
            for (JSONObject track : songs) {
                songList.add(JSONtoSongRecommendation(track));
            }
            return songList;
        } catch (Exception e) {
            throw e;
        }
    }
}