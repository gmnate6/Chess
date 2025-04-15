package com.nathanholmberg.chess.server;

import com.nathanholmberg.chess.server.endpoints.PingEndpoint;
import org.glassfish.tyrus.server.Server;

public class ServerApplication {
    private static Class<?>[] getEndpointClasses() {
        return new Class<?>[] {
                PingEndpoint.class
        };
    }

    public static void main(String[] args) {
        Server server = new Server(
                "0.0.0.0",
                8080,
                "/",
                null,
                getEndpointClasses()
        );

        try {
            server.start();
            System.out.println("✅ WebSocket server started on ws://localhost:8080");
            System.out.println("⌨️ Press Enter to stop the server...");
            System.in.read(); // Wait for Enter to stop
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
