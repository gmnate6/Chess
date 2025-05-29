package com.nathanholmberg.chess.protocol.handlers;

import com.nathanholmberg.chess.protocol.MessageSerializer;
import com.nathanholmberg.chess.protocol.constants.Side;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.lobby.server.GameReadyMessage;
import com.nathanholmberg.chess.protocol.messages.lobby.server.JoinedMatchmakingMessage;

public class LobbyEndpointMessageHandler extends AbstractEndpointMessageHandler {
    private ClientHandler clientHandler;
    public interface ClientHandler extends Handler {
        void onJoinedMatchmaking(JoinedMatchmakingMessage message);
        void onGameReady(GameReadyMessage message);
    }

    private ServerHandler serverHandler;
    public interface ServerHandler extends Handler {

    }

    @Override
    public void setHandler(Handler handler) {
        super.setHandler(handler);
        if (side == Side.CLIENT) {
            clientHandler = (ClientHandler) handler;
        } else {
            serverHandler = (ServerHandler) handler;
        }
    }

    public LobbyEndpointMessageHandler(Side side) {
        super(side);
    }

    @Override
    protected void handleClientMessage(Message message) {
        if (message instanceof JoinedMatchmakingMessage joinedMatchmakingMessage) {
            clientHandler.onJoinedMatchmaking(joinedMatchmakingMessage);
        } else if (message instanceof GameReadyMessage gameReadyMessage) {
            clientHandler.onGameReady(gameReadyMessage);
        } else {
            unknownMessage(MessageSerializer.serialize(message), "Unknown message type: " + message.getType());
        }
    }

    @Override
    protected void handleServerMessage(Message message) {

    }
}
