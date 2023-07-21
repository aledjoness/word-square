package wordsquare;

import wordsquare.domain.Dictionary;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        Dictionary dictionary = Dictionary.populate();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter size of WordSquare: ");
        int size = scanner.nextInt();
        System.out.printf("Enter %d characters to populate grid: ", size * size);
        String characters = scanner.next();
        scanner.close();
        WordSquare wordSquare = new WordSquare(size, characters);
        if (wordSquare.isNotValid()) {
            System.err.printf("[ERROR] WordSquare must be populated by exactly %d characters%n", size * size);
        }

    }
}
