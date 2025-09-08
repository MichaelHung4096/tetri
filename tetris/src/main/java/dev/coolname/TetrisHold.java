package dev.coolname;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class TetrisHold extends JPanel {
    public TetrisHold() {
        initHold();
    }

    public void initHold() {
        TetrisPieceDisplayer displayer = new TetrisPieceDisplayer(30, 30);
        this.add(displayer);
    }
}
