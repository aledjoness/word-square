package wordsquare.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Dictionary {

    private static final String FILE_PATH = "src/main/resources/eng_words.txt";
    private List<String> dictionary;

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
            this.dictionary = reader.lines().filter(word -> word.length() == size).toList();
        }
    }

    public boolean areWords(List<String> words) {
        for (String word : words) {
            if (!dictionary.contains(word)) {
                return false;
            }
        }
        return true;
    }

    public boolean isWord(String... delimitedWord) {
        return dictionary.contains(String.join("", delimitedWord));
    }

    public static class UnpopulatedDictionary extends Dictionary {

        @Override
        public boolean isWord(String... delimitedWord) {
            return false;
        }
    }
}
