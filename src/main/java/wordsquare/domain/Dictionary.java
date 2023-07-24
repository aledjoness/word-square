package wordsquare.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Dictionary {

    private static final String FILE_PATH = "src/main/resources/eng_words.txt";
    private List<String> words;

    private Dictionary() {
    }

    public static Dictionary populate(int size) {
        try {
            Dictionary dictionary = new Dictionary();
            dictionary.readWordsFromFile(size);
            return dictionary;
        } catch (IOException e) {
            System.err.println("Unable to populate dictionary");
            return new UnpopulatedDictionary();
        }
    }

    private void readWordsFromFile(int size) throws IOException {
        Path path = Paths.get(FILE_PATH);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            this.words = reader.lines().filter(word -> word.length() == size).toList();
        }
    }

    public boolean isWord(String... delimitedWord) {
        return words.contains(String.join("", delimitedWord));
    }

    public static class UnpopulatedDictionary extends Dictionary {

        @Override
        public boolean isWord(String... delimitedWord) {
            return false;
        }
    }
}
