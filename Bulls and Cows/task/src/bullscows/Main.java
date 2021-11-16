package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        play();
    }

    private static void play() {
        Scanner scanner = new Scanner(System.in);

        // Ввести с клаиватуры длинну секретного кода
        System.out.println("Please, enter the secret code's length:");
        int lengthOfCode;
        String strLengthOfCode = "";
        try {
            strLengthOfCode = scanner.next();
            lengthOfCode = Integer.parseInt(strLengthOfCode);
        } catch (NumberFormatException e) {
            System.out.printf("Error: \"%s\" isn't a valid number.", strLengthOfCode);
            return;
        }

        // Ввести количество возможных символов
        System.out.println("Input the number of possible symbols in the code:");
        int possibleSymbols;
        String strPossibleSymbols = "";
        try {
            strPossibleSymbols = scanner.next();
            possibleSymbols = Integer.parseInt(strPossibleSymbols);
        } catch (NumberFormatException e) {
            System.out.printf("Error: \"%s\" isn't a valid number.", strPossibleSymbols);
            return;
        }

        // Сгенерировать секретный код
        String secreteCode = generateSecreteCode(lengthOfCode, possibleSymbols);

        if (secreteCode.length() == lengthOfCode) {
            // Сгенерировать количество звёздочек
            String stars = generateStars(secreteCode.length());
            // Сгененрировать строку с символами, которые используются
            String symbols = generateSymbolsString(possibleSymbols);
            System.out.printf("The secret is prepared: %s (%s).\n", stars, symbols);
            // Начать игру
            System.out.println("Okay, let's start a game!");
            int turn = 1;
            // Играть до тех пор, пока не выиграем.
            while (true) {
                System.out.printf("Turn %d:\n", turn);
                String answer = scanner.next();
                String grade = gradeAnswer(answer, secreteCode);
                System.out.printf("Grade: %s\n", grade);
                if (String.format("%d bull(s)", lengthOfCode).equals(grade)) {
                    System.out.println("Congratulations! You guessed the secret code.");
                    break;
                }
                turn++;
            }
        } else {
            System.out.println(secreteCode);
        }
    }

    private static String generateSecreteCode(int length, int possibleSymbols) {
        if (length > 36) {
            return String.format("Error: can't generate a secret number with a length of %d because there aren't enough unique digits.", length);
        } else if (length > possibleSymbols) {
            return String.format("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", length, possibleSymbols);
        } else if (length == 0) {
            return "Error: can't generate a secret number with a length of 0.";
        } else if (possibleSymbols > 36) {
            return String.format("Error: can't generate a secret number with possible symbols of %d.", possibleSymbols);
        }
        String alphabet = generateAlphabet(possibleSymbols);
        StringBuilder secreteCode = new StringBuilder();
        Random random = new Random();
        do {
            int index = random.nextInt(possibleSymbols);
            char temporary = alphabet.charAt(index);
            if (secreteCode.indexOf(String.valueOf(temporary)) == -1) {
                if (secreteCode.length() == 0 && "0".equals(String.valueOf(temporary))) {
                    continue;
                }
                secreteCode.append(temporary);
            }
        } while (secreteCode.length() != length);

        return secreteCode.toString();
    }

    private static String gradeAnswer(String answer, String secretCode) {
        int bulls = 0;
        int cows = 0;
        for (int i = 0; i < answer.length(); i++) {
            char temporaryChar = answer.charAt(i);
            if (secretCode.contains(temporaryChar + "")) {
                if (temporaryChar == secretCode.charAt(i)) {
                    bulls++;
                } else {
                    cows++;
                }
            }
        }

        String grade = "";
        if (cows == 0 && bulls == 0) {
            grade = "None";
        } else if (bulls > 0 && cows == 0) {
            grade = String.format("%d bull(s)", bulls);
        } else if (cows > 0 && bulls == 0) {
            grade = String.format("%d cow(s)", cows);
        } else {
            grade = String.format("%d bull(s) and %d cow(s)", bulls, cows);
        }

        return grade;
    }

    private static String generateAlphabet(int possibleSymbols) {
        StringBuilder alphabet = new StringBuilder();
        for (int i = 0; i < Math.min(possibleSymbols, 10); i++) {
            alphabet.append(i);
        }
        if (possibleSymbols > 10) {
            char temporary = 'a';
            for (int i = 10; i < possibleSymbols; i++) {
                alphabet.append(temporary);
                temporary++;
            }
        }
        return alphabet.toString();
    }

    private static String generateStars(int count) {
        return "*".repeat(count);
    }

    private static String generateSymbolsString(int count) {
        StringBuilder symbols = new StringBuilder();
        symbols.append("0");
        symbols.append("-");
        if (count <= 10) {
            symbols.append(count - 1);
        } else {
            symbols.append("9, ");
            symbols.append("a-");
            char temporary = 'a';
            for (int i = 10; i < count - 1; i++) {
                temporary++;
            }
            symbols.append(temporary);
        }
        return symbols.toString();
    }
}
