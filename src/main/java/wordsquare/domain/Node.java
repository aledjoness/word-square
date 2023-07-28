package wordsquare.domain;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import wordsquare.FileHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Node {

    private final int position; // e.g. 0 = middle diagonal
    private final List<NodeValue> value;
    private final List<String> remainingCharacters;
    private final Node previous;

    public Node(int position, List<NodeValue> value, List<String> remainingCharacters, Node previous) {
        this.position = position;
        this.value = value;
        this.remainingCharacters = remainingCharacters;
        this.previous = previous;
    }

    public int getPosition() {
        return position;
    }

    public List<NodeValue> getValue() {
        return value;
    }

    public List<String> getRemainingCharacters() {
        return remainingCharacters;
    }

    public Node getPrevious() {
        return previous;
    }

    public static Node initialiseNode(List<String> inputCharacters) {
        return new Node(-1, new LinkedList<>(), inputCharacters, null);
    }

    public int initialise() {
        List<Pair<List<NodeValue>, List<String>>> valueToRemainingCharacters = permuteRemainingCharacters();
        FileHelper.deleteFile();
        FileHelper.writeToFile(valueToRemainingCharacters);
        int numLines = valueToRemainingCharacters.size();
        valueToRemainingCharacters.clear();
        return numLines;
    }

    private boolean isFinalNode() {
        return remainingCharacters.isEmpty();
    }

    private boolean isTopOfGrid(int numberOfInputCharacters) {
        int wordSize = (int) Math.sqrt(numberOfInputCharacters);
        int noOfGroupsPlus1 = wordSize * 2;
        return this.position == noOfGroupsPlus1 / 2 - 1;
    }

    // todo: extract numberOfInputChars to variable
    public void calculateViableSolutions(List<LinkedList<Node>> viableSolutions, int numberOfInputCharacters, Dictionary dictionary) {
        // if current node is final node, add Node trail to completeSolutions list
        // else go one position deeper
        if (isTopOfGrid(numberOfInputCharacters)) {
            // Do we have a valid word? If yes, consider it by doing a full calculation.
            // This should be a a prelim check to see if there's a word at the top.

            // Once you've found all permutations and have stored them in a file, for each line, read in and convert to
            // NodeValues to remaining characters --  then permute all groups-1

            // Calculate Node trail and add to completeSolutions list
            LinkedList<Node> nodeTrail = calculateNodeTrail();

            // todo: make a List<Word> where Word has access to Dictionary
            List<String> topAndLeftSideWord = findTopAndLeftSideWord(nodeTrail);
            if (dictionary.areWords(topAndLeftSideWord)) {
                if (!viableSolutions.contains(nodeTrail)) {
                    viableSolutions.add(nodeTrail);
                }
            }
        } else {
            // Permute remaining characters, for each permutation create new Node
            List<Pair<List<NodeValue>, List<String>>> valueToRemainingCharacters = permuteViableRemainingCharactersForNextGroup(numberOfInputCharacters, position + 1);

            for (Pair<List<NodeValue>, List<String>> permutation : valueToRemainingCharacters) {
                Node nextNode = new Node(position + 1, permutation.left(), permutation.right(), this);
                nextNode.calculateViableSolutions(viableSolutions, numberOfInputCharacters, dictionary);
            }
        }
    }

    public void calculateBottomHalfViableSolutions(List<List<String>> completeSolutions, int numberOfInputCharacters, Dictionary dictionary) {
        // if current node is final node, add Node trail to completeSolutions list
        // else go one position deeper
        if (isFinalNode()) {
            // Calculate Node trail and add to completeSolutions list
            LinkedList<Node> nodeTrail = calculateNodeTrail();

            // todo: make a List<Word> where Word has access to Dictionary
            List<String> stitchedNodes = stitchNodesTogether(nodeTrail, numberOfInputCharacters);
            if (dictionary.areWords(stitchedNodes)) {
                // We have a grid of full words
                if (!completeSolutions.contains(stitchedNodes)) {
                    completeSolutions.add(stitchedNodes);
                }
            }
        } else {
            // Permute remaining characters, for each permutation create new Node
            int nextPosition = (int) (position % (Math.sqrt(numberOfInputCharacters) - 1)) + 1;
            List<Pair<List<NodeValue>, List<String>>> valueToRemainingCharacters = permuteViableRemainingCharactersForNextGroup(numberOfInputCharacters, nextPosition);

            for (Pair<List<NodeValue>, List<String>> permutation : valueToRemainingCharacters) {
                Node nextNode = new Node(nextPosition, permutation.left(), permutation.right(), this);
                nextNode.calculateBottomHalfViableSolutions(completeSolutions, numberOfInputCharacters, dictionary);
            }
        }
    }

    private List<Pair<List<NodeValue>, List<String>>> permuteViableRemainingCharactersForNextGroup(int numberOfInputCharacters, int nextPosition) {
        int wordSizeAtBeginning = (int) Math.sqrt(numberOfInputCharacters);
        int wordSizeAtCurrentPosition = wordSizeAtBeginning - nextPosition;

        Permutation permutation = new Permutation(wordSizeAtCurrentPosition, remainingCharacters);
        return permutation.calculatePermutations2();
    }

    private List<Pair<List<NodeValue>, List<String>>> permuteRemainingCharacters() {
        int groupSizeForNextIteration = (int) Math.sqrt(remainingCharacters.size());
        Permutation permutation = new Permutation(groupSizeForNextIteration, remainingCharacters);
        return permutation.calculatePermutations();
    }

    private LinkedList<Node> calculateNodeTrail() {
        LinkedList<Node> nodeTrail = new LinkedList<>();
        nodeTrail.add(this);
        Node previousPointer = previous;
        while (previousPointer != null) {
            nodeTrail.add(previousPointer);
            previousPointer = previousPointer.previous;
        }
        return nodeTrail;
    }

    public static List<String> findTopAndLeftSideWord(List<Node> nodes) {
        nodes.removeIf(node -> node.position == -1);

        // We have only worked out the top half of the grid so far, e.g.
        // h e a r t
        // e m b e
        // a r m
        // r e
        // t
        //
        // If the top word is a word and the left hand side word is also a word, then we can nominate this as a
        // candidate for a 'full' solution check
        // We are looking for the first and last values of each node, and then we need to stitch them together
        // Because of the pattern matching and filtering we know that the top and side word will be the same, so we
        // only need to check one of them
        List<String> sideWord = new LinkedList<>();
        for (int i = 0; i < nodes.size(); i++) {
            sideWord.add(nodes.get(i).value.get(0).getValue());
        }
        return List.of(String.join("", sideWord));
    }

    public static List<String> stitchNodesTogether(List<Node> nodes, int numberOfInputCharacters) {
        // Remove start node if exists

        // Example grid below:
        //      f  e  a  s  t
        //   4  e  a  r  t  h
        //   3  a  r  m  o  r
        //   2  s  t  o  n  e
        //   1  t  h  r  e  w
        //    0  1  2  3  4
        //
        // Numbers always point diagonally up right, e.g. group 0 is ttmtt
        nodes.removeIf(node -> node.position == -1);
        LinkedList<List<NodeValue>> wordGroups = new LinkedList<>();
        for (int i = 0; i < nodes.size() / 2; i++) {
            if (i == 0) {
                // 0th node (i.e. bottom right hand of the grid, should be at the back)
                wordGroups.add(nodes.get(i).value);
            } else {
                wordGroups.addFirst(nodes.get(i).value);
            }
        }
        wordGroups.addFirst(nodes.get(nodes.size() - 1).value);
        for (int i = nodes.size() - 2; i > nodes.size() / 2 - 1; i--) {
            wordGroups.addFirst(nodes.get(i).value);
        }

        // Strategy is add the bottom numbers 4->3->2->1, then 0, then prepend side numbers 1->2->3->4
        // Use a moving window to create words
        int wordSize = (int) Math.sqrt(numberOfInputCharacters);
        int noOfGroups = wordSize * 2 - 1;

        int windowPointer = 0;
        StringBuilder concatenatedWords = new StringBuilder();
        List<NodeValue> nodeValuesToSetBackToUnexamined = new LinkedList<>();
        while (windowPointer <= noOfGroups / 2) {
            for (int i = windowPointer; i < windowPointer + wordSize; i++) {
                // We currently have word groups that look something like this:
                //
                //       d
                //    a  d  o
                // s  a  d  o  t
                // 3  1  0  2  4
                //
                // We now need to combine them into, in this case, 3x3 words
                // This is done by using a moving window (of size 3) across and reading from top to bottom
                // We need to avoid reading the same value twice, so we mark it as 'examined' and read the next
                // value in the list if we come across and examined value
                int offset = 1;
                NodeValue nodeValue = wordGroups.get(i).get(wordGroups.get(i).size() - offset);

                while (nodeValue.hasBeenExamined()) {
                    offset++;
                    nodeValue = wordGroups.get(i).get(wordGroups.get(i).size() - offset);
                }
                nodeValue.setAsExamined();
                nodeValuesToSetBackToUnexamined.add(nodeValue);
                concatenatedWords.append(nodeValue.getValue());
            }
            windowPointer++;
        }
        // Ideally wouldn't need to do this, but we share object references so other solutions that have the same
        // permutations would inadvertently have their examined status set
        nodeValuesToSetBackToUnexamined.forEach(NodeValue::setAsUnexamined);
        return ImmutableList.copyOf(Splitter.fixedLength(wordSize).split(concatenatedWords.toString()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Node node = (Node) o;
        return position == node.position && Objects.equals(value, node.value) && Objects.equals(remainingCharacters, node.remainingCharacters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, value, remainingCharacters);
    }
}
