package wordsquare.domain;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import wordsquare.FileHelper;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class NodeTest {

    @Test
    void initialisesStartNode() {
        Node node = Node.initialiseNode(List.of("a", "a", "b", "b", "c", "c", "f", "g", "h"));
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
    void calculatesViableSolutions() {
        Node startNode = new Node(0, List.of(new NodeValue("a"), new NodeValue("a")), List.of("b", "c"), null);
        Dictionary dictionary = Mockito.mock(Dictionary.class);
        Mockito.when(dictionary.areWords(any())).thenReturn(true);

        List<LinkedList<Node>> solutions = new LinkedList<>();
        startNode.calculateViableSolutions(solutions, 4, dictionary);

        List<Node> expected1 = List.of(new Node(1,
                                                List.of(new NodeValue("b")),
                                                List.of("c"),
                                                null),
                                       new Node(0,
                                                List.of(new NodeValue("a"), new NodeValue("a")),
                                                List.of("b", "c"),
                                                null));
        LinkedList<Node> nodes1 = new LinkedList<>(expected1);
        List<Node> expected2 = List.of(new Node(1,
                                                List.of(new NodeValue("c")),
                                                List.of("b"),
                                                null),
                                       new Node(0,
                                                List.of(new NodeValue("a"), new NodeValue("a")),
                                                List.of("b", "c"),
                                                null));
        LinkedList<Node> nodes2 = new LinkedList<>(expected2);
        assertThat(solutions).usingRecursiveFieldByFieldElementComparatorIgnoringFields("previous").containsExactly(
                nodes1,
                nodes2);
    }

    @Test
    void calculatesBottomHalfViableSolutions() {
        Node firstNode = new Node(0, List.of(new NodeValue("t"), new NodeValue("t")), List.of("o", "i"), null);

        Dictionary dictionary = Mockito.mock(Dictionary.class);
        Mockito.when(dictionary.areWords(List.of("ot", "ti"))).thenReturn(false);
        Mockito.when(dictionary.areWords(List.of("it", "to"))).thenReturn(true);

        List<List<String>> viableSolutions = new LinkedList<>();
        firstNode.calculateBottomHalfViableSolutions(viableSolutions, 4, dictionary);

        assertThat(viableSolutions).containsExactly(List.of("it", "to"));
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
                                            null));

        // E.g. stitching i t into words (it, to)
        //                t o
        List<String> stitchedTogether = Node.stitchNodesTogether(new LinkedList<>(nodes), 4);

        assertThat(stitchedTogether).containsExactly("it", "to");
    }
}
