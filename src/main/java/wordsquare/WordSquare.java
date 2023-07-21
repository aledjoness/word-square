package wordsquare;

public class WordSquare {

    private final int size;
    private final String characters;

    public WordSquare(int size, String characters) {
        this.size = size;
        this.characters = characters;
    }

    public boolean isNotValid() {
        return characters.length() != (size * size);
    }
}
