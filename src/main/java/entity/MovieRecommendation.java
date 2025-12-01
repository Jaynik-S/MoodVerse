package entity;

public class MovieRecommendation {
    private String releaseYear;
    private String imageUrl;
    private String movieTitle;
    private String movieRating;
    private String overview;

    public MovieRecommendation(String releaseYear, String imageUrl,
                             String movieTitle, String movieRating,
                             String overview) {
        this.releaseYear = releaseYear;
        this.imageUrl = imageUrl;
        this.movieTitle = movieTitle;
        this.movieRating = movieRating;
        this.overview = overview;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public String getOverview() {
        return overview;
    }
}
