package org.home.chatapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatClient extends Application {

    private VBox messagesBox;
    private TextField inputField;
    private PrintWriter out;

    @Override
    public void start(Stage stage) {
        messagesBox = new VBox(10);
        messagesBox.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(messagesBox);
        scrollPane.setFitToWidth(true);

        inputField = new TextField();
        inputField.setPromptText("Type message...");

        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(e -> sendMessage());

        inputField.setOnAction(e -> sendMessage());

        HBox bottom = new HBox(10, inputField, sendBtn);
        HBox.setHgrow(inputField, Priority.ALWAYS);
        bottom.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        root.setBottom(bottom);

        root.setStyle("""
                -fx-background-color: #ece5dd;
                """);

        Scene scene = new Scene(root, 400, 600);

        stage.setTitle("Chat App");
        stage.setScene(scene);
        stage.show();

        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 9000);

                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));

                out = new PrintWriter(socket.getOutputStream(), true);

                String message;

                while ((message = in.readLine()) != null) {
                    String finalMessage = message;

                    Platform.runLater(() ->
                            addMessage(finalMessage, false));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage() {
        String message = inputField.getText();

        if (!message.isEmpty()) {
            out.println(message);

            addMessage(message, true);

            inputField.clear();
        }
    }

    private void addMessage(String text, boolean isSender) {
        String time = LocalTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm"));

        Label textLabel = new Label(text);
        textLabel.setWrapText(true);

        Label timeLabel = new Label(time);

        timeLabel.setStyle("""
                -fx-font-size: 10px;
                -fx-text-fill: gray;
