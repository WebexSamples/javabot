package bot;

import com.ciscospark.*;

import java.net.*;

/**
 * Wraps the Spark object and adds some convenience methods.
 */
public class Bot {

    private Spark spark;

    public Bot(String accessToken) {
        spark = Spark.builder().
                baseUrl(URI.create("https://api.ciscospark.com/v1")).
                accessToken(accessToken).build();
    }

    public Person getMyDetails() {
        return spark.people().path("/me").get();
    }

    Message getMessageById(String messageId) {
        return spark.messages().path("/" + messageId).get();
    }

    void sayHello(String roomId) {
        Message message = new Message();
        message.setRoomId(roomId);
        message.setMarkdown("hello there \uD83D\uDC4B");
        spark.messages().post(message);
    }

}
