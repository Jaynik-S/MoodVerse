package interface_adapter.recommendation_menu;

import entity.MovieRecommendation;
import entity.SongRecommendation;

import java.util.List;

public class RecommendationMenuState {

    private SongRecommendation songRecommendationOne;
    private SongRecommendation songRecommendationTwo;
    private SongRecommendation songRecommendationThree;
    private SongRecommendation songRecommendationFour;
    private SongRecommendation songRecommendationFive;

    private MovieRecommendation movieRecommendationOne;
    private MovieRecommendation movieRecommendationTwo;
    private MovieRecommendation movieRecommendationThree;
    private MovieRecommendation movieRecommendationFour;

    private String displayError;

    public void setError(String displayError) {

        this.displayError = displayError;
    }


    public String getError() {
        return displayError;
    }

    public void setSongRecommendation(List<SongRecommendation> songRecommendation) {
        // Defensive: handle null or lists shorter than 5
        if (songRecommendation == null) {
            this.songRecommendationOne = null;
            this.songRecommendationTwo = null;
            this.songRecommendationThree = null;
            this.songRecommendationFour = null;
            this.songRecommendationFive = null;
            return;
        }

        this.songRecommendationOne = songRecommendation.size() > 0 ? songRecommendation.get(0) : null;
        this.songRecommendationTwo = songRecommendation.size() > 1 ? songRecommendation.get(1) : null;
        this.songRecommendationThree = songRecommendation.size() > 2 ? songRecommendation.get(2) : null;
        this.songRecommendationFour = songRecommendation.size() > 3 ? songRecommendation.get(3) : null;
        this.songRecommendationFive = songRecommendation.size() > 4 ? songRecommendation.get(4) : null;
    }

    public void setMovieRecommendation(List<MovieRecommendation> movieRecommendation) {
        // Defensive: handle null or lists shorter than 4
        if (movieRecommendation == null) {
            this.movieRecommendationOne = null;
            this.movieRecommendationTwo = null;
            this.movieRecommendationThree = null;
            this.movieRecommendationFour = null;
            return;
        }

        this.movieRecommendationOne = movieRecommendation.size() > 0 ? movieRecommendation.get(0) : null;
        this.movieRecommendationTwo = movieRecommendation.size() > 1 ? movieRecommendation.get(1) : null;
        this.movieRecommendationThree = movieRecommendation.size() > 2 ? movieRecommendation.get(2) : null;
        this.movieRecommendationFour = movieRecommendation.size() > 3 ? movieRecommendation.get(3) : null;
    }

    public SongRecommendation getSongRecommendationOne() {
        return songRecommendationOne;
    }

    public SongRecommendation getSongRecommendationTwo() {
        return songRecommendationTwo;
    }

    public SongRecommendation getSongRecommendationThree() {
        return songRecommendationThree;
    }

    public SongRecommendation getSongRecommendationFour() {
        return songRecommendationFour;
    }

    public SongRecommendation getSongRecommendationFive() {
        return songRecommendationFive;
    }

    public MovieRecommendation getMovieRecommendationOne() {
        return movieRecommendationOne;
    }

    public MovieRecommendation getMovieRecommendationTwo() {
        return movieRecommendationTwo;
    }

    public MovieRecommendation getMovieRecommendationThree() {
        return movieRecommendationThree;
    }

    public MovieRecommendation getMovieRecommendationFour() {
        return movieRecommendationFour;
    }


}
