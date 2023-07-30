package wordsquare;

import org.junit.jupiter.api.Test;
import wordsquare.domain.Dictionary;
import wordsquare.domain.Solution;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WordSquareCharactersTest {

    private WordSquareCharacters wordSquareCharacters;
    private Dictionary realDictionary;

    @Test
    void solvesSize2() {
        realDictionary = Dictionary.populate(2);
        wordSquareCharacters = new WordSquareCharacters("inna", realDictionary);

        List<Solution> solution = wordSquareCharacters.solve();
        assertThat(solution.get(0).hasBeenFound()).isTrue();
        assertThat(solution.get(0).characters()).isEqualTo(List.of("in", "na"));
    }

    @Test
    void solvesSize3() {
        realDictionary = Dictionary.populate(3);
        wordSquareCharacters = new WordSquareCharacters("aadddoost", realDictionary);

        List<Solution> solution = wordSquareCharacters.solve();
        assertThat(solution.get(0).hasBeenFound()).isTrue();
        assertThat(solution.get(0).characters()).isEqualTo(List.of("sad", "ado", "dot"));
    }

    @Test
    void solvesSize4() {
        realDictionary = Dictionary.populate(4);
        wordSquareCharacters = new WordSquareCharacters("eeeeddoonnnsssrv", realDictionary);

        List<Solution> solution = wordSquareCharacters.solve();
        assertThat(solution.get(0).hasBeenFound()).isTrue();
        assertThat(solution.get(0).characters()).isEqualTo(List.of("rose", "oven", "send", "ends"));
    }

    @Test
    void solvesSize5() {
        realDictionary = Dictionary.populate(5);
        wordSquareCharacters = new WordSquareCharacters("aabbeeeeeeeehmosrrrruttvv", realDictionary);

        List<Solution> solution = wordSquareCharacters.solve();
        assertThat(solution.get(0).hasBeenFound()).isTrue();
        assertThat(solution.get(0).characters()).isEqualTo(List.of("heart", "ember", "above", "revue", "trees"));
    }
}
