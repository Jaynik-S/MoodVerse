package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeywordTest {

    @Test
    void constructorStoresTextAndScore() {
        Keyword kw = new Keyword("joy", 0.9);

        assertEquals("joy", kw.text());
        assertEquals(0.9, kw.score(), 1e-9);
    }

    @Test
    void allowsBoundaryScoresZeroAndOne() {
        Keyword low = new Keyword("low", 0.0);
        Keyword high = new Keyword("high", 1.0);

        assertEquals(0.0, low.score(), 1e-9);
        assertEquals(1.0, high.score(), 1e-9);
    }

    @Test
    void allowsNegativeAndGreaterThanOneScoresSinceNoValidation() {
        Keyword negative = new Keyword("neg", -0.5);
        Keyword big = new Keyword("big", 2.5);

        assertEquals(-0.5, negative.score(), 1e-9);
        assertEquals(2.5, big.score(), 1e-9);
    }

    @Test
    void allowsNullTextAndKeepsReference() {
        Keyword kw = new Keyword(null, 0.3);

        assertNull(kw.text());
        assertEquals(0.3, kw.score(), 1e-9);
    }
}
