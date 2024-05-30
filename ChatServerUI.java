package com.chat.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import com.chat.server.ChatServer;
import java.io.PrintWriter;
import java.util.HashSet;

public class ChatServerUI extends Frame {
    private TextArea textArea;
    private TextField textField;
    private ChatServer server;
    private HashSet<PrintWriter> clientWriters;

    public ChatServerUI() {
        super("Chat Server");
        textArea = new TextArea();
        textArea.setEditable(false);
        textField = new TextField();
        add(textArea, BorderLayout.CENTER);
        add(textField, BorderLayout.SOUTH);
        setSize(400, 300);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = textField.getText();
                textField.setText("");
                displayMessage("Server: " + message);
                broadcastMessage("Server: " + message);
            }
        });

        clientWriters = new HashSet<>();
        new Thread(() -> {
            try {
                server = new ChatServer(clientWriters);
                server.start();
            } catch (IOException e) {
                textArea.append("Error starting server: " + e.getMessage() + "\n");
            }
        }).start();
    }

    public void displayMessage(String message) {
        textArea.append(message + "\n");
    }

    public void broadcastMessage(String message) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
    }

    public static void main(String[] args) {
        new ChatServerUI();
    }
}
