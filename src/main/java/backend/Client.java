package backend;

import message.DrinkMessage;
import message.FinishedMakingDrinkMessage;
import message.MakingDrinkMessage;
import message.Message;

import java.io.*;
import java.net.Socket;

public class Client {

    private static final String HOST_NAME = "IAGO";
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final ClientFrame frame;

    public static void main(String[] args) {
            new Client();

    }

    private ObjectOutputStream out;

    public Client(){
        frame = new ClientFrame(this);
        try
        {
            socket = new Socket(HOST_NAME, Server.PORT_NUMBER);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            this.out = oos;
            Message fromServer;

            while ((fromServer = (Message) ois.readObject()) != null) {
                handleMessage(fromServer);
                System.out.println("Server: " + fromServer.getMessage());
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            if (socket != null) {
                socket.close();
            }
            if (oos != null) {
                oos.close();
            }
            if (ois != null) {
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * TODO:
     * backend.Client windows need to respond to messages correctly
     * backend.Client windows update when another user is making a drink
     * Drink-maker window gets a populating list of drinks to make
     * Support multiple drink makers at once.
     * backend.Client windows notify when tea is being made.
     * backend.Server needs to gracefully handle clients disconnecting.
     * Polish
     * Profit
     */

    public void sendMessage(Message message){
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(Message message){
        if(message instanceof DrinkMessage){
            frame.drinkRequested((DrinkMessage) message);
        } else if (message instanceof MakingDrinkMessage){
            frame.drinkBeingMade((MakingDrinkMessage) message);
        } else if (message instanceof FinishedMakingDrinkMessage){
            frame.drinkFinished((FinishedMakingDrinkMessage) message);
        }
    }
}
