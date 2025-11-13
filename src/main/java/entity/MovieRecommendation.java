package entity;

public class MovieRecommendation implements RecommendationItem {
    private int itemId;
    private int releaseYear;
    private String imageUrl;

    private String movieTitle;
    private float movieRating;
    private int voteCount;
    private String overview;

    @Override
    public int getItemId() { return itemId; }

    @Override
    public int getReleaseYear() { return releaseYear; }

    @Override
    public String getImageUrl() { return imageUrl; }

    public String getMovieTitle() { return movieTitle; }
    public float getMovieRating() { return movieRating; }
    public int getVoteCount() { return voteCount; }
    public String getOverview() { return overview; }
}
