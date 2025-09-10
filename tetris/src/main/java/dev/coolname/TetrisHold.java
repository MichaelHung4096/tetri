package dev.coolname;

import java.awt.Component;

import javax.swing.JPanel;

public class TetrisHold extends JPanel {
    public TetrisHold() {
        initHold();
        this.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    }

    public void initHold() {
        TetrisPieceDisplayer displayer = new TetrisPieceDisplayer(30, 30);
        this.add(displayer);
    }
}
