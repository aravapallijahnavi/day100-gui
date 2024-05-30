package com.chat.client;

import java.io.*;
import java.net.*;
import com.chat.ui.ChatClientUI;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ChatClientUI ui;

    public ChatClient(String serverAddress, int port, ChatClientUI ui) throws IOException {
        this.ui = ui;
        socket = new Socket(serverAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void start() {
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    ui.displayMessage(message);
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }).start();
    }
}
