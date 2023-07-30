package wordsquare;

import wordsquare.domain.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WordSquareCharacters {

    private final List<String> inputCharacters;
    private final Dictionary dictionary;

    public WordSquareCharacters(String rawInputCharacters, Dictionary dictionary) {
        this.inputCharacters = Arrays.stream(rawInputCharacters.split("")).toList();
        this.dictionary = dictionary;
    }

    public int count() {
        return inputCharacters.size();
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
                Node nextNode = Node.fromPreviousNode(previousNode);
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
