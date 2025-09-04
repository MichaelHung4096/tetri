package dev.coolname;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class TetrisHold extends JPanel {
    public TetrisHold() {
        initHold();
    }

    public void initHold() {
        GridLayout layout = new GridLayout(4,4);
        this.setLayout(layout);
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                TetrisNoninteractiveButton button = new TetrisNoninteractiveButton(30, 30, i, j);
                this.add(button);
            }
        }
    }
}
