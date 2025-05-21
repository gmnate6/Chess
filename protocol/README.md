# Chess Protocol Package Overview
This document provides an overview of the protocol package, explaining how it is organized, how it works with endpoints, and how new messages can be added. It includes descriptions of all key components and outlines the flow of data through the system.
## Key Architecture Highlights
1. **Centralized Message Handling**:
    - The protocol package uses a **message registry** (`MessageRegistry`) to manage and handle messages based on their type, eliminating scattered type checks and manual routing.
    - Messages are deserialized dynamically and routed to their respective handlers defined in the registry.

2. **Separation of Concerns**:
    - Messages represent **data** (e.g., what is being sent or received).
    - The **registry** binds message types to their corresponding handlers, centralizing this logic.
    - Endpoints simply **delegate processing** to the registry, ensuring clean and maintainable WebSocket endpoints.

3. **Ease of Extensibility**:
    - Adding a new message is simple and requires minimal changes (explained in the _How to Add New Messages_ section below).
    - Handlers for messages are registered in one place, ensuring everything is organized.

## Flow of Data Through the Protocol
1. **Incoming Message**:
    - An endpoint (e.g., ) receives a raw JSON message from a client. `GameEndpoint`
    - The JSON is passed to the `MessageRegistry`.

2. **Deserialization**:
    - The `MessageRegistry` parses the JSON string and retrieves the field. `type`
    - The is used to look up the corresponding class in the registry, and the JSON is deserialized into an instance of that class. `type``Message`

3. **Validation** _(Optional)_:
    - If the message class overrides the `validate()` method, validation logic is applied at this step to ensure the message is valid.

4. **Message Handling**:
    - Based on the field, a registered **handler** (a `Consumer<Message>`) is invoked for this message. The registry routes the deserialized message to its designated handler. `type`

5. **Processing**:
    - The handler performs the required action based on the message. This could involve updating the server state (e.g., processing a move in the game) or sending a response back to the client.

## Component Breakdown
### 1. **Base Message Class**
- Every message in the protocol extends the abstract class. `Message`
- The class includes common properties and methods, such as:
    - A field derived from the message class name. `type`
    - An optional `validate()` method that can be overridden for custom validation logic.

`Message`

### 2. **MessageRegistry**
- The `MessageRegistry` is a static class that centralizes:
    - **Message Classes**: Maps strings (e.g., ) to their corresponding subclasses for deserialization. `type``"MoveMessage"``Message`
    - **Handlers**: Maps strings to handler functions (`Consumer<T>`), which define how to process each message type. `type`

- The registry is responsible for:
    - Deserializing raw JSON messages into instances. `Message`
    - Validating and routing messages to their registered handlers.

### 3. **Message Registration**
- During application startup, all supported message types must be registered with the `MessageRegistry` via the `register()` method.
- The registry binds each to:
    - The corresponding class for deserialization. `Message`
    - A handler function for processing the message.

`type`

### 4. **Handlers**
- Each message type has a handler function (`Consumer<T>`) registered in the `MessageRegistry`.
- Handlers define the logic for processing that message type. For example:
    - A handler could verify the move and update the server-side game state. `MoveMessage`
    - A handler could end the game for the resigning player. `ResignMessage`

## How the Protocol is Used by Endpoints
1. **WebSocket Endpoints**:
    - Endpoints (e.g., ) receive raw JSON messages via the WebSocket. `GameEndpoint`
    - When an incoming message is received:
        - The endpoint passes the raw message to the `MessageRegistry` for processing.
        - The endpoint itself does not need to know the details of the message or its type.

2. **Message Deserialization**:
    - The `MessageRegistry` deserializes the message into its respective subclass using the field in the JSON. `Message``type`

3. **Routing and Processing**:
    - The deserialized message is passed to the handler registered for that specific message type.
    - The handler performs the required action (e.g., updating the server state, broadcasting updates to other clients, etc.).

## How to Add New Messages
Adding a new message to the protocol package is a three-step process:
### Step 1: Define the New Message
- Create a new class that extends . `Message`
- If the message contains additional data, include fields and getter/setter methods.
- Optionally override the `validate()` method to implement custom validation logic.

Example:
``` java
package com.nathanholmberg.chess.protocol.messages.game.client;

import com.nathanholmberg.chess.protocol.messages.Message;

public class UndoMoveMessage extends Message {
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void validate() throws Exception {
        if (reason == null || reason.isEmpty()) {
            throw new Exception("Reason cannot be null or empty");
        }
    }
}
```
### Step 2: Register the Message
- Register the message in the `MessageRegistry` with its corresponding handler.

Example:
``` java
MessageRegistry.register("UndoMoveMessage", UndoMoveMessage.class, message -> {
    System.out.println("Undo Move requested: " + message.getReason());
    // Add custom handling logic here
});
```
### Step 3: Handle the Message
- Define the logic for processing this message in the handler you provided during registration.

## Example Data Flow: UndoMoveMessage
1. **Client Sends Message**:
    - Client sends a JSON string:
``` json
     {
       "type": "UndoMoveMessage",
       "reason": "Misclicked move"
     }
```
1. **Endpoint Receives Raw JSON**:
    - The WebSocket endpoint passes the JSON to the `MessageRegistry` for processing:
``` java
     Message message = MessageRegistry.deserialize(rawMessage);
     MessageRegistry.processMessage(message);
```
1. **MessageRegistry Deserializes the JSON**:
    - The `MessageRegistry` looks up `"UndoMoveMessage"` in its registry, finds the `UndoMoveMessage` class, and deserializes the JSON into an instance of this class.

2. **Validation**:
    - The `validate()` method of `UndoMoveMessage` is invoked to ensure the data is valid.

3. **Handler is Invoked**:
    - The registered handler for `UndoMoveMessage` is called, where the server can process the request to undo the last move.

## Benefits of the Reworked Protocol
1. **Centralized Logic**:
    - The `MessageRegistry` ensures all message types and handlers are organized in one place.

2. **Extensibility**:
    - Adding new messages requires minimal effort (define, register, handle). No changes are needed in the WebSocket endpoint.

3. **Type Safety**:
    - Strong typing and compile-time checking ensure that each message is processed correctly and reliably.

4. **Ease of Maintenance**:
    - Handlers and message types are easy to find and modify as they are centralized in the registry.
