package wordsquare.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PermutationTest {

    private Permutation permutation;

    @Test
    void permutesInputOfSize1GroupSize1() {
        permutation = new Permutation(1, List.of("h"));

        assertThat(permutation.calculatePermutations()).containsExactlyInAnyOrder(
                Pair.of(List.of(new NodeValue("h")), List.of()));
    }

    @Test
    void permutesInputOfSize2GroupSize1() {
        permutation = new Permutation(1, List.of("h", "s"));

        assertThat(permutation.calculatePermutations()).containsExactlyInAnyOrder(
                Pair.of(List.of(new NodeValue("h")), List.of("s")),
                Pair.of(List.of(new NodeValue("s")), List.of("h")));
    }

    @Test
    void permutesInputOfSize6GroupSize2() {
        permutation = new Permutation(2, List.of("e", "e", "a", "a", "h", "s"));

        assertThat(permutation.calculatePermutations()).containsExactlyInAnyOrder(
                Pair.of(List.of(new NodeValue("e"), new NodeValue("e")), List.of("a", "a", "h", "s")),
                Pair.of(List.of(new NodeValue("a"), new NodeValue("a")), List.of("e", "e", "h", "s")));
    }

    @Test
    void permutesInputOfSize11GroupSize3() {
        permutation = new Permutation(3, List.of("a", "a", "e", "e", "e", "e", "e", "e", "h", "s", "u"));

        assertThat(permutation.calculatePermutations()).containsExactlyInAnyOrder(
                Pair.of(List.of(new NodeValue("a"), new NodeValue("e"), new NodeValue("a")), List.of("e", "e", "e", "e", "e", "h", "s", "u")),
                Pair.of(List.of(new NodeValue("a"), new NodeValue("h"), new NodeValue("a")), List.of("e", "e", "e", "e", "e", "e", "s", "u")),
                Pair.of(List.of(new NodeValue("a"), new NodeValue("s"), new NodeValue("a")), List.of("e", "e", "e", "e", "e", "e", "h", "u")),
                Pair.of(List.of(new NodeValue("a"), new NodeValue("u"), new NodeValue("a")), List.of("e", "e", "e", "e", "e", "e", "h", "s")),
                Pair.of(List.of(new NodeValue("e"), new NodeValue("a"), new NodeValue("e")), List.of("a", "e", "e", "e", "e", "h", "s", "u")),
                Pair.of(List.of(new NodeValue("e"), new NodeValue("e"), new NodeValue("e")), List.of("a", "a", "e", "e", "e", "h", "s", "u")),
                Pair.of(List.of(new NodeValue("e"), new NodeValue("h"), new NodeValue("e")), List.of("a", "a", "e", "e", "e", "e", "s", "u")),
                Pair.of(List.of(new NodeValue("e"), new NodeValue("s"), new NodeValue("e")), List.of("a", "a", "e", "e", "e", "e", "h", "u")),
                Pair.of(List.of(new NodeValue("e"), new NodeValue("u"), new NodeValue("e")), List.of("a", "a", "e", "e", "e", "e", "h", "s")));
    }
}
