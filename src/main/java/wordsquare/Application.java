package wordsquare;

import wordsquare.domain.Dictionary;
import wordsquare.domain.Solution;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter size of WordSquare: ");
        int size = scanner.nextInt();
        System.out.printf("Enter %d characters to populate grid: ", size * size);
        String characters = scanner.next();
        scanner.close();
        WordSquare wordSquare = new WordSquare(size, characters, Dictionary.populate(size));
        if (wordSquare.isNotValid()) {
            System.err.println("[ERROR] WordSquare must be greater than size 1 and be populated by exactly size*size characters");
            System.exit(1);
        }

        Solution solution = wordSquare.solve();

        if (solution.hasBeenFound()) {
            solution.printSolution();
        } else {
            System.out.printf("There is no solution for size %d and input %s%n", size, characters);
        }
    }
}
