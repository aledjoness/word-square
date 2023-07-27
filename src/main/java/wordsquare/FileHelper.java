package wordsquare;

import wordsquare.domain.NodeValue;
import wordsquare.domain.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FileHelper {

    private FileHelper() {
    }

    public static final Path FILE_PATH = Paths.get("src/main/resources/start_nodes.txt");

    public static void writeToFile(List<Pair<List<NodeValue>, List<String>>> valueToRemainingCharacters) {
        // Save something like eosoe@aaaeefhhmnsrrrrttttw@ -- @ is a separator
        List<String> fileEntries = new LinkedList<>();
        for (Pair<List<NodeValue>, List<String>> pair : valueToRemainingCharacters) {
            List<NodeValue> nodes = pair.left();
            List<String> nodesAsStrings = nodes.stream().map(NodeValue::getValue).toList();
            String nodesAsSingleString = String.join("", nodesAsStrings);

            List<String> remainingChars = pair.right();
            String remainingCharsAsSingleString = String.join("", remainingChars);

            String fileEntry = String.format("%s@%s@", nodesAsSingleString, remainingCharsAsSingleString);
            fileEntries.add(fileEntry);
        }

        try {
            Files.write(FILE_PATH, fileEntries, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Unable to write to starts_node.txt");
        }
    }

    public static List<Pair<List<NodeValue>, List<String>>> readFile() {
        List<Pair<List<NodeValue>, List<String>>> result = new LinkedList<>();
        try {
            List<String> strings = Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8);
            for (String entry : strings) {
                String[] split = entry.split("@");
                String nodeValuesAsSingleString = split[0];
                String remainingCharsAsSingleString = split[1];
                List<String> nodeValuesAsList = Arrays.stream(nodeValuesAsSingleString.split("")).toList();
                List<NodeValue> nodeValues = nodeValuesAsList.stream().map(NodeValue::new).toList();
                List<String> remainingCharsAsList = Arrays.stream(remainingCharsAsSingleString.split("")).toList();
                Pair<List<NodeValue>, List<String>>  entryAsNodePair = Pair.of(nodeValues, remainingCharsAsList);
                result.add(entryAsNodePair);
            }
        } catch (IOException e) {
            System.err.println("Unable to read from starts_node.txt");
        }
        return result;
    }

    public static Pair<List<NodeValue>, List<String>> readFirstLine() {
        List<Pair<List<NodeValue>, List<String>>> allEntries = readFile();

        Pair<List<NodeValue>, List<String>> firstLine = allEntries.get(0);
        allEntries.remove(0);
        writeToFile(allEntries);
        return firstLine;
    }

    public static void deleteFile() {
        try {
            Files.delete(FILE_PATH);
        } catch (IOException e) {
            System.err.println("Unable to delete starts_node.txt");
        }
    }
}
