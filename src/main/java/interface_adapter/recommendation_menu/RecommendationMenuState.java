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

    public void setSongRecommendation(List<SongRecommendation> songRecommendation) {
        this.songRecommendationOne = songRecommendation.get(0);
        this.songRecommendationTwo = songRecommendation.get(1);
        this.songRecommendationThree = songRecommendation.get(2);
        this.songRecommendationFour = songRecommendation.get(3);
        this.songRecommendationFive = songRecommendation.get(4);
    }

    public void setMovieRecommendation(List<MovieRecommendation> movieRecommendation) {
        this.movieRecommendationOne = movieRecommendation.get(0);
        this.movieRecommendationTwo = movieRecommendation.get(1);
        this.movieRecommendationThree = movieRecommendation.get(2);
        this.movieRecommendationFour = movieRecommendation.get(3);
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
