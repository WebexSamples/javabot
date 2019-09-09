import bot.Bot;
import bot.BotService;
import com.ciscospark.Person;
import util.Printer;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Printer.printLogo();

        //request a bot access token
        String accessToken = requestToken();

        //request a port to listen on
        int port = requestPort();

        //start the service
        new BotService(new Bot(accessToken), port).run();

    }

    /**
     * @return the verified access token entered by the user
     */
    private static String requestToken() {
        boolean verified = false;
        String token = "";

        while (!verified) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter an access token:");
            token = scanner.nextLine();

            if (verifyAccount(token)) {
                verified = true;
            }
        }

        return token;
    }

    /**
     * @return whether or not the entered access token is valid,
     * i.e. can fetch details of itself with /people/me
     */
    private static boolean verifyAccount(String accessToken) {
        boolean isValid = false;
        Bot bot = new Bot(accessToken);

        try {
            Printer.printInfo("Verifying account");
            Person me = bot.getMyDetails();
            isValid = true;
            Printer.printInfo("Account verified as " + me.getDisplayName().toUpperCase());

        } catch (Exception e) {
            Printer.printError("Access token invalid");
        }

        return isValid;
    }

    /**
     * @return port number entered by the user
     */
    private static int requestPort() {
        System.out.println("Please enter port you want to listen on:");

        Scanner scanner = new Scanner(System.in);
        int input;

        do {
            while (!scanner.hasNextInt()) {
                Printer.printError("Not a number, try again");
                scanner.next();
            }
            input = scanner.nextInt();
        } while (input < 0);

        return input;

    }


}

