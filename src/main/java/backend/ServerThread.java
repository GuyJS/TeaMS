package backend;

import message.Message;

import java.io.*;
import java.net.Socket;
import java.util.Observer;

public class ServerThread extends Thread {

    private Server server;
    private Socket client;
    private ObjectOutputStream out;

    public ServerThread(Socket client, Server server){
        this.server = server;
        this.client = client;
        System.out.println("Created new thread.");
    }

    @Override
    public void run() {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            this.out = oos;
            Observer observer = (o, arg) -> messageClient((Message) arg);
            server.addObserver(observer);

            Message input;
            while ((input = (Message) ois.readObject()) != null){
                server.handleMessage(input, this);
            }
        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
        }
    }

    public boolean messageClient(Message message){
        try {
            out.writeObject(message);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
