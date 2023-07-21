package wordsquare;

import wordsquare.domain.Dictionary;

public class Application {

    public static void main(String[] args) {
        Dictionary dictionary = Dictionary.populate();
        System.out.println("Hello World!");
    }
}
