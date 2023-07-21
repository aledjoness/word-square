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

    public static Dictionary populate() {
        try {
            Dictionary dictionary = new Dictionary();
            dictionary.readWordsFromFile();
            return dictionary;
        } catch (IOException e) {
            System.err.println("Unable to populate dictionary");
            return new UnpopulatedDictionary();
        }
    }

    private void readWordsFromFile() throws IOException {
        Path path = Paths.get(FILE_PATH);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            this.words = reader.lines().toList();
            System.out.println("Words size: " + words.size());
        }
    }

    public boolean isAWord(String word) {
        return words.contains(word);
    }

    public static class UnpopulatedDictionary extends Dictionary {

        @Override
        public boolean isAWord(String word) {
            return false;
        }
    }
}
