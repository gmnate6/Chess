package com.nathanholmberg.chess.protocol.handlers;

import com.nathanholmberg.chess.protocol.MessageSerializer;
import com.nathanholmberg.chess.protocol.constants.Side;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.ClientInfoMessage;
import com.nathanholmberg.chess.protocol.messages.game.MoveMessage;
import com.nathanholmberg.chess.protocol.messages.game.client.*;
import com.nathanholmberg.chess.protocol.messages.game.server.*;

public class GameEndpointMessageHandler extends AbstractEndpointMessageHandler {
    private ClientHandler clientHandler;
    public interface ClientHandler extends Handler {
        void onClientInfo(ClientInfoMessage message);
        void onMove(MoveMessage message);
        void onAcceptDraw(AcceptDrawMessage message);
        void onDeclineDraw(DeclineDrawMessage message);
        void onOfferDraw(OfferDrawMessage message);
        void onRequestGameState(RequestGameStateMessage message);
        void onResign(ResignMessage message);
    }

    private ServerHandler serverHandler;
    public interface ServerHandler extends Handler {
        void onClientInfo(ClientInfoMessage message);
        void onMove(MoveMessage message);
        void onClockUpdate(ClockUpdateMessage message);
        void onDrawOffered(DrawOfferedMessage message);
        void onGameEnd(GameEndMessage message);
        void onGameStart(GameStartMessage message);
        void onGameState(GameStateMessage message);
        void onIllegalMove(IllegalMoveMessage message);
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

    public GameEndpointMessageHandler(Side side) {
        super(side);
    }

    @Override
    protected void handleClientMessage(Message message) {
        if (message instanceof ClientInfoMessage clientInfoMessage) {
            clientHandler.onClientInfo(clientInfoMessage);
        } else if (message instanceof MoveMessage moveMessage) {
            clientHandler.onMove(moveMessage);
        }

        else if (message instanceof AcceptDrawMessage acceptDrawMessage) {
            clientHandler.onAcceptDraw(acceptDrawMessage);
        } else if (message instanceof DeclineDrawMessage declineDrawMessage) {
            clientHandler.onDeclineDraw(declineDrawMessage);
        } else if (message instanceof OfferDrawMessage offerDrawMessage) {
            clientHandler.onOfferDraw(offerDrawMessage);
        } else if (message instanceof RequestGameStateMessage requestGameStateMessage) {
            clientHandler.onRequestGameState(requestGameStateMessage);
        } else if (message instanceof ResignMessage resignMessage) {
            clientHandler.onResign(resignMessage);
        }

        else {
            unknownMessage(MessageSerializer.serialize(message), "Unknown message type: " + message.getType());
        }
    }

    @Override
    protected void handleServerMessage(Message message) {
        if (message instanceof ClientInfoMessage clientInfoMessage) {
            serverHandler.onClientInfo(clientInfoMessage);
        } else if (message instanceof MoveMessage moveMessage) {
            serverHandler.onMove(moveMessage);
        }

        else if (message instanceof ClockUpdateMessage clockUpdateMessage) {
            serverHandler.onClockUpdate(clockUpdateMessage);
        } else if (message instanceof DrawOfferedMessage drawOfferedMessage) {
            serverHandler.onDrawOffered(drawOfferedMessage);
        } else if (message instanceof GameEndMessage gameEndMessage) {
            serverHandler.onGameEnd(gameEndMessage);
        } else if (message instanceof GameStartMessage gameStartMessage) {
            serverHandler.onGameStart(gameStartMessage);
        } else if (message instanceof GameStateMessage gameStateMessage) {
            serverHandler.onGameState(gameStateMessage);
        } else if (message instanceof IllegalMoveMessage illegalMoveMessage) {
            serverHandler.onIllegalMove(illegalMoveMessage);
        }

        else {
            unknownMessage(MessageSerializer.serialize(message), "Unknown message type: " + message.getType());
        }
    }
}
