package interface_adapter.recommendation;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple state holder for recommendation view.
 */
public class RecommendationState {
    private List<String> songs = new ArrayList<>();
    private List<String> movies = new ArrayList<>();
    private String error;

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }

    public List<String> getMovies() {
        return movies;
    }

    public void setMovies(List<String> movies) {
        this.movies = movies;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
