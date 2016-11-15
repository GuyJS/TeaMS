package backend;

import message.DrinkMessage;
import message.FinishedMakingDrinkMessage;
import message.MakingDrinkMessage;
import message.Message;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.List;

public class Server extends Observable {

    public static final int PORT_NUMBER = 7345;
    private Set<ServerThread> clients = new HashSet<>();
    private JTextArea jTextArea;

    public static void main(String[] args) {
        Server server = new Server();
    }

    public Server() {
        try (
                ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
        ) {
            JFrame frame = new JFrame();
            frame.setSize(700, 400);
            JPanel panel = new JPanel(new BorderLayout());
            jTextArea = new JTextArea(15, 50);
            JScrollPane scrollPane = new JScrollPane(jTextArea);
            panel.add(scrollPane, BorderLayout.CENTER);
            frame.add(panel);
            frame.setVisible(true);
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ServerThread t = new ServerThread(clientSocket, this);
                t.start();
                clients.add(t);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<ServerThread> threadsMakingDrinks = new ArrayList<>();

    public void handleMessage(Message message, ServerThread thread){
        jTextArea.setText(jTextArea.getText() + message.getMessage() + "\n");
        if(message instanceof DrinkMessage){
            messageThreads(message, threadsMakingDrinks);
        } else if (message instanceof MakingDrinkMessage){
            threadsMakingDrinks.add(thread);
            messageOthers(message, thread);
        } else if (message instanceof FinishedMakingDrinkMessage){
            threadsMakingDrinks.remove(thread);
            messageOthers(message, thread);
        }
    }

    public void messageOthers(Message message, ServerThread senderThread){
        clients.stream()
                .filter(serverThread -> !senderThread.equals(serverThread))
                .forEach(serverThread -> serverThread.messageClient(message));
    }

    public void messageThreads(Message message, List<ServerThread> threadsToMessage){
        threadsToMessage.stream().forEach(thread -> {
            boolean succeeded = thread.messageClient(message);
            if(!succeeded){
                clients.remove(thread);
            }
        });
    }
}
