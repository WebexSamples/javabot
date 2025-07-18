# JavaBot - Local HTTP Event Listener

## 📋 Description

Used to listen to events in Webex Teams on a localhost port. The only command it can understand is **hello**, replies "hello there". This simple Java-based bot serves as a perfect testing companion for webhook forwarding applications and demonstrates how to build a basic Webex Teams bot using Java and the Cisco Spark Java SDK.

## 🎯 Use Cases

**Perfect for:**
- **Testing webhook forwarding** with applications like [hookbuster](https://github.com/WebexSamples/hookbuster)
- **Local development** without requiring public webhook endpoints
- **Learning Java bot development** with Webex Teams
- **Prototyping bot interactions** in a controlled environment
- **Firewall-friendly testing** for applications behind corporate firewalls

## 🔧 Requirements

- **Webex Teams access token**: this will be used to communicate with Webex Teams. Ideally you would use the access token of the same bot here.
- **Open port on localhost**: this is where the app will listen for incoming messages
- **Java 1.8**: Required runtime environment
- **Maven 3.6.1**: Build and dependency management

## 🚀 Installation

**Quick Setup:**
```bash
# Clone the repository
git clone https://github.com/WebexSamples/javabot.git
cd javabot

# Install dependencies and compile
mvn install

# Run the application
mvn exec:java -Dexec.mainClass=Main
```

## 💻 Usage

When you run the application, you'll see the interactive CLI:

```
+---------------------------------------------+
|                                             |
|        _                  _           _     |
|       | | __ ___   ____ _| |__   ___ | |_   |
|    _  | |/ _` \ \ / / _` | '_ \ / _ \| __|  |
|   | |_| | (_| |\ V / (_| | |_) | (_) | |_   |
|    \___/ \__,_| \_/ \__,_|_.__/ \___/ \__|  |
|                                             |
|                                             |
+---------------------------------------------+

Please enter an access token
AaBbCcDdEeFfGgHhIiJjAaBbCcDdEeFfGgHhIiJjAaBbCcDdEeFfGgHhIiJj_ABCD_1a2b3c4d-1234-abcd-9876-abcdefghijkl
INFO Verifying account
INFO Account verified as Droid
Please enter port you want to listen on:
5000
INFO Service listening on localhost:5000
```

### Interactive Setup

The application will prompt you for:

1. **Access Token**: Your Webex Teams bot access token
2. **Port Number**: Local port to listen for incoming HTTP requests (e.g., 5000)

### Bot Commands

Once running, the bot responds to:
- **hello**: Responds with "hello there 👋"

**Tip**: You can use the [hookbuster](https://github.com/WebexSamples/hookbuster) to test how this app works. Start a websocket listener for `messages:created` and the **hookbuster** will forward incoming message events to localhost, where **javabot** can listen to them. No need for webhooks!

## 📁 Project Structure

```
javabot/
├── src/main/java/
│   ├── Main.java           # Application entry point and CLI
│   ├── bot/
│   │   ├── Bot.java        # Webex Teams API wrapper
│   │   └── BotService.java # HTTP server and message processing
│   └── util/
│       └── Printer.java    # Console output utilities
├── pom.xml                 # Maven configuration
├── javaBot.iml            # IntelliJ IDEA project file
└── README.md              # This file
```

## 🏗️ Architecture

### Components Overview

1. **Main.java**: Entry point that handles CLI interactions and initializes the bot
2. **Bot.java**: Wrapper around the Cisco Spark SDK with convenience methods
3. **BotService.java**: HTTP server that listens for incoming requests and processes messages
4. **Printer.java**: Utility class for colored console output

### Event Flow

```
Webex Teams → hookbuster → HTTP POST → javabot → Bot Response → Webex Teams
```

### Message Processing

1. **HTTP Request**: Receives JSON payload from webhook/hookbuster
2. **Message Parsing**: Extracts message content and metadata
3. **Command Detection**: Looks for "hello" in message text
4. **Response Generation**: Sends reply back to Webex Teams
5. **Bot Filtering**: Ignores messages from other bots

## 🔍 Code Deep Dive

### Bot.java - Webex SDK Integration

```java
public class Bot {
    private Spark spark;
    
    public Bot(String accessToken) {
        spark = Spark.builder()
                .baseUrl(URI.create("https://api.ciscospark.com/v1"))
                .accessToken(accessToken)
                .build();
    }
    
    void sayHello(String roomId) {
        Message message = new Message();
        message.setRoomId(roomId);
        message.setMarkdown("hello there 👋");
        spark.messages().post(message);
    }
}
```

### BotService.java - HTTP Server

```java
public class BotService {
    public void run() {
        ServerSocket serverSocket = new ServerSocket(port);
        
        while (true) {
            Socket socket = serverSocket.accept();
            JSONObject requestBody = getRequestBodyFromInput(socket);
            evaluateRequest(requestBody);
        }
    }
    
    private void evaluateRequest(JSONObject requestBody) {
        String resource = (String) requestBody.get("resource");
        
        if (resource.equals("messages")) {
            // Process message and respond to "hello"
            if (messageList.contains("hello")) {
                bot.sayHello(roomId);
            }
        }
    }
}
```

## 🔧 Dependencies

```xml
<dependencies>
 <dependency>
   <groupId>org.glassfish</groupId>
   <artifactId>javax.json</artifactId>
   <version>1.0.4</version>
 </dependency>

 <dependency>
   <groupId>com.ciscospark</groupId>
   <artifactId>ciscospark-client</artifactId>
   <version>1.0-SNAPSHOT</version>
 </dependency>

 <dependency>
   <groupId>org.json</groupId>
   <artifactId>json</artifactId>
   <version>20180130</version>
 </dependency>
</dependencies>
```

### Key Dependencies

- **javax.json**: JSON processing API
- **ciscospark-client**: Official Cisco Spark Java SDK
- **org.json**: JSON parsing and manipulation library

## 🧪 Testing & Integration

### Testing with Hookbuster

The recommended way to test javabot is with [hookbuster](https://github.com/WebexSamples/hookbuster):

1. **Start JavaBot**: Run javabot and set it to listen on port 5000
2. **Start Hookbuster**: Configure hookbuster to forward messages to localhost:5000
3. **Test Interaction**: Send "hello" to your bot in Webex Teams
4. **Verify Response**: Bot should respond with "hello there 👋"

### Manual Testing

You can also test manually by sending HTTP POST requests:

```bash
curl -X POST http://localhost:5000 \
  -H "Content-Type: application/json" \
  -d '{
    "resource": "messages",
    "data": {
      "id": "message-id",
      "roomId": "room-id",
      "personEmail": "user@example.com"
    }
  }'
```

### Event Payload Example

JavaBot expects JSON payloads in this format:

```json
{
  "resource": "messages",
  "event": "created",
  "data": {
    "id": "message-id",
    "roomId": "room-id",
    "personId": "person-id",
    "personEmail": "user@example.com",
    "text": "hello bot",
    "created": "2023-01-01T12:00:00.000Z"
  }
}
```

## 🔐 Security Features

### Bot Message Filtering

JavaBot automatically filters out messages from other bots:

```java
if (!message.getPersonEmail().contains("@webex.bot")) {
    // Process message from human user
}
```

### Access Token Validation

The application validates access tokens before starting:

```java
private static boolean verifyAccount(String accessToken) {
    try {
        Person me = bot.getMyDetails();
        return true;
    } catch (Exception e) {
        return false;
    }
}
```

## 🎨 Console Output

JavaBot provides colored console output for better visibility:

- **INFO**: Cyan text for general information
- **ERROR**: Red text for error messages
- **Logo**: ASCII art banner on startup

### Output Examples

```
INFO Verifying account
INFO Account verified as DROID
INFO Service listening on localhost:5000
INFO Request received
INFO {"resource":"messages","data":{"id":"123","roomId":"456"}}
```

## 🛠️ Development

### Building from Source

```bash
# Compile the project
mvn compile

# Run tests (if any)
mvn test

# Package as JAR
mvn package

# Run with custom main class
mvn exec:java -Dexec.mainClass=Main
```

### IDE Setup

The project includes IntelliJ IDEA configuration (`javaBot.iml`). You can:

1. **Import Project**: Open the project in IntelliJ IDEA
2. **Maven Integration**: IDE will automatically handle dependencies
3. **Run Configuration**: Set up run configuration for Main.java

### Extending Functionality

To add new commands:

1. **Modify BotService.java**: Add new command detection logic
2. **Update Bot.java**: Add new response methods
3. **Test**: Verify new commands work with hookbuster

Example command addition:

```java
// In evaluateRequest method
if (messageList.contains("goodbye")) {
    bot.sayGoodbye(roomId);
}

// In Bot.java
void sayGoodbye(String roomId) {
    Message message = new Message();
    message.setRoomId(roomId);
    message.setMarkdown("goodbye! 👋");
    spark.messages().post(message);
}
```

## 🔄 Error Handling

### Common Errors

**Port Already in Use**
```
ERROR Address already in use
ERROR Service not running
```
*Solution*: Choose a different port or stop the process using the port

**Invalid Access Token**
```
ERROR Access token invalid
```
*Solution*: Verify your token has the correct permissions and hasn't expired

**Network Issues**
```
ERROR Connection refused
```
*Solution*: Check network connectivity and firewall settings

### Debug Mode

Add debug logging by modifying the Printer class:

```java
public static void printDebug(String message) {
    System.out.println(YELLOW_BRIGHT + "DEBUG " + message + RESET);
}
```

## 📊 Performance Considerations

### Resource Usage

- **Memory**: Minimal footprint, mainly for HTTP server and SDK
- **CPU**: Low usage, event-driven processing
- **Network**: Outbound HTTPS to Webex APIs only

### Scalability

- **Single-threaded**: Handles one request at a time
- **Lightweight**: Suitable for development and testing
- **Blocking I/O**: Uses traditional Java I/O model

## 🚀 Production Considerations

### Deployment

For production use, consider:

1. **Threading**: Add multi-threading for concurrent request handling
2. **Error Recovery**: Implement retry logic for API failures
3. **Logging**: Add comprehensive logging framework
4. **Configuration**: Externalize configuration (properties files)
5. **Security**: Implement proper authentication and authorization

### Docker Deployment

```dockerfile
FROM openjdk:8-jre-slim

WORKDIR /app
COPY target/javaBot-1.0-SNAPSHOT.jar app.jar
COPY target/lib/ lib/

EXPOSE 5000

CMD ["java", "-cp", "app.jar:lib/*", "Main"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-feature`
3. Make your changes
4. Test with hookbuster integration
5. Submit a pull request

### Development Guidelines

- Follow Java naming conventions
- Add JavaDoc comments for public methods
- Test all new functionality with hookbuster
- Maintain backward compatibility

## 📄 License

This project is licensed under the terms specified in the [LICENSE](LICENSE) file.

## 🔗 Related Projects

- [Hookbuster](https://github.com/WebexSamples/hookbuster): WebSocket-to-HTTP event forwarder
- [Webex Node Bot Framework](https://github.com/WebexCommunity/webex-node-bot-framework): Node.js bot framework
- [Cisco Spark Java SDK](https://github.com/ciscospark/spark-java-sdk): Official Java SDK

## 📚 Additional Resources

- [Webex Developer Portal](https://developer.webex.com/)
- [Cisco Spark Java SDK Documentation](https://github.com/ciscospark/spark-java-sdk)
- [Webex Teams API Reference](https://developer.webex.com/docs/api/v1)
- [Maven Documentation](https://maven.apache.org/guides/)

## 🆘 Support

- Create an issue in this repository
- Review [Webex Developer Documentation](https://developer.webex.com/docs)
- Join the [Webex Developer Community](https://developer.webex.com/community)

---

**Repository**: https://github.com/WebexSamples/javabot
