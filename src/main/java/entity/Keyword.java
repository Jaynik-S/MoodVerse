package entity;

public final class Keyword {
    private final String text;
    private final double score; // 0..1 importance within this note

    public Keyword(String text, double score) {
        this.text = text;
        this.score = score;
    }

    public String text() {
        return text;
    }

    public double score() {
        return score;
    }
}
