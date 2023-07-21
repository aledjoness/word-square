package wordsquare;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WordSquareTest {

    private WordSquare wordSquare;

    @Test
    void isValidIfNumberOfCharactersEqualsSquareOfSideLength() {
        wordSquare = new WordSquare(5, "abcdefghijklmnopqrstuvwxy");
        assertThat(wordSquare.isNotValid()).isFalse();
    }

    @Test
    void isInvalidIfNumberOfCharactersDoesNotEqualSquareOfSideLength() {
        wordSquare = new WordSquare(5, "abcde");
        assertThat(wordSquare.isNotValid()).isTrue();
    }
}
