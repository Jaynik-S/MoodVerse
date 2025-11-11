package entity;

import java.util.List;
public final class AnalysisResult {
    private final SentimentLabel overall;
    private final double confidence; // 0..1
    private final List<Keyword> keywords;
    public AnalysisResult(SentimentLabel overall, double confidence, List<Keyword> keywords) {
        this.overall = overall; this.confidence = confidence; this.keywords = List.copyOf(keywords);
    }
    public SentimentLabel overall() { return overall; }
    public double confidence() { return confidence; }
    public List<Keyword> keywords() { return keywords; }
}
