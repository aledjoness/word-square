package wordsquare.domain;

import java.util.List;

public record Solution(List<String> characters) {

    public static Solution none() {
        return new Solution(List.of());
    }

    public boolean hasBeenFound() {
        return !characters.isEmpty();
    }

    public void printSolution() {
        System.out.println("Solution:");
        for (int i = 0; i < characters.size(); i++) {
            System.out.print(characters.get(i));
            if (((i + 1) % Math.sqrt(characters.size())) == 0) {
                System.out.println();
            }
        }
    }
}
