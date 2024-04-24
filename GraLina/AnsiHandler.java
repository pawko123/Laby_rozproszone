package GraLina;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnsiHandler {
    public static String addcolor(String text, String color) {
        return switch (color) {
            case "yellow" -> "\033[33m" + text + "\033[0m";
            case "red" -> "\033[31m" + text + "\033[0m";
            case "blue" -> "\033[34m" + text + "\033[0m";
            case "green" -> "\033[32m" + text + "\033[0m";
            default -> text;
        };
    }
}
