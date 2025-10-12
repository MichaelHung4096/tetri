package dev.coolname;

import java.awt.Component;

import javax.swing.JPanel;

public class TetrisHold extends JPanel {
    private TetrisPiece piece;
    private TetrisPieceDisplayer displayer;
    public TetrisHold() {
        initHold();
        this.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    }

    public void initHold() {
        displayer = new TetrisPieceDisplayer(30, 30);
        this.add(displayer);
    }

    public void setPiece(TetrisPiece piece) {
        this.piece = piece;
        displayer.updatePiece(piece);
    }
    public TetrisPiece getPiece() {
        return piece;
    }


}
