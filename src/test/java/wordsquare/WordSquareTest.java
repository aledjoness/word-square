package wordsquare;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import wordsquare.domain.Dictionary;

import static org.assertj.core.api.Assertions.assertThat;

class WordSquareTest {

    private WordSquare wordSquare;
    private final Dictionary dictionary = Mockito.mock(Dictionary.class);

    @Test
    void isValidIfNumberOfCharactersEqualsSquareOfSideLength() {
        wordSquare = new WordSquare(5, "abcdefghijklmnopqrstuvwxy", dictionary);
        assertThat(wordSquare.isNotValid()).isFalse();
    }

    @Test
    void isInvalidIfNumberOfCharactersDoesNotEqualSquareOfSideLength() {
        wordSquare = new WordSquare(5, "abcde", dictionary);
        assertThat(wordSquare.isNotValid()).isTrue();
    }

    @Test
    void isInvalidIfSizeIs1() {
        wordSquare = new WordSquare(1, "a", dictionary);
        assertThat(wordSquare.isNotValid()).isTrue();
    }
}
