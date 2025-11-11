package z_temp_files;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

//import io.github.cdimascio.dotenv.Dotenv;

public class SpotifyMoodSearch {

//    Dotenv dotenv = Dotenv.load();
//    private static final String CLIENT_ID = dotenv.get("SPOTIFY_CLIENT_ID");
//    private static final String CLIENT_SECRET = dotenv.get("SPOTIFY_CLIENT_SECRET");

    private static final String CLIENT_ID = "KEY1";
    private static final String CLIENT_SECRET = "KEY2";

    private static String accessToken;

    public static void main(String[] args) throws IOException, InterruptedException {
        accessToken = getAccessToken();

        List<String> moods = List.of(
                "mood:happy genre:pop",
                "beach vibes summer",
                "forest ambient nature",
                "energetic workout"
        );

        for (String mood : moods) {
            getSongsByMood(mood, "2006-2025", 10);
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

    // Search for songs using the Spotify Web API
    private static void getSongsByMood(String keywords, String yearRange, int limit)
            throws IOException, InterruptedException {

        String query = String.format("(%s) year:%s", keywords, yearRange).replace(" ", "%20");
        String url = String.format("https://api.spotify.com/v1/search?q=%s&type=track&limit=%d", query, limit);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        JSONArray tracks = json.getJSONObject("tracks").getJSONArray("items");

        System.out.printf("%n🎧 Songs for '%s' (%s)%n%n", keywords, yearRange);

        for (int i = 0; i < tracks.length(); i++) {
            JSONObject track = tracks.getJSONObject(i);
            String name = track.getString("name");
            String artist = track.getJSONArray("artists").getJSONObject(0).getString("name");
            int popularity = track.getInt("popularity");
            String urlTrack = track.getJSONObject("external_urls").getString("spotify");

            System.out.printf("%d. %s — %s (Popularity: %d)%n   %s%n%n",
                    i + 1, name, artist, popularity, urlTrack);
        }
    }
}
