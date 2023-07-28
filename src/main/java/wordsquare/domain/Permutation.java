package wordsquare.domain;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

public class Permutation {

    private final int groupSize;
    private final List<String> remainingCharacters;

    public Permutation(int groupSize, List<String> remainingCharacters) {
        this.groupSize = groupSize;
        this.remainingCharacters = remainingCharacters;
    }

    public List<Pair<List<NodeValue>, List<String>>> calculatePermutations2() {
        List<Pair<List<NodeValue>, List<String>>> result = new LinkedList<>();
        if (groupSize == 1) {
            if (remainingCharacters.size() == 1) {
                result.add(Pair.of(List.of(new NodeValue(remainingCharacters.get(0))), new LinkedList<>()));
            } else {
                for (String remainingChar: remainingCharacters) {
                    List<String> remainingCharsMinusCurrentChar = new LinkedList<>(remainingCharacters);
                    remainingCharsMinusCurrentChar.remove(remainingChar);
                    result.add(Pair.of(List.of(new NodeValue(remainingChar)), remainingCharsMinusCurrentChar));
                }
            }
            return result;
        }

        String inputMinusDuplicatesAndSingles = String.join("", halveDuplicateValuesAndRemoveSingleCharacters(remainingCharacters));
        List<String> permutations = new ArrayList<>();
        findPermutations(0, inputMinusDuplicatesAndSingles, groupSize, remainingCharacters, permutations);

        // todo: dupe list
        List<Pair<List<NodeValue>, List<String>>> permutationsToRemainingCharacters = new LinkedList<>();
        convertToNodeValues(remainingCharacters, permutations, permutationsToRemainingCharacters);

        return permutationsToRemainingCharacters;
    }

    // Returns PossibleValues : RemainingValues
    // todo: change return type to something more readable such a Permutations
    public List<Pair<List<NodeValue>, List<String>>> calculatePermutations() {
        List<Pair<List<NodeValue>, List<String>>> result = new LinkedList<>();
        if (groupSize == 1) {
            if (remainingCharacters.size() == 1) {
                result.add(Pair.of(List.of(new NodeValue(remainingCharacters.get(0))), new LinkedList<>()));
            } else {
                // Total remaining characters size = 2
                result.add(Pair.of(List.of(new NodeValue(remainingCharacters.get(0))), new LinkedList<>(singletonList(remainingCharacters.get(1)))));
                result.add(Pair.of(List.of(new NodeValue(remainingCharacters.get(1))), new LinkedList<>(singletonList(remainingCharacters.get(0)))));
            }
            return result;
        }

        String inputMinusDuplicatesAndSingles = String.join("", halveDuplicateValuesAndRemoveSingleCharacters(remainingCharacters));
        List<String> permutations = new ArrayList<>();
        findPermutations(0, inputMinusDuplicatesAndSingles, groupSize, remainingCharacters, permutations);

        List<Pair<List<NodeValue>, List<String>>> permutationsToRemainingCharacters = new LinkedList<>();
        convertToNodeValues(remainingCharacters, permutations, permutationsToRemainingCharacters);

        return permutationsToRemainingCharacters;
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

    private static void findSubstringsForEven(String currentPermutation, int halfGroupSize, List<String> result) {
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

    private static void findSubstringsForOdd(String currentPermutation, int halfGroupSize, List<String> originalInput, List<String> result) {
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

        for (String character : permutationAsList) {
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

    private static void findPermutations(int index, String str, int groupSize, List<String> originalInput, List<String> result) {
        // Base case
        if (index == (str.length() - 1)) {
            if (groupSize % 2 == 0) {
                findSubstringsForEven(str, groupSize / 2, result);
            } else {
                findSubstringsForOdd(str, groupSize / 2, originalInput, result);
            }
            return;
        }

        char prev = '*';

        // Loop from j = 1 to length of String
        for (int j = index; j < str.length(); j++) {
            char[] temp = str.toCharArray();
            if (j > index && temp[index] == temp[j]) {
                continue;
            }
            if (prev != '*' && prev == str.charAt(j)) {
                continue;
            }

            // Swap the elements
            swap2(temp, index, j);
            prev = str.charAt(j);

            // Recursion call
            findPermutations(index + 1, String.valueOf(temp), groupSize, originalInput, result);
        }
    }

    private static void swap2(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

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
}
