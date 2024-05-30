package com.chat.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import com.chat.client.ChatClient;

public class ChatClientUI extends Frame {
    private TextArea textArea;
    private TextField textField;
    private ChatClient client;

    public ChatClientUI(String serverAddress, int port) {
        super("Chat Client");
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

        try {
            client = new ChatClient(serverAddress, port, this);
            client.start();
        } catch (IOException e) {
            textArea.append("Error connecting to server: " + e.getMessage() + "\n");
        }

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = textField.getText();
                textField.setText("");
                client.sendMessage(message);
            }
        });
    }

    public void displayMessage(String message) {
        textArea.append(message + "\n");
    }

    public static void main(String[] args) {
        new ChatClientUI("localhost", 12345);
    }
}
