package wordsquare;

import org.junit.jupiter.api.Test;
import wordsquare.domain.Dictionary;
import wordsquare.domain.Dictionary.UnpopulatedDictionary;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryTest {

    private final Dictionary dictionary = Dictionary.populate();

    @Test
    void unpopulatedDictionaryCanNotValidateAnyWord() {
        Dictionary unpopulatedDictionary = new UnpopulatedDictionary();
        assertThat(unpopulatedDictionary.isAWord("pizza")).isFalse();
    }

    @Test
    void validatesAgainstCorrectWords() {
        assertThat(dictionary.isAWord("pizza")).isTrue();
    }

    @Test
    void validatesAgainstIncorrectWords() {
        assertThat(dictionary.isAWord("badpizza")).isFalse();
    }
}
