package wordsquare.domain;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

public class Permutation {

    private final int groupSize;
    private final List<String> remainingCharacters;
    private Map<String, Long> characterToOccurrence;

    public Permutation(int groupSize, List<String> remainingCharacters) {
        this.groupSize = groupSize;
        this.remainingCharacters = remainingCharacters;
    }

    // Returns PossibleValues : RemainingValues
    // todo: change return type to something more readable
    public List<Pair<List<NodeValue>, List<String>>> calculatePermutations() {
        List<Pair<List<NodeValue>, List<String>>> result = new LinkedList<>();
        if (groupSize == 1) {
            if (remainingCharacters.size() == 1) {
                result.add(Pair.of(List.of(new NodeValue(remainingCharacters.get(0))), new LinkedList<>()));
            } else {
                // Group size = 2
                result.add(Pair.of(List.of(new NodeValue(remainingCharacters.get(0))), new LinkedList<>(singletonList(remainingCharacters.get(1)))));
                result.add(Pair.of(List.of(new NodeValue(remainingCharacters.get(1))), new LinkedList<>(singletonList(remainingCharacters.get(0)))));
            }
            return result;
        }
        // Must start and end with same letter
        characterToOccurrence = remainingCharacters.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<String> startingCharacters = characterToOccurrence.entrySet().stream().filter(entry -> entry.getValue() > 1).map(Map.Entry::getKey).toList();

        // todo: one starting character at a time, work out all solutions, check if they're words or not
        List<Pair<List<NodeValue>, List<String>>> allPermutations = new LinkedList<>();
        for (String startingCharacter : startingCharacters) {
            allPermutations.addAll(findAllPermutations(startingCharacter));
        }
        return allPermutations;
    }

    private boolean isAcceptable(String currentPermutation, String startingCharacter) {
        // Either we have groupSize == 1 (because groupSize is initially 3), so one character will always be acceptable
        if (groupSize - 2 == 1) {
            return currentPermutation.length() == 1;
        }
        if (groupSize == 2) {
            return currentPermutation.startsWith(startingCharacter)
                    && currentPermutation.endsWith(startingCharacter);
        }
        return currentPermutation.length() == groupSize - 2 // Have removed the start and end characters
                && currentPermutation.startsWith(startingCharacter)
                && currentPermutation.endsWith(startingCharacter)
                && patternIsAcceptableForGroupSize(currentPermutation);
    }

    private boolean patternIsAcceptableForGroupSize(String currentPermutation) {
        if (groupSize % 2 == 0) {
            // Should be reflective
            String firstHalf = currentPermutation.substring(0, currentPermutation.length() / 2 - 1);
            String secondHalf = currentPermutation.substring(currentPermutation.length() / 2 - 1);
            return firstHalf.equals(secondHalf);
        }
        // Should be reflective with the exception of the letter in the middle
        String firstHalf = currentPermutation.substring(0, currentPermutation.length() / 2);
        String secondHalf = currentPermutation.substring(currentPermutation.length() / 2 + 1);
        return firstHalf.equals(secondHalf);
    }

    private List<Pair<List<NodeValue>, List<String>>> findAllPermutations(String startingCharacter) {
        List<String> totalRemainingLetters = new LinkedList<>(remainingCharacters);
        Collections.copy(totalRemainingLetters, remainingCharacters);

        totalRemainingLetters.remove(startingCharacter);
        totalRemainingLetters.remove(startingCharacter);

        List<Pair<List<NodeValue>, List<String>>> realResult = new LinkedList<>();
        if (groupSize == 2) {
            List<NodeValue> nodeValues = new LinkedList<>();
            nodeValues.add(new NodeValue(startingCharacter));
            nodeValues.add(new NodeValue(startingCharacter));

            realResult.add(Pair.of(nodeValues, new LinkedList<>(totalRemainingLetters)));
            return realResult;
        }

        List<String> result = new ArrayList<>();
        Stream.of(String.join("", totalRemainingLetters))
                .flatMap(currentPermutation -> startPermutation(currentPermutation).stream())
                .flatMap(currentPermutation -> findSubstrings(currentPermutation).stream())
                .distinct()
                .filter(currentPermutation -> this.isAcceptable(currentPermutation, startingCharacter))
                .forEach(result::add);

        // Need to bookend result with starting characters
        for (String permutation : result) {
            // Add node values
            List<NodeValue> nodeValues = new LinkedList<>();
            nodeValues.add(new NodeValue(startingCharacter));
            List<String> permutationAsList = Arrays.stream(permutation.split("")).toList();
            nodeValues.addAll(NodeValue.toNodeValues(permutationAsList));
            nodeValues.add(new NodeValue(startingCharacter));

            // Add remaining chars
            List<String> remainingCharsForThisPermutation = new LinkedList<>(totalRemainingLetters);
            for (String character : permutationAsList) {
                remainingCharsForThisPermutation.remove(character);
            }
            realResult.add(Pair.of(nodeValues, remainingCharsForThisPermutation));
        }
        return realResult;
    }

    public static List<String> startPermutation(final String currentPermutation) {
        return permute("", currentPermutation);
    }

    // todo: limit amount of permutations
    private static List<String> permute(final String prefix, final String currentPermutation) {
        final List<String> permutations = new ArrayList<>();

        final int n = currentPermutation.length();
        if (n == 0) {
            permutations.add(prefix);
        } else {
            for (int i = 0; i < n; i++) {
                permutations.addAll(permute(prefix + currentPermutation.charAt(i),
                                            currentPermutation.substring(0, i) + currentPermutation.substring(i + 1, n)));
            }
        }
        return permutations;
    }

    public static List<String> findSubstrings(String currentPermutation) {
        List<String> allSubstrings = new ArrayList<>();
        for (int i = 0; i < currentPermutation.length(); i++) {
            for (int j = 1; j <= currentPermutation.length() - i; j++) {
                allSubstrings.add(currentPermutation.substring(i, i + j));
            }
        }
        return allSubstrings;
    }
}
