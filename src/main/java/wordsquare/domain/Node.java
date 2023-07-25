package wordsquare.domain;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.util.LinkedList;
import java.util.List;

public class Node {

    private final int position; // e.g. 0 = middle diagonal
    private final List<NodeValue> value;
    private final List<String> remainingCharacters;
    private Node previous;
    private final List<LinkedList<Node>> completeSolutions;

    public Node(int position, List<NodeValue> value, List<String> remainingCharacters, Node previous, List<LinkedList<Node>> completeSolutions) {
        this.position = position;
        this.value = value;
        this.remainingCharacters = remainingCharacters;
        this.previous = previous;
        this.completeSolutions = completeSolutions;
    }

    public boolean isFinalNode() {
        return remainingCharacters.isEmpty();
    }

    public List<LinkedList<Node>> calculateSolutions() {
        // if current node is final node, add Node trail to completeSolutions list
        // else go one position deeper

        // dd, do, ds etc.
        // permute remaining characters, for each permutation create a new Node
        if (remainingCharacters.isEmpty()) {
            // Calculate Node trail and add to completeSolutions list
            LinkedList<Node> nodeTrail = calculateNodeTrail();
            completeSolutions.add(nodeTrail);
        } else {
            // Permute remaining characters, for each permutation create new Node
            List<Pair<List<NodeValue>, List<String>>> valueToRemainingCharacters = permuteRemainingCharacters();

            for (Pair<List<NodeValue>, List<String>> permutation : valueToRemainingCharacters) {
                Node nextNode = new Node(position + 1, permutation.left(), permutation.right(), this, completeSolutions);
                nextNode.calculateSolutions();
            }
        }
        return completeSolutions;
    }

    private List<Pair<List<NodeValue>, List<String>>> permuteRemainingCharacters() {
        int groupSizeForNextIteration = (int) Math.sqrt(remainingCharacters.size());
        Permutation permutation = new Permutation(groupSizeForNextIteration, remainingCharacters);
        return permutation.calculatePermutations();
    }

    private LinkedList<Node> calculateNodeTrail() {
        LinkedList<Node> nodeTrail = new LinkedList<>();
        nodeTrail.add(this);
        while (previous != null) {
            nodeTrail.add(previous);
            previous = previous.previous;
        }
        return nodeTrail;
    }

    public static List<String> stitchNodesTogether(List<Node> nodes, int numberOfInputCharacters) {
        // Remove start node
        nodes.removeIf(node -> node.position == -1);
        LinkedList<List<NodeValue>> wordGroups = new LinkedList<>();
        for (int i = nodes.size() - 1; i >= 0; i--) {
            if (nodes.get(i).position == 0) {
                wordGroups.add(nodes.get(i).value);
            } else {
                if (nodes.get(i).position % 2 == 0) {
                    wordGroups.addLast(nodes.get(i).value);
                } else {
                    wordGroups.addFirst(nodes.get(i).value);
                }
            }
        }
        // Use a moving window to create words
        int wordSize = ((int) Math.sqrt(numberOfInputCharacters));
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
}