package foodordering;

import java.util.Scanner;

/** Small helper for reading console input safely. */
public class Input {
    private static final Scanner sc = new Scanner(System.in);

    public static String text(String prompt) {
        System.out.print(prompt);
        return sc.hasNextLine() ? sc.nextLine().trim() : "";
    }

    public static int integer(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(text(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    public static double decimal(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(text(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /** Removes characters that are used as separators in the .txt files. */
    public static String clean(String s) {
        return s.replace("|", "").replace(";", "").replace(":", "").trim();
    }

    /** Returns a line of dashes, e.g. line(10) -> "----------". */
    public static String line(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) sb.append('-');
        return sb.toString();
    }
}
