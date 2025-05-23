# Protocol Restructuring Guide

This document explains the newly proposed structure for the protocol package in your project. The solution aims to improve clarity, extensibility, and maintainability by addressing the challenges faced in deserializing, handling, and extending message types in your protocol. Below, you will find an overview of the design, its components, and how everything works together.

---

## 🎯 **Goals**
1. **Reduce Boilerplate**:
    - Avoid large `switch` statements or long `instanceof` checks for deserialization and message handling.
2. **Encapsulate Responsibilities**:
    - Move message handling logic to well-defined handlers.
3. **Simplify Adding New Messages**:
    - Adding a new message type should require minimal updates to the codebase.
4. **Leverage Polymorphism**:
    - Replace centralized message logic with decentralized and extensible structures.

---

## 🗂 **Overview of the New Protocol Structure**

### 1. **Message Base Class**

The `Message` base class is the parent of all messages. It includes the message `type` and handles the deserialization process via a centralized factory (`MessageFactory`). Each subclass represents a specific type of message (e.g., `MoveMessage`, `ClientInfoMessage`).

#### Key Methods:
- `getType()`: Returns the message type.
- `deserialize(String json, Gson gson)`: Uses the `MessageFactory` to resolve the correct subclass and deserialize the message.

```java
public abstract class Message {
    private final String type = getClass().getSimpleName();

    public String getType() {
        return type;
    }

    public static void registerMessageType(Class<? extends Message> clazz) {
        MessageFactory.register(clazz.getSimpleName(), clazz);
    }

    public static Message deserialize(String json, Gson gson) throws Exception {
        return MessageFactory.deserialize(json, gson);
    }
}
```


---

### 2. **MessageFactory**

The `MessageFactory` maps message type strings to their respective Java classes. When deserializing, it dynamically determines which class to instantiate based on the `type` field in the incoming JSON.

#### Key Methods:
- `register(String type, Class<? extends Message> clazz)`: Registers a message type.
- `deserialize(String json, Gson gson)`: Looks up the correct message class and deserializes it.

```java
public class MessageFactory {
    private static final Map<String, Class<? extends Message>> registry = new HashMap<>();

    public static void register(String type, Class<? extends Message> clazz) {
        registry.put(type, clazz);
    }

    public static Message deserialize(String json, Gson gson) throws ProtocolException {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        Class<? extends Message> clazz = registry.get(type);
        if (clazz == null) {
            throw new ProtocolException("Unknown message type: " + type);
        }

        return gson.fromJson(json, clazz);
    }
}
```


---

### 3. **Message Registration (Initialization)**

All message types are registered at application startup using a static initializer. This ensures all supported message types are properly registered before any deserialization occurs.

#### Example: **MessageRegistry.java**
```java
public class MessageRegistry {
    static {
        registerMessages();
    }

    private static void registerMessages() {
        // Game-related messages
        Message.registerMessageType(ClientInfoMessage.class);
        Message.registerMessageType(MoveMessage.class);

        // Lobby-related messages
        Message.registerMessageType(GameReadyMessage.class);
    }

    // Optional init method to trigger the static block
    public static void init() {}
}
```


#### Usage:
Call `MessageRegistry.init()` at the beginning of your application to ensure all message types are registered.

---

### 4. **Handler Interface**

Handlers define how to process specific actions for each message type. Each endpoint will implement its own handler interface to delegate processing logic.

#### Example: **GameMessageHandler**
```java
public interface GameMessageHandler {
    void handleClientInfoMessage(ClientInfoMessage message);
    void handleMoveMessage(MoveMessage message);
    void handleGameReadyMessage(GameReadyMessage message);
    // Add more methods for any additional message types
}
```


This interface allows each endpoint to define message-specific behavior without using `if` or `switch`.

---

### 5. **MessageManager**

The `MessageManager` acts as a bridge between the deserialized messages and the handler. It encapsulates the logic of determining which handler method to call based on the message type. This keeps the endpoint clean and free from message-specific logic.

#### Example: **GameMessageManager**
```java
public class GameMessageManager {
    private final GameMessageHandler handler;

    public GameMessageManager(GameMessageHandler handler) {
        this.handler = handler;
    }

    public void handleMessage(Message message) {
        if (message instanceof ClientInfoMessage clientInfoMessage) {
            handler.handleClientInfoMessage(clientInfoMessage);
        } else if (message instanceof MoveMessage moveMessage) {
            handler.handleMoveMessage(moveMessage);
        } else if (message instanceof GameReadyMessage gameReadyMessage) {
            handler.handleGameReadyMessage(gameReadyMessage);
        } else {
            System.err.println("Unhandled message type: " + message.getType());
        }
    }
}
```


This delegation reduces the complexity of the `onMessage` method in endpoint classes.

---

### 6. **Endpoint Example**

Here’s what the updated endpoint might look like with the refactored protocol structure.

#### Example: **GameEndpoint.java**
```java
@OnMessage
public void onMessage(Session session, String json) throws IOException {
    if (gameServer == null || color == null) {
        closeWithError(session, "GameServer not properly initialized");
        return;
    }

    // Deserialize the incoming JSON into a Message object
    Message message;
    try {
        message = Message.deserialize(json, new Gson());
    } catch (Exception e) {
        closeWithError(session, "Invalid message format: " + json);
        return;
    }

    // Pass the deserialized message to the Manager for handling
    messageManager.handleMessage(message);
}
```


---

### 7. **Example Process Flow**

Let’s follow how a message gets handled:
1. A JSON payload is received by the `onMessage` method in the `GameEndpoint`.
2. The JSON is deserialized into the appropriate `Message` subclass using `Message.deserialize()`.
3. The deserialized `Message` is passed to the `GameMessageManager`.
4. The manager checks the message type and calls the corresponding method on the handler interface (e.g., `handleMoveMessage`).
5. The handler implementation in the endpoint processes the logic.

---

## 🚀 Adding a New Message

Adding a new message type is simple:
1. Create a new message class that extends `Message`.
2. Register the new class in the `MessageRegistry`.
3. Add a new method to the appropriate handler interface (e.g., `GameMessageHandler`).
4. Implement the new handler method in the endpoint.

#### Example: Adding `DrawRequestMessage`
1. **Create the Class**:
```java
public class DrawRequestMessage extends Message {
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
```


2. **Register the Class**:
```java
Message.registerMessageType(DrawRequestMessage.class);
```


3. **Add to the Handler**:
```java
void handleDrawRequestMessage(DrawRequestMessage message);
```


4. **Implement in Endpoint**:
```java
@Override
public void handleDrawRequestMessage(DrawRequestMessage message) {
    gameServer.handleDrawRequest(message.getReason());
}
```


---

## 🙌 Benefits of the Refactored Structure

1. **Extensibility**:
    - Adding new messages does not require modifying the `MessageFactory` or writing boilerplate `if` checks.
2. **Decoupled Logic**:
    - Each responsibility (serialization, deserialization, message handling) is separated and easier to manage.
3. **Readability**:
    - Message-specific processing logic is confined to handlers, simplifying endpoint methods.
4. **Code Safety**:
    - The compiler ensures you implement all required handlers for new message types, reducing runtime bugs.

---

This structure should greatly improve the clarity and maintainability of your protocol! Let me know if you have any other ideas or concerns!