package wordsquare.domain;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    // todo: Do we still need this?
    public static void findSubstringsForEven(String currentPermutation, int halfGroupSize, List<String> result) {
        for (int i = 0; i < currentPermutation.length(); i++) {
            for (int j = 1; j <= currentPermutation.length() - i; j++) {
                String substring = currentPermutation.substring(i, i + j);
                if (substring.length() == halfGroupSize) {
                    String completedGroup = completeRestOfGroupBySymmetry(substring);

                    if (!result.contains(completedGroup)) {
                        result.add(completedGroup);
                    }
                }
            }
        }
    }

    public static void findSubstringsForOdd(String currentPermutation, int halfGroupSize, List<String> originalInput, List<String> result) {
        for (int i = 0; i < currentPermutation.length(); i++) {
            for (int j = 1; j <= currentPermutation.length() - i; j++) {
                String substring = currentPermutation.substring(i, i + j);
                if (substring.length() == halfGroupSize) {
                    // If odd then we have a symmetric group until the midpoint, now we just need to add all remaining chars
                    List<List<String>> possiblePermutationsFromCurrentSymmetry = findAllPossibleMiddleValues(substring, originalInput);

                    for (List<String> possiblePermutation : possiblePermutationsFromCurrentSymmetry) {
                        String permutationAsString = String.join("", possiblePermutation);

                        if (!result.contains(permutationAsString)) {
                            result.add(permutationAsString);
                        }
                    }
                }
            }
        }
    }

    private static List<List<String>> findAllPossibleMiddleValues(String currentPermutation, List<String> originalInput) {
        List<String> remainingCharactersToUse = new ArrayList<>(originalInput);

        String mirroredCurrentPermutation = completeRestOfGroupBySymmetry(currentPermutation);
        List<String> permutationAsList = Arrays.stream(mirroredCurrentPermutation.split("")).collect(Collectors.toList());

        // Because currentPermutation is half a symmetric representation of what the actual group is, we need to remove
        // from originalInput twice
        for (String character : permutationAsList) {
            remainingCharactersToUse.remove(character);
            remainingCharactersToUse.remove(character);
        }

        // Make a template and add all possible characters to middle of permutation e.g. abXba
        int middle = permutationAsList.size() / 2;
        permutationAsList.add(middle, "[]"); // Placeholder

        List<List<String>> possiblePermutations = new LinkedList<>();
        for (String remainingCharacter : remainingCharactersToUse) {
            permutationAsList.remove(middle);
            permutationAsList.add(middle, remainingCharacter);

            if (!possiblePermutations.contains(permutationAsList)) {
                possiblePermutations.add(new ArrayList<>(permutationAsList));
            }
        }
        return possiblePermutations;
    }

    private static String completeRestOfGroupBySymmetry(String substring) {
        // abc in an even group becomes abccba
        return substring + new StringBuilder(substring).reverse();
    }

    static boolean shouldSwap(char[] str, int start, int curr) {
        for (int i = start; i < curr; i++) {
            if (str[i] == str[curr]) {
                return false;
            }
        }
        return true;
    }

    static void findPermutations(char[] str, int groupSize, List<String> originalInput, int index, int n, List<String> result) {
        if (index >= n) {
            if (groupSize % 2 == 0) {
                findSubstringsForEven(new String(str), groupSize / 2, result);
            } else {
                findSubstringsForOdd(new String(str), groupSize / 2, originalInput, result);
            }
            return;
        }

        for (int i = index; i < n; i++) {

            // Proceed further for str[i] only if it
            // doesn't match with any of the characters
            // after str[index]
            boolean check = shouldSwap(str, index, i);
            if (check) {
                swap(str, index, i);
                findPermutations(str, groupSize, originalInput, index + 1, n, result);
                swap(str, index, i);
            }
        }
    }

    static void swap(char[] str, int i, int j) {
        char c = str[i];
        str[i] = str[j];
        str[j] = c;
    }

    // return halved values : unused
    private List<String> halveDuplicateValuesAndRemoveSingleCharacters(List<String> input) {
        List<String> halvedDuplicateValues = new ArrayList<>();
        Map<String, Long> charToCount = input.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        for (Map.Entry<String, Long> entry : charToCount.entrySet()) {
            if (entry.getValue() % 2 == 0) {
                int occurrencesHalved = entry.getValue().intValue() / 2;
                for (int j = 0; j < occurrencesHalved; j++) {
                    halvedDuplicateValues.add(entry.getKey());
                }
            } else {
                if (entry.getValue() > 2) {
                    int i = entry.getValue().intValue();
                    i--;
                    int occurrencesHalved = i / 2;
                    for (int j = 0; j < occurrencesHalved; j++) {
                        halvedDuplicateValues.add(entry.getKey());
                    }
                }
            }
        }
        return halvedDuplicateValues;
    }

    @Test
    void name() {
        List<String> input = List.of("a", "a", "a", "e", "e", "e", "e", "f", "h", "h", "m", "o", "o", "n", "s", "s", "r", "r", "r", "r", "t", "t", "t", "t", "w");
//        List<String> input = List.of("a", "a", "c", "c", "d", "e", "e", "e", "e", "m", "m", "n", "n", "o", "o");
//        List<String> input = List.of("a", "a", "c", "c", "d", "e", "e", "e", "f");
        String join = String.join("", halveDuplicateValuesAndRemoveSingleCharacters(input));

        char[] chars = join.toCharArray();
        List<String> result = new ArrayList<>();
        findPermutations(chars, 5, input, 0, chars.length, result);

        List<Pair<List<NodeValue>, List<String>>> realResult = new LinkedList<>();
        convertToNodeValues(input, result, realResult);
        int i = 0;
    }

    @Test
    void test() {
        List<String> input = List.of("a", "a", "c", "c", "d", "e", "e", "e", "e", "m", "m", "n", "n", "o", "o");
        String inpoot = halveDuplicateValuesAndRemoveSingleCharacters(input).toString();

        String trim = inpoot.replace(",", "")  //remove the commas
                .replace(" ", "")  //remove the right bracket
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();

        char[] chars = trim.toCharArray();

//        char[] str = {'e', 'e', 'e', 'e', 'e', 'e', 'h', 's', 'u'};
//        List<String> aacceeeemmnnnoo = List.of("aacceeeemmnnnoo");
//        char[] input = aacceeeemmnnnoo.toString().toCharArray();
//        char[] str = {'a', 'a', 'c', 'c', 'd', 'e', 'e', 'e', 'e', 'm', 'm', 'n', 'n', 'n', 'o', 'o'};
//        char[] str = {'a', 'a', 'e', 'e', 'b', 'b', 'c', 'e', 'c', 'x', 'y', 'y', 'y', 'y', 'y', 'y'};
//        char[] str = {'a', 'a', 'e', 'e', 'e', 'e', 'e', 'e', 'f', 'f', 'g', 'g'};
        List<String> result = new ArrayList<>();
        findPermutations(chars, 2, input, 0, chars.length, result);

        List<Pair<List<NodeValue>, List<String>>> realResult = new LinkedList<>();
        convertToNodeValues(input, result, realResult);
        int i = 0;
    }

    private void convertToNodeValues(List<String> input, List<String> result, List<Pair<List<NodeValue>, List<String>>> realResult) {
        for (String permutation : result) {
            // Add node values
            List<String> permutationAsList = Arrays.stream(permutation.split("")).toList();
            List<NodeValue> nodeValues = new LinkedList<>(NodeValue.toNodeValues(permutationAsList));

            // Add remaining chars
            List<String> remainingCharsForThisPermutation = new LinkedList<>(input);
            for (String character : permutationAsList) {
                remainingCharsForThisPermutation.remove(character);
            }
            realResult.add(Pair.of(nodeValues, remainingCharsForThisPermutation));
        }
    }

    private static boolean isAcceptable(String currentPermutation, List<String> startingCharacters, int groupSize) {
        // Either we have groupSize == 1 (because groupSize is initially 3), so one character will always be acceptable
        if (groupSize - 2 == 1) {
            return currentPermutation.length() == 1;
        }
//        if (groupSize == 2) {
//            return startsAndEndsWith(currentPermutation, startingCharacters);
//        }
        return currentPermutation.length() == groupSize // Have removed the start and end characters
//                && startsAndEndsWith(currentPermutation, startingCharacters)
                && patternIsAcceptableForGroupSize(currentPermutation, groupSize);
    }

    private static boolean startsAndEndsWith(String currentPermutation, List<String> startingCharacters) {
        if (currentPermutation.substring(0, 1).equals(currentPermutation.substring(currentPermutation.length() - 1))) {
            if (startingCharacters.contains(currentPermutation.substring(0, 1))) {
                return true;
            }
        }
        return false;
    }

    private static boolean patternIsAcceptableForGroupSize(String currentPermutation, int groupSize) {
        if (groupSize % 2 == 0) {
            // Should be reflective
            String firstHalf = currentPermutation.substring(0, currentPermutation.length() / 2);
            String secondHalf = currentPermutation.substring(currentPermutation.length() / 2);
            return firstHalf.equals(new StringBuilder(secondHalf).reverse().toString());
        }
        // Should be reflective with the exception of the letter in the middle
        String firstHalf = currentPermutation.substring(0, currentPermutation.length() / 2);
        String secondHalf = currentPermutation.substring(currentPermutation.length() / 2 + 1);
        return firstHalf.equals(secondHalf);
    }

