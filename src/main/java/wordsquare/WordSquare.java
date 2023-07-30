package wordsquare;

import wordsquare.domain.Dictionary;
import wordsquare.domain.Solution;

import java.util.List;

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

    public List<Solution> solve() {
        return characters.solve();
    }
}
