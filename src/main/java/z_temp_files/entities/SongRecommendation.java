package z_temp_files.entities;

public class SongRecommendation implements RecommendationItem {
    private int itemId;
    private int releaseYear;
    private String imageUrl;

    private String songName;
    private String artistName;
    private int popularityScore;
    private String externalUrl;

    @Override
    public int getItemId() { return itemId; }

    @Override
    public int getReleaseYear() { return releaseYear; }

    @Override
    public String getImageUrl() { return imageUrl; }

    public String getSongName() { return songName; }
    public String getArtistName() { return artistName; }
    public int getPopularityScore() { return popularityScore; }
    public String getExternalUrl() { return externalUrl; }
}
