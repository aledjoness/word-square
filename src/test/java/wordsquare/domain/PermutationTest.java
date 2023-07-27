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

    @Test
    void name() {
        permutation = new Permutation(4, List.of("a", "a", "c", "c", "d", "e", "e", "e", "e", "m", "m", "n", "n", "n", "o", "o"));
        assertThat(permutation.calculatePermutations()).containsExactlyInAnyOrder(
                Pair.of(List.of(new NodeValue("a"), new NodeValue("c"), new NodeValue("c"), new NodeValue("a")), List.of("d","e","e","e","e","m","m","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("a"), new NodeValue("e"), new NodeValue("e"), new NodeValue("a")), List.of("c","c","d","e","e","m","m","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("a"), new NodeValue("m"), new NodeValue("m"), new NodeValue("a")), List.of("c","c","d","e","e","e","e","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("a"), new NodeValue("n"), new NodeValue("n"), new NodeValue("a")), List.of("c","c","d","e","e","e","e","m","m","n","o","o")),
                Pair.of(List.of(new NodeValue("a"), new NodeValue("o"), new NodeValue("o"), new NodeValue("a")), List.of("c","c","d","e","e","e","e","m","m","n","n","n")),
                Pair.of(List.of(new NodeValue("c"), new NodeValue("a"), new NodeValue("a"), new NodeValue("c")), List.of("d","e","e","e","e","m","m","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("c"), new NodeValue("e"), new NodeValue("e"), new NodeValue("c")), List.of("a","a","d","e","e","m","m","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("c"), new NodeValue("m"), new NodeValue("m"), new NodeValue("c")), List.of("a","a","d","e","e","e","e","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("c"), new NodeValue("n"), new NodeValue("n"), new NodeValue("c")), List.of("a","a","d","e","e","e","e","m","m","n","o","o")),
                Pair.of(List.of(new NodeValue("c"), new NodeValue("o"), new NodeValue("o"), new NodeValue("c")), List.of("a","a","d","e","e","e","e","m","m","n","n","n")),
                Pair.of(List.of(new NodeValue("e"), new NodeValue("a"), new NodeValue("a"), new NodeValue("e")), List.of("c","c","d","e","e","m","m","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("e"), new NodeValue("c"), new NodeValue("c"), new NodeValue("e")), List.of("a","a","d","e","e","m","m","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("e"), new NodeValue("e"), new NodeValue("e"), new NodeValue("e")), List.of("a","a","c","c","d","m","m","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("e"), new NodeValue("m"), new NodeValue("m"), new NodeValue("e")), List.of("a","a","c","c","d","e","e","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("e"), new NodeValue("n"), new NodeValue("n"), new NodeValue("e")), List.of("a","a","c","c","d","e","e","m","m","n","o","o")),
                Pair.of(List.of(new NodeValue("e"), new NodeValue("o"), new NodeValue("o"), new NodeValue("e")), List.of("a","a","c","c","d","e","e","m","m","n","n","n")),
                Pair.of(List.of(new NodeValue("m"), new NodeValue("a"), new NodeValue("a"), new NodeValue("m")), List.of("c","c","d","e","e","e","e","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("m"), new NodeValue("c"), new NodeValue("c"), new NodeValue("m")), List.of("a","a","d","e","e","e","e","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("m"), new NodeValue("e"), new NodeValue("e"), new NodeValue("m")), List.of("a","a","c","c","d","e","e","n","n","n","o","o")),
                Pair.of(List.of(new NodeValue("m"), new NodeValue("n"), new NodeValue("n"), new NodeValue("m")), List.of("a","a","c","c","d","e","e","e","e","n","o","o")),
                Pair.of(List.of(new NodeValue("m"), new NodeValue("o"), new NodeValue("o"), new NodeValue("m")), List.of("a","a","c","c","d","e","e","e","e","n","n","n")),
                Pair.of(List.of(new NodeValue("n"), new NodeValue("a"), new NodeValue("a"), new NodeValue("n")), List.of("c","c","d","e","e","e","e","m","m","n","o","o")),
                Pair.of(List.of(new NodeValue("n"), new NodeValue("c"), new NodeValue("c"), new NodeValue("n")), List.of("a","a","d","e","e","e","e","m","m","n","o","o")),
                Pair.of(List.of(new NodeValue("n"), new NodeValue("e"), new NodeValue("e"), new NodeValue("n")), List.of("a","a","c","c","d","e","e","m","m","n","o","o")),
                Pair.of(List.of(new NodeValue("n"), new NodeValue("m"), new NodeValue("m"), new NodeValue("n")), List.of("a","a","c","c","d","e","e","e","e","n","o","o")),
                Pair.of(List.of(new NodeValue("n"), new NodeValue("o"), new NodeValue("o"), new NodeValue("n")), List.of("a","a","c","c","d","e","e","e","e","m","m","n")),
                Pair.of(List.of(new NodeValue("o"), new NodeValue("n"), new NodeValue("n"), new NodeValue("o")), List.of("a","a","c","c","d","e","e","e","e","m","m","n")),
                Pair.of(List.of(new NodeValue("o"), new NodeValue("a"), new NodeValue("a"), new NodeValue("o")), List.of("c","c","d","e","e","e","e","m","m","n","n","n")),
                Pair.of(List.of(new NodeValue("o"), new NodeValue("c"), new NodeValue("c"), new NodeValue("o")), List.of("a","a","d","e","e","e","e","m","m","n","n","n")),
                Pair.of(List.of(new NodeValue("o"), new NodeValue("e"), new NodeValue("e"), new NodeValue("o")), List.of("a","a","c","c","d","e","e","m","m","n","n","n")),
                Pair.of(List.of(new NodeValue("o"), new NodeValue("m"), new NodeValue("m"), new NodeValue("o")), List.of("a","a","c","c","d","e","e","e","e","n","n","n")));
    }
}
