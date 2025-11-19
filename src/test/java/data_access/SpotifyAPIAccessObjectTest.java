package data_access;

import entity.SongRecommendation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpotifyAPIAccessObjectTest {

    @Test
    public void testJSONtoSongRecommendation_AllFieldsPresent() {
        JSONObject artist = new JSONObject().put("name", "Artist Name");
        JSONArray artists = new JSONArray().put(artist);
        JSONArray images = new JSONArray().put(new JSONObject().put("url", "http://image/cover.jpg"));
        JSONObject album = new JSONObject()
                .put("release_date", "2020-05-01")
                .put("images", images);
        JSONObject externalUrls = new JSONObject().put("spotify", "http://spotify/track");

        JSONObject track = new JSONObject()
                .put("name", "Song Name")
                .put("artists", artists)
                .put("album", album)
                .put("external_urls", externalUrls)
                .put("popularity", "87");

        SpotifyAPIAccessObject dao = new SpotifyAPIAccessObject(List.of("happy"));
        SongRecommendation rec = dao.JSONtoSongRecommendation(track);

        assertEquals("2020", rec.getReleaseYear());
        assertEquals("http://image/cover.jpg", rec.getImageUrl());
        assertEquals("Song Name", rec.getSongName());
        assertEquals("Artist Name", rec.getArtistName());
        assertEquals("87/100", rec.getPopularityScore());
        assertEquals("http://spotify/track", rec.getExternalUrl());
    }

    @Test
    public void testJSONtoSongRecommendation_MissingOptionalFields() {
        JSONObject track = new JSONObject()
                .put("name", "Unknown Song");

        SpotifyAPIAccessObject dao = new SpotifyAPIAccessObject(List.of("sad"));
        SongRecommendation rec = dao.JSONtoSongRecommendation(track);

        assertEquals("Unknown", rec.getReleaseYear());
        assertEquals("", rec.getImageUrl());
        assertEquals("Unknown Song", rec.getSongName());
        assertEquals("Unknown", rec.getArtistName());
        assertEquals("/100", rec.getPopularityScore());
        assertEquals("", rec.getExternalUrl());
    }

    @Test
    public void testFetchSongRecommendations_RealAPI_WhenCredentialsPresent() throws Exception {
        String clientId = System.getenv("SPOTIFY_CLIENT_ID");
        String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");

        if (clientId == null || clientId.isEmpty() || clientSecret == null || clientSecret.isEmpty()) {
            return;
        }

        SpotifyAPIAccessObject dao = new SpotifyAPIAccessObject(List.of("adventure", "friendship"));
        List<SongRecommendation> recs = dao.fetchSongRecommendations();
        assertNotNull(recs);
        assertFalse(recs.isEmpty());
        for (SongRecommendation rec : recs) {
            assertNotNull(rec.getSongName());
            assertNotNull(rec.getArtistName());
            System.out.println("Song: " + rec.getSongName() + " & Artist: " + rec.getArtistName());
        }
    }
}
