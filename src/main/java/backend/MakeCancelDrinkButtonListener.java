package backend;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MakeCancelDrinkButtonListener implements ActionListener {

    private ClientFrame frame;
    private JButton button;

    public MakeCancelDrinkButtonListener(ClientFrame frame, JButton button){
        this.frame = frame;
        this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(button.getText().equals(ClientFrame.MAKE_DRINK)){
            frame.makingDrink();
        } else if ( button.getText().equals(ClientFrame.FINISHED_MAKING) ){
            frame.finishMaking();
        }
    }
}
