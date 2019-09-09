package util;

/**
 * Utility class for printing info to the console.
 */
public class Printer {

    private static final String CYAN_BRIGHT = "\033[0;96m";
    private static final String RESET = "\033[0m";
    private static final String RED_BRIGHT = "\033[0;91m";

    public static void printLogo() {

        System.out.println("+---------------------------------------------+\n"
                + "|                                             |\n"
                + "|        _                  _           _     |\n"
                + "|       | | __ ___   ____ _| |__   ___ | |_   |\n"
                + "|    _  | |/ _` \\ \\ / / _` | '_ \\ / _ \\| __|  |\n"
                + "|   | |_| | (_| |\\ V / (_| | |_) | (_) | |_   |\n"
                + "|    \\___/ \\__,_| \\_/ \\__,_|_.__/ \\___/ \\__|  |\n"
                + "|                                             |\n"
                + "|                                             |\n"
                + "+---------------------------------------------+\n");

    }

    public static void printInfo(String message) {
        System.out.println(CYAN_BRIGHT
                + "INFO " + message
                + RESET);
    }

    public static void printError(String message) {
        System.out.println(RED_BRIGHT
                + "ERROR " + message
                + RESET);
    }

}
