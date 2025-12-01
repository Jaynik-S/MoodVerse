package entity;

public class SongRecommendation {
    private String releaseYear;
    private String imageUrl;
    private String songName;
    private String artistName;
    private String popularityScore;
    private String externalUrl;

    public SongRecommendation(String releaseYear, String imageUrl,
                              String songName, String artistName,
                              String popularityScore, String externalUrl) {
        this.releaseYear = releaseYear;
        this.imageUrl = imageUrl;
        this.songName = songName;
        this.artistName = artistName;
        this.popularityScore = popularityScore;
        this.externalUrl = externalUrl;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getPopularityScore() {
        return popularityScore;
    }

    public String getExternalUrl() {
        return externalUrl;
    }
}
