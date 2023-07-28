package wordsquare;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import wordsquare.domain.Dictionary;
import wordsquare.domain.Solution;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class WordSquareCharactersTest {

    private final Dictionary dictionary = Mockito.mock(Dictionary.class);

    private WordSquareCharacters wordSquareCharacters;

    @Test
    void size2RequiresAtLeastOneDuplicatePair() {
        wordSquareCharacters = new WordSquareCharacters("abcd", dictionary);

        Solution solution = wordSquareCharacters.solveSize2();
        assertThat(solution.hasBeenFound()).isFalse();
    }

    @Test
    void solvesSize2WithAllDuplicateCharacters() {
        wordSquareCharacters = new WordSquareCharacters("oooo", dictionary);
        when(dictionary.isWord("o", "o")).thenReturn(true);

        Solution solution = wordSquareCharacters.solveSize2();
        assertThat(solution.hasBeenFound()).isTrue();
        assertThat(solution.characters()).isEqualTo(List.of("o", "o", "o", "o"));
    }

    @Test
    void size2AllDuplicateCharactersMustBeAValidWord() {
        wordSquareCharacters = new WordSquareCharacters("vvvv", dictionary);
        when(dictionary.isWord("v", "v")).thenReturn(false);

        Solution solution = wordSquareCharacters.solveSize2();
        assertThat(solution.hasBeenFound()).isFalse();
        assertThat(solution.characters()).isEmpty();
    }

    @Test
    void solvesSize2WithTwoDuplicates() {
        wordSquareCharacters = new WordSquareCharacters("nnoo", dictionary);
        when(dictionary.isWord("o", "n")).thenReturn(true);
        when(dictionary.isWord("n", "o")).thenReturn(true);

        Solution solution = wordSquareCharacters.solveSize2();
        assertThat(solution.hasBeenFound()).isTrue();
        assertThat(solution.characters()).isEqualTo(List.of("n", "o", "o", "n"));
    }

    @Test
    void size2WithTwoDuplicatesMustBeValidWords() {
        wordSquareCharacters = new WordSquareCharacters("wiwi", dictionary);
        when(dictionary.isWord("i", "w")).thenReturn(false);
        when(dictionary.isWord("w", "i")).thenReturn(false);

        Solution solution = wordSquareCharacters.solveSize2();
        assertThat(solution.hasBeenFound()).isFalse();
        assertThat(solution.characters()).isEmpty();
    }

    @Test
    void solvesSize2WithOneDuplicatePartOne() {
        wordSquareCharacters = new WordSquareCharacters("ihaa", dictionary);
        when(dictionary.isWord("h", "a")).thenReturn(true);
        when(dictionary.isWord("a", "i")).thenReturn(true);

        Solution solution = wordSquareCharacters.solveSize2();
        assertThat(solution.hasBeenFound()).isTrue();
        assertThat(solution.characters()).isEqualTo(List.of("h", "a", "a", "i"));
    }

    @Test
    void solvesSize2WithOneDuplicatePartTwo() {
        wordSquareCharacters = new WordSquareCharacters("inna", dictionary);
        when(dictionary.isWord("a", "n")).thenReturn(true);
        when(dictionary.isWord("n", "i")).thenReturn(false);
        when(dictionary.isWord("i", "n")).thenReturn(true);
        when(dictionary.isWord("n", "a")).thenReturn(true);

        Solution solution = wordSquareCharacters.solveSize2();
        assertThat(solution.hasBeenFound()).isTrue();
        assertThat(solution.characters()).isEqualTo(List.of("i", "n", "n", "a"));
    }

    @Test
    void size2OneDuplicatePairMustBeAValidWord() {
        wordSquareCharacters = new WordSquareCharacters("adkk", dictionary);
        when(dictionary.isWord("a", "k")).thenReturn(false);
        when(dictionary.isWord("d", "k")).thenReturn(false);

        Solution solution = wordSquareCharacters.solveSize2();
        assertThat(solution.hasBeenFound()).isFalse();
        assertThat(solution.characters()).isEmpty();
    }

    @Test
    void solvesSize3() {
        Dictionary realDictionary = Dictionary.populate(3);
        wordSquareCharacters = new WordSquareCharacters("aadddoost", realDictionary);

        List<Solution> solution = wordSquareCharacters.solve();
        assertThat(solution.get(0).hasBeenFound()).isTrue();
        assertThat(solution.get(0).characters()).isEqualTo(List.of("sad", "ado", "dot"));
    }

    @Test
    void solvesSize4() {
        Dictionary realDictionary = Dictionary.populate(4);
        wordSquareCharacters = new WordSquareCharacters("eeeeddoonnnsssrv", realDictionary);

        List<Solution> solution = wordSquareCharacters.solve();
        assertThat(solution.get(0).hasBeenFound()).isTrue();
        assertThat(solution.get(0).characters()).isEqualTo(List.of("rose", "oven", "send", "ends"));
    }

    @Test
    void solvesSize5() {
        Dictionary realDictionary = Dictionary.populate(5);
        wordSquareCharacters = new WordSquareCharacters("aabbeeeeeeeehmosrrrruttvv", realDictionary);

        List<Solution> solution = wordSquareCharacters.solve();
        assertThat(solution.get(0).hasBeenFound()).isTrue();
        assertThat(solution.get(0).characters()).isEqualTo(List.of("heart", "ember", "above", "revue", "trees"));
    }
}
