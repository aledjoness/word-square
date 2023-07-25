package wordsquare.domain;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        List<Pair<List<NodeValue>, List<String>>> allPermutations = new LinkedList<>();
        for (String startingCharacter : startingCharacters) {
            allPermutations.addAll(findAllPermutations(startingCharacter));
        }
        return allPermutations;
    }

    private List<Pair<List<NodeValue>, List<String>>> findAllPermutations(String startingCharacter) {
        List<String> totalRemainingLetters = new LinkedList<>(remainingCharacters);
        Collections.copy(totalRemainingLetters, remainingCharacters);
        totalRemainingLetters.remove(startingCharacter);
        totalRemainingLetters.remove(startingCharacter);

        LinkedList<String> remainingLettersForThisIteration = new LinkedList<>(totalRemainingLetters);
        Collections.copy(remainingLettersForThisIteration, totalRemainingLetters);
        List<Pair<List<NodeValue>, List<String>>> result = new LinkedList<>();
        while (!remainingLettersForThisIteration.isEmpty()) {
            LinkedList<NodeValue> currentPermutationFront = new LinkedList<>();
            LinkedList<NodeValue> currentPermutationBack = new LinkedList<>();

            currentPermutationFront.add(new NodeValue(startingCharacter));
            currentPermutationBack.add(new NodeValue(startingCharacter));
            while (currentPermutationSizeIsLessThanHalfTheGroupSize(currentPermutationFront.size())) {
                String nextCharacterToConsider = remainingLettersForThisIteration.getFirst();

                if (isADuplicate(nextCharacterToConsider)) {
                    currentPermutationFront.add(new NodeValue(nextCharacterToConsider));
                    remainingLettersForThisIteration.remove(nextCharacterToConsider);
                    if (currentPermutationFront.size() > groupSize / 2) {
                        break;
                    }
                    currentPermutationBack.add(new NodeValue(nextCharacterToConsider));
                    remainingLettersForThisIteration.remove(nextCharacterToConsider);
                } else {
                    if (nextIndexPermitsNonDuplicateCharacters(currentPermutationFront.size())) {
                        currentPermutationFront.add(new NodeValue(nextCharacterToConsider));
                        remainingLettersForThisIteration.remove(nextCharacterToConsider);
                    } else {
                        // todo: push non dupe letter to back
                    }
                }
            }
            // CurrentPermutation is half a mirror of the full result
            List<String> lettersRemainingForThisIteration = new LinkedList<>(remainingCharacters);
            Collections.copy(lettersRemainingForThisIteration, remainingCharacters);
            List<NodeValue> combinedCurrentPermutation = addPermutationsTogether(currentPermutationFront, currentPermutationBack);
            removeCombinedCurrentPermutationElementsFromPossibleRemainingCharacters(combinedCurrentPermutation, lettersRemainingForThisIteration);

            addToResult(Pair.of(combinedCurrentPermutation, lettersRemainingForThisIteration), result);
            if (currentPermutationFront.size() == 2) {
                // Then it only contains starting character (at start and end) and cannot add anymore
                break;
            }
        }
        return result;
    }

    private boolean currentPermutationSizeIsLessThanHalfTheGroupSize(int currentPermutationSize) {
        if (groupSize % 2 == 0) {
            return currentPermutationSize < (groupSize / 2);
        }
        return currentPermutationSize <= (groupSize / 2);
    }

    private void removeCombinedCurrentPermutationElementsFromPossibleRemainingCharacters(List<NodeValue> combinedCurrentPermutation, List<String> lettersRemainingForThisIteration) {
        for (NodeValue permutation : combinedCurrentPermutation) {
            lettersRemainingForThisIteration.remove(permutation.getValue());
        }
    }

    private void addToResult(Pair<List<NodeValue>, List<String>> currentPermutation, List<Pair<List<NodeValue>, List<String>>> result) {
        if (!result.contains(currentPermutation)) {
            result.add(currentPermutation);
        }
    }

    private List<NodeValue> addPermutationsTogether(List<NodeValue> currentPermutationFront, List<NodeValue> currentPermutationBack) {
        currentPermutationFront.addAll(currentPermutationBack);
        return currentPermutationFront;
    }

    private boolean nextIndexPermitsNonDuplicateCharacters(int nextIndex) {
        return (groupSize % 2 != 0) && (nextIndex >= groupSize / 2); // possibly >
    }

    private boolean isADuplicate(String nextCharacterToConsider) {
        return characterToOccurrence.get(nextCharacterToConsider) > 1;
    }
}
