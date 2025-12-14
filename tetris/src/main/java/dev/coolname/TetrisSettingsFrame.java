package dev.coolname;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

class SettingsButton extends JButton {
    public SettingsButton(String t) {
        super(t);
    }
}


public class TetrisSettingsFrame extends JPanel{
    TetrisSettings settings;
    public TetrisSettingsFrame(TetrisSettings settings) {
        this.settings = settings;
        init();
    }


    public String str(int[] list) { //there has to be a one line solution to this 

        ArrayList<String> keynames = new ArrayList<>();
        
        for(int i : list) {
            keynames.add(KeyEvent.getKeyText(i));
        }
        return String.join(", ", keynames);
    }


    public void init() {

        //TODO: add SettingsSettingsButton to implement editting contorlss
        SettingsButton left = new SettingsButton("left: " + str(settings.MOVE_LEFT));
        this.add(left);

        SettingsButton right = new SettingsButton("right: " + str(settings.MOVE_RIGHT));
        this.add(right);

        SettingsButton soft = new SettingsButton("softdrop: " + str(settings.SOFT_DROP));
        this.add(soft);

        SettingsButton hard = new SettingsButton("harddrop: " + str(settings.HARD_DROP));
        this.add(hard);

        SettingsButton cw = new SettingsButton("clockwise rotation: " + str(settings.ROTATE_CW));
        this.add(cw);

        SettingsButton ccw = new SettingsButton("counterclockwise rotation: " + str(settings.ROTATE_CCW));
        this.add(ccw);

        SettingsButton r180 = new SettingsButton("180 rotation: " + str(settings.ROTATE_180));
        this.add(r180);

        SettingsButton hold = new SettingsButton("hold: " + str(settings.HOLD));
        this.add(hold);

        SettingsButton reset = new SettingsButton("reset: " + str(settings.RESET));
        this.add(reset);

        SettingsButton ghost_action = new SettingsButton("ghost: " + str(settings.GHOST_ACTION));
        this.add(ghost_action);


    }


}
