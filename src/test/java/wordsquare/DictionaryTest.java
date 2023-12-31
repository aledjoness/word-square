package wordsquare;

import org.junit.jupiter.api.Test;
import wordsquare.domain.Dictionary;
import wordsquare.domain.Dictionary.UnpopulatedDictionary;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryTest {

    private final Dictionary dictionary = Dictionary.populate(5);

    @Test
    void unpopulatedDictionaryCanNotValidateAnyWord() {
        Dictionary unpopulatedDictionary = new UnpopulatedDictionary();
        assertThat(unpopulatedDictionary.isWord("pizza")).isFalse();
    }

    @Test
    void validatesAgainstCorrectWords() {
        assertThat(dictionary.isWord("pizza")).isTrue();
    }

    @Test
    void validatesAgainstIncorrectWords() {
        assertThat(dictionary.isWord("gbvcf")).isFalse();
    }

    @Test
    void validatesOnDelimitedWords() {
        assertThat(dictionary.isWord("p", "i", "z", "z", "a")).isTrue();
    }

    @Test
    void sizeOfDictionaryPreventsLongerWordsFromBeingCorrect() {
        assertThat(dictionary.isWord("elongate")).isFalse();
    }
}
