package wordsquare.domain;

import org.junit.jupiter.api.Test;
import wordsquare.FileHelper;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NodeTest {

    @Test
    void initialisesStartNode() {
        Node node = Node.startNode(List.of("a", "a", "b", "b", "c", "c", "f", "g", "h"));
        int numOfLinesForStartNode = node.initialise();

        assertThat(numOfLinesForStartNode).isEqualTo(15);
        List<Pair<List<NodeValue>, List<String>>> startNodes = FileHelper.readFile();

        assertThat(startNodes).containsExactlyInAnyOrder(
                Pair.of(List.of(new NodeValue("a"), new NodeValue("b"), new NodeValue("a")), List.of("b", "c", "c", "f", "g", "h")),
                Pair.of(List.of(new NodeValue("a"), new NodeValue("c"), new NodeValue("a")), List.of("b", "b", "c", "f", "g", "h")),
                Pair.of(List.of(new NodeValue("a"), new NodeValue("f"), new NodeValue("a")), List.of("b", "b", "c", "c", "g", "h")),
                Pair.of(List.of(new NodeValue("a"), new NodeValue("g"), new NodeValue("a")), List.of("b", "b", "c", "c", "f", "h")),
                Pair.of(List.of(new NodeValue("a"), new NodeValue("h"), new NodeValue("a")), List.of("b", "b", "c", "c", "f", "g")),
                Pair.of(List.of(new NodeValue("b"), new NodeValue("a"), new NodeValue("b")), List.of("a", "c", "c", "f", "g", "h")),
                Pair.of(List.of(new NodeValue("b"), new NodeValue("c"), new NodeValue("b")), List.of("a", "a", "c", "f", "g", "h")),
                Pair.of(List.of(new NodeValue("b"), new NodeValue("f"), new NodeValue("b")), List.of("a", "a", "c", "c", "g", "h")),
                Pair.of(List.of(new NodeValue("b"), new NodeValue("g"), new NodeValue("b")), List.of("a", "a", "c", "c", "f", "h")),
                Pair.of(List.of(new NodeValue("b"), new NodeValue("h"), new NodeValue("b")), List.of("a", "a", "c", "c", "f", "g")),
                Pair.of(List.of(new NodeValue("c"), new NodeValue("a"), new NodeValue("c")), List.of("a", "b", "b", "f", "g", "h")),
                Pair.of(List.of(new NodeValue("c"), new NodeValue("b"), new NodeValue("c")), List.of("a", "a", "b", "f", "g", "h")),
                Pair.of(List.of(new NodeValue("c"), new NodeValue("f"), new NodeValue("c")), List.of("a", "a", "b", "b", "g", "h")),
                Pair.of(List.of(new NodeValue("c"), new NodeValue("g"), new NodeValue("c")), List.of("a", "a", "b", "b", "f", "h")),
                Pair.of(List.of(new NodeValue("c"), new NodeValue("h"), new NodeValue("c")), List.of("a", "a", "b", "b", "f", "g")));
    }

    @Test
    void calculatesSolutions() {
        Node node = new Node(0, List.of(new NodeValue("a"), new NodeValue("a")),
                             List.of("b", "c"),
                             Node.startNode(List.of("a", "a", "b", "c")));

        List<LinkedList<Node>> solutions = new LinkedList<>();
        node.calculateSolutions(solutions);

        List<Node> expected1 = List.of(new Node(2,
                                                List.of(new NodeValue("c")),
                                                List.of(),
                                                null),
                                       new Node(1,
                                                List.of(new NodeValue("b")),
                                                List.of("c"),
                                                null),
                                       new Node(0,
                                                List.of(new NodeValue("a"), new NodeValue("a")),
                                                List.of("b", "c"),
                                                null),
                                       new Node(-1,
                                                List.of(),
                                                List.of("a", "a", "b", "c"),
                                                null));
        LinkedList<Node> nodes1 = new LinkedList<>(expected1);
        List<Node> expected2 = List.of(new Node(2,
                                                List.of(new NodeValue("b")),
                                                List.of(),
                                                null),
                                       new Node(1,
                                                List.of(new NodeValue("c")),
                                                List.of("b"),
                                                null),
                                       new Node(0,
                                                List.of(new NodeValue("a"), new NodeValue("a")),
                                                List.of("b", "c"),
                                                null),
                                       new Node(-1,
                                                List.of(),
                                                List.of("a", "a", "b", "c"),
                                                null));
        LinkedList<Node> nodes2 = new LinkedList<>(expected2);
        assertThat(solutions).usingRecursiveFieldByFieldElementComparatorIgnoringFields("previous").containsExactly(
                nodes1,
                nodes2);
    }

    @Test
    void stitchesNodesTogether() {
        List<Node> nodes = List.of(new Node(2,
                                            List.of(new NodeValue("o")),
                                            List.of(),
                                            null),
                                   new Node(1,
                                            List.of(new NodeValue("i")),
                                            List.of("o"),
                                            null),
                                   new Node(0,
                                            List.of(new NodeValue("t"), new NodeValue("t")),
                                            List.of("i", "o"),
                                            null),
                                   new Node(-1,
                                            List.of(),
                                            List.of("i", "o", "t", "t"),
                                            null));

        // E.g. stitching i t into words (it, to)
        //                t o
        List<String> stichedTogether = Node.stitchNodesTogether(new LinkedList<>(nodes), 4);

        assertThat(stichedTogether).containsExactly("it", "to");
    }
}
