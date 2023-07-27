package wordsquare.domain;

import java.util.List;

public record Solution(List<String> characters) {

    public static Solution none() {
        return new Solution(List.of());
    }

    public boolean hasBeenFound() {
        return !characters.isEmpty();
    }

    public void print() {
        System.out.println("Solution: ");
        characters.forEach(System.out::println);
    }
}
