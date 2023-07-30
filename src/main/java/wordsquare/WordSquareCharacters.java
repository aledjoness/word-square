package wordsquare;

import wordsquare.domain.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WordSquareCharacters {

    private final List<String> inputCharacters;
    private final int numberOfUniqueDuplicates;
    private final List<String> duplicateCharacters;
    private final List<String> nonDuplicateCharacters;
    private final Dictionary dictionary;

    public WordSquareCharacters(String rawInputCharacters, Dictionary dictionary) {
        this.inputCharacters = Arrays.stream(rawInputCharacters.split("")).toList();
        this.dictionary = dictionary;
        Map<String, Long> charactersToOccurrences = inputCharacters.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        numberOfUniqueDuplicates = (int) charactersToOccurrences.entrySet().stream().filter(entry -> entry.getValue() > 1).map(Map.Entry::getKey).count();
        duplicateCharacters = charactersToOccurrences.entrySet().stream().filter(entry -> entry.getValue() > 1).map(Map.Entry::getKey).toList();
        nonDuplicateCharacters = charactersToOccurrences.entrySet().stream().filter(entry -> entry.getValue() == 1).map(Map.Entry::getKey).toList();
    }

    public int count() {
        return inputCharacters.size();
    }

    private String getCharAt(int index) {
        return inputCharacters.get(index);
    }

    private String getDupeCharAt(int index) {
        return duplicateCharacters.get(index);
    }

    private String getNonDupeCharAt(int index) {
        return nonDuplicateCharacters.get(index);
    }

    public Solution solveSize2() {
        // Top right and bottom left are always the same
        // i.e. there are no dupes
        if (duplicateCharacters.isEmpty()) {
            return Solution.none();
        }
        // i.e. there are only duplicates of the same character
        if (numberOfUniqueDuplicates == 1 && nonDuplicateCharacters.isEmpty()) {
            // all characters are the same, so can pull out the first index twice
            if (dictionary.isWord(getCharAt(0), getCharAt(0))) {
                return new Solution(inputCharacters);
            } else {
                return Solution.none();
            }
        }
        // i.e. there are 2 unique dupes
        if (numberOfUniqueDuplicates == 2) {
            if (dictionary.isWord(getDupeCharAt(0), getDupeCharAt(1))
                    && dictionary.isWord(getDupeCharAt(1), getDupeCharAt(0))) {
                return new Solution(List.of(getDupeCharAt(0), getDupeCharAt(1),
                                            getDupeCharAt(1), getDupeCharAt(0)));
            }
            return Solution.none();
        }
        // i.e. there is 1 dupe
        if (dictionary.isWord(getNonDupeCharAt(0), getDupeCharAt(0))
                && dictionary.isWord(getDupeCharAt(0), getNonDupeCharAt(1))) {
            return new Solution(List.of(getNonDupeCharAt(0), getDupeCharAt(0),
                                        getDupeCharAt(0), getNonDupeCharAt(1)));
        }
        if (dictionary.isWord(getNonDupeCharAt(1), getDupeCharAt(0))
                && dictionary.isWord(getDupeCharAt(0), getNonDupeCharAt(0))) {
            return new Solution(List.of(getNonDupeCharAt(1), getDupeCharAt(0),
                                        getDupeCharAt(0), getNonDupeCharAt(0)));
        }
        return Solution.none();
    }

    public List<Solution> solve() {
        System.out.println("--- Starting permutation of initial combinations ---");
        Node initNode = Node.initialiseNode(inputCharacters);
        // Initialises all potential nodes into start_nodes.txt
        int numLines = initNode.initialise();
        System.out.println("--- Finished permuting all initial combinations ---");
        System.out.println("--- Looking for solution... ---");

        for (int i = 0; i < numLines; i++) {
            Pair<List<NodeValue>, List<String>> nodeToRemainingCharacters = FileHelper.readFirstLine();
            System.out.println("Assessing viability of next permutation (" + nodeToRemainingCharacters + ")");
            Node startNode = new Node(0, nodeToRemainingCharacters.left(), nodeToRemainingCharacters.right(), null);

            // Viable solutions are one where the top and left side word are valid English words
            List<LinkedList<Node>> viableTopHalfSolutions = new LinkedList<>();
            startNode.calculateViableSolutions(viableTopHalfSolutions, inputCharacters.size(), dictionary);

            // For each viable solution, calculate whether it is actually a solution by now using the remaining
            // characters to populate the bottom half of the grid
            // Set the previous of the last node to the new node we're creating, pass in the remaining characters from
            // that node, so we should end up with a complete set
            List<List<String>> viableTotalSolutions = new LinkedList<>();
            for (LinkedList<Node> viableSolution : viableTopHalfSolutions) {
                Node previousNode = viableSolution.get(0);
                Node nextNode = new Node(previousNode.getPosition(), previousNode.getValue(), previousNode.getRemainingCharacters(), previousNode.getPrevious());
                nextNode.calculateBottomHalfViableSolutions(viableTotalSolutions, inputCharacters.size(), dictionary);
                if (!viableTotalSolutions.isEmpty()) {
                    // Solution found
                    return viableTotalSolutions.stream().map(Solution::new).toList();
                }
            }
        }
        return List.of(Solution.none());
    }
}