//    private static boolean isAcceptable(String currentPermutation, List<String> startingCharacters, int groupSize) {
//        // Either we have groupSize == 1 (because groupSize is initially 3), so one character will always be acceptable
//        if (groupSize - 2 == 1) {
//            return currentPermutation.length() == 1;
//        }
//        if (groupSize == 2) {
//            return startsAndEndsWith(currentPermutation, startingCharacters);
//        }
//        return currentPermutation.length() == groupSize // Have removed the start and end characters
//                && startsAndEndsWith(currentPermutation, startingCharacters)
//                && patternIsAcceptableForGroupSize(currentPermutation, groupSize);
//    }
//
//    private static boolean startsAndEndsWith(String currentPermutation, List<String> startingCharacters) {
//        if (currentPermutation.substring(0, 1).equals(currentPermutation.substring(currentPermutation.length()-1))) {
//            if (startingCharacters.contains(currentPermutation.substring(0, 1))){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private static boolean patternIsAcceptableForGroupSize(String currentPermutation, int groupSize) {
//        if (groupSize % 2 == 0) {
//            // Should be reflective
//            String firstHalf = currentPermutation.substring(0, currentPermutation.length() / 2);
//            String secondHalf = currentPermutation.substring(currentPermutation.length() / 2);
//            return firstHalf.equals(new StringBuilder(secondHalf).reverse().toString());
//        }
//        // Should be reflective with the exception of the letter in the middle
//        String firstHalf = currentPermutation.substring(0, currentPermutation.length() / 2);
//        String secondHalf = currentPermutation.substring(currentPermutation.length() / 2 + 1);
//        return firstHalf.equals(secondHalf);
//    }

}
