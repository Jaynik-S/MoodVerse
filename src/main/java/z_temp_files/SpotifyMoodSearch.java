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
//import io.github.cdimascio.dotenv.Dotenv;

public class SpotifyMoodSearch {

//    Dotenv dotenv = Dotenv.load();
//    private static final String CLIENT_ID = dotenv.get("SPOTIFY_CLIENT_ID");
//    private static final String CLIENT_SECRET = dotenv.get("SPOTIFY_CLIENT_SECRET");

    private static final String CLIENT_ID = "637126d66b1a4cf186efd2774592c16a";
    private static final String CLIENT_SECRET = "b8641bb30455417abebc76a5636cc61b";

    private static String accessToken;

    public static void main(String[] args) throws IOException, InterruptedException {
        accessToken = getAccessToken();

//        List<String> moods = List.of(
//                "beach vibes summer",
//                "forest ambient nature",
//                "energetic workout"
//        );

        List<String> moods = List.of(
            "breakfast coffee alarm late deadline meeting lunch emails project groceries cleaning cooking relax sleep tired",
            "happiness anxiety lonely motivation focus progress achievement argument friends family plans hope frustration reflection improvement",
            "journal morning commute work school relationship stress exercise study dinner weekend rain gratitude"
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


    private static void getSongsByMood(String keywords, String yearRange, int limit)
            throws IOException, InterruptedException {

        String q = String.format("(%s) year:%s", keywords, yearRange);
        String encoded = URLEncoder.encode(q, StandardCharsets.UTF_8);
        String url = String.format("https://api.spotify.com/v1/search?q=%s&type=track&limit=%d", encoded, limit);

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

            String songName = track.optString("name", "Unknown");
            String artistName = track.optJSONArray("artists") != null && track.getJSONArray("artists").length() > 0
                    ? track.getJSONArray("artists").getJSONObject(0).optString("name", "Unknown")
                    : "Unknown";

            // release_year from album.release_date (YYYY or YYYY-MM-DD)
            String releaseYear = "Unknown";
            if (track.has("album")) {
                JSONObject album = track.getJSONObject("album");
                String releaseDate = album.optString("release_date", "");
                if (!releaseDate.isEmpty()) {
                    releaseYear = releaseDate.substring(0, Math.min(4, releaseDate.length()));
                }
            }

            // external_url from track.external_urls.spotify
            String externalUrl = track.optJSONObject("external_urls") != null
                    ? track.getJSONObject("external_urls").optString("spotify", "")
                    : "";

            // cover_url from album.images[0].url (largest first)
            String coverUrl = "";
            if (track.has("album")) {
                JSONObject album = track.getJSONObject("album");
                JSONArray images = album.optJSONArray("images");
                if (images != null && images.length() > 0) {
                    coverUrl = images.getJSONObject(0).optString("url", "");
                }
            }

            int popularity = track.optInt("popularity", -1); // optional: keep as a proxy metric

            System.out.printf(
                    "%d)\n" +
                            "  song_name: %s\n" +
                            "  artist_name: %s\n" +
                            "  release_year: %s\n" +
                            "  external_url (song link): %s\n" +
                            "  cover_url (image path): %s\n" +
                            "  popularity (0-100): %s%n%n",
                    i + 1, songName, artistName, releaseYear, externalUrl, coverUrl,
                    (popularity >= 0 ? String.valueOf(popularity) : "Unknown")
            );
        }
    }
}