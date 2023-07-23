package wordsquare;

import wordsquare.domain.Dictionary;
import wordsquare.domain.Solution;

public class WordSquare {

    private final int size;
    private final WordSquareCharacters characters;

    public WordSquare(int size, String characters, Dictionary dictionary) {
        this.size = size;
        this.characters = new WordSquareCharacters(characters, dictionary);
    }

    public boolean isNotValid() {
        return size <= 1 || characters.count() != (size * size);
    }

    public Solution solve() {
        if (size == 2) {
            return characters.solveSize2();
        }
        // Pick pivots, work from there
        return Solution.none();
    }
}
