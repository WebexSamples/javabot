package bot;

import com.ciscospark.Message;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import util.Printer;

/**
 * Starts a local socket to listen on a localhost port,
 * specified in the constructor.
 */
public class BotService {

    private Bot bot;
    private int port;

    public BotService(Bot bot, int port) {
        this.bot = bot;
        this.port = port;
    }

    public void run() {

        Printer.printInfo("Service listening on localhost:" + port);

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {

                try {
                    //waits for incoming request
                    Socket socket = serverSocket.accept();

                    JSONObject requestBody = getRequestBodyFromInput(socket);
                    evaluateRequest(requestBody);

                } catch (Exception e) {
                    Printer.printError(e.getMessage());
                }
            }
        } catch (IOException e) {
            //socket already in use
            Printer.printError(e.getMessage());
            Printer.printError("Service not running");
        }
    }

    /**
     * Evaluates a JSON request. Only handles messages
     * resources.
     *
     * @param requestBody JSON to evaluate
     */
    private void evaluateRequest(JSONObject requestBody) {

        //print info to the console
        Printer.printInfo("Request received");
        Printer.printInfo(requestBody.toString());

        String resource = (String) requestBody.get("resource");
        JSONObject data = (JSONObject) requestBody.get("data");
        String roomId = (String) data.get("roomId");

        if (resource.equals("messages")) {
            String messageId = (String) data.get("id");
            Message message = bot.getMessageById(messageId);

            if (!message.getPersonEmail().contains("@webex.bot")) {
                //split the message along spaces
                String[] trimmedMessage = message.getText().split("\\s");
                //convert the message array into a searchable list
                ArrayList<String> messageList = new ArrayList<>(Arrays.asList(trimmedMessage));

                if (messageList.contains("hello")) {
                    bot.sayHello(roomId);
                }
            }
        }
    }

    /**
     * Retrieves the body of the request from the stream as a JSON object.
     * */
    private JSONObject getRequestBodyFromInput(Socket socket) throws IOException {

        InputStream is = socket.getInputStream();
        InputStreamReader isReader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isReader);

        //reads through the header line
        String headerLine;
        while ((headerLine = br.readLine()).length() != 0) {
            //System.out.println(headerLine);
        }

        StringBuilder payload = new StringBuilder();

        while (br.ready()) {
            payload.append((char) br.read());
        }

        return new JSONObject(payload.toString());

    }

}
