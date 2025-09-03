package dev.coolname;

import java.awt.Color;

public enum TetrisPiece {
    I_PIECE(1, Color.CYAN),
    L_PIECE(2, Color.ORANGE),
    J_PIECE(3, Color.BLUE),
    Z_PIECE(4, Color.RED),
    S_PIECE(5, Color.GREEN),
    O_PIECE(6, Color.YELLOW),
    T_PIECE(7, Color.PINK);

    public final int solidID;
    public final Color color;


    //TetrisPiece(int solidID, int ghostID, int[][][] rotations, int[][][] kicktable, Color color) {} <- what final thing should look like
    TetrisPiece(int solidID, Color color) {
        this.solidID = solidID;
        this.color = color;
    }
}
