package dev.coolname;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TetrisSettingsFrame extends JPanel{
    TetrisSettings settings;
    public TetrisSettingsFrame(TetrisSettings settings) {
        this.settings = settings;
        init();
    }
    public String str(int[] list) {
        StringBuilder sb = new StringBuilder();
        for(int i : list) {
            sb.append(i + " ");
        }
        return sb.toString();
    }
    public void init() {
        JButton button = new JButton("left " + str(settings.MOVE_LEFT));
        this.add(button);
        //TODO do all other controsl
    }


}
