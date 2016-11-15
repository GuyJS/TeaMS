package backend;

import message.DrinkMessage;
import message.FinishedMakingDrinkMessage;
import message.MakingDrinkMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientFrame extends JFrame {

    public static final String DRINK_PANEL = "DRINK_PANEL";
    public static final String REQUESTED_PANEL = "REQUESTED_PANEL";
    public static final String MAKE_DRINK = "Make a Drink";
    public static final String FINISHED_MAKING = "Finish Making Drink";
    private JPanel namePanel;
    private JPanel drinkPanel;
    private JPanel sendPanel;
    private JTextField nameField;
    private JTextArea drinkArea;
    private JButton sendButton;
    private final Client client;
    private JLabel incomingMessageLabel;
    private JButton makeDrinkButton;
    private JPanel drinksRequestedPanel;
    private JTextArea requestedDrinks;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    public ClientFrame(Client client){
        this.client = client;
        setLayout(new BorderLayout());
        setSize(700, 350);
        setTitle("TeaMS");
        JPanel panel = new JPanel(new BorderLayout());

        createDrinkPanel();
        createNamePanel();
        createSendPanel();

        panel.add(namePanel, BorderLayout.NORTH);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(drinkPanel, DRINK_PANEL);
        cardPanel.add(drinksRequestedPanel, REQUESTED_PANEL);

        panel.add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, DRINK_PANEL);
        panel.add(sendPanel, BorderLayout.SOUTH);
        panel.setVisible(true);
        add(panel, BorderLayout.NORTH);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                client.closeConnection();
                System.exit(0);
            }
        });
        setVisible(true);
    }

    private void createNamePanel(){
        JLabel nameLabel = new JLabel("Name: ");
        nameField = new JTextField(12);
        namePanel = new JPanel(new FlowLayout());
        namePanel.add(nameLabel);
        namePanel.add(nameField);
    }

    private void createDrinkPanel(){
        JLabel drinkLabel = new JLabel("Drink:");
        drinkLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JLabel requestedDrinkLabel = new JLabel("Requested drinks:");
        requestedDrinkLabel.setHorizontalAlignment(SwingConstants.LEFT);

        drinkArea = new JTextArea(12, 50);
        drinkPanel = new JPanel();
        initialiseCardPanel(drinkLabel, drinkPanel, drinkArea);

        requestedDrinks = new JTextArea(12, 50);
        drinksRequestedPanel = new JPanel();
        initialiseCardPanel(requestedDrinkLabel, drinksRequestedPanel, requestedDrinks);
    }

    private void initialiseCardPanel(JLabel label, JPanel panel, JTextArea textArea) {
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMaximumSize(new Dimension(500, 700));
        panel.setLayout(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(wrapInScrollPane(textArea), BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(5, 20, 5, 20));
    }

    private void createSendPanel(){
        sendPanel = new JPanel(new FlowLayout());
        incomingMessageLabel = new JLabel("");
        sendButton = new JButton("Send drink request");
        sendButton.addActionListener(e -> client.sendMessage( new DrinkMessage(nameField.getText(), drinkArea.getText()) ));
        makeDrinkButton = new JButton(MAKE_DRINK);
        makeDrinkButton.addActionListener( new MakeCancelDrinkButtonListener(this, makeDrinkButton) );
        sendPanel.add(incomingMessageLabel);
        sendPanel.add(sendButton);
        sendPanel.add(makeDrinkButton);
        setSendItemsVisibility(false);
    }

    public void drinkRequested(DrinkMessage message){
        StringBuilder sb = new StringBuilder(requestedDrinks.getText());
        sb.append(message.getName() + ":\n");
        sb.append(message.getMessage() + "\n\n" );
        requestedDrinks.setText(sb.toString());
    }

    public void drinkBeingMade(MakingDrinkMessage message){
        incomingMessageLabel.setText(message.getMessage());
        setSendItemsVisibility(true);
    }

    public void drinkFinished(FinishedMakingDrinkMessage message){
        setSendItemsVisibility(false);
        incomingMessageLabel.setText("");
    }

    public void makingDrink(){
        cardLayout.show(cardPanel, REQUESTED_PANEL);
        requestedDrinks.setText("");
        client.sendMessage( new MakingDrinkMessage(nameField.getText()) );
        makeDrinkButton.setText(FINISHED_MAKING);
    }

    public void finishMaking(){
        cardLayout.show(cardPanel, DRINK_PANEL);
        client.sendMessage( new FinishedMakingDrinkMessage(nameField.getText()) );
        makeDrinkButton.setText(MAKE_DRINK);
    }

    private void setSendItemsVisibility(boolean aFlag) {
        incomingMessageLabel.setVisible(aFlag);
        sendButton.setVisible(aFlag);
    }

    private Component wrapInScrollPane(Component comp){
        return new JScrollPane(comp);
    }

}
