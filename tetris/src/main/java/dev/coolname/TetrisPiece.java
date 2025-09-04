package dev.coolname;

import java.awt.Color;

public enum TetrisPiece {
    I_PIECE(1, 'I', Color.CYAN, new int[][][] {
                                                {{0,0,0,0},
                                                 {0,0,0,0},
                                                 {1,1,1,1},
                                                 {0,0,0,0}}
                                                }),
    L_PIECE(2, 'L', Color.ORANGE, new int[][][] {
                                                    {{0,0,0},
                                                     {2,0,0},
                                                     {2,2,2}}
                                                }),
    J_PIECE(3, 'J', Color.BLUE, new int[][][] {
                                                  {{0,0,0},
                                                   {0,0,3},
                                                   {3,3,3}}
                                                }),
    Z_PIECE(4, 'Z', Color.RED, new int[][][] {
                                                    {{0,0,0},
                                                     {0,4,4},
                                                     {4,4,0}}
                                                }),
    S_PIECE(5, 'S', Color.GREEN, new int[][][] {
                                                    {{0,0,0},
                                                     {5,5,0},
                                                     {0,5,5}}
                                                }),
    O_PIECE(6, 'O', Color.YELLOW, new int[][][] {
                                                    {{6,6},
                                                     {6,6}}
                                                    }),
    T_PIECE(7, 'T', Color.PINK, new int[][][] {
                                                    {{0,0,0},
                                                     {0,7,0},
                                                     {7,7,7}}
                                                    });

    public final int solidID;
    public final Color color;
    public final int[][][] rotations;
    public final char charID;


    //TetrisPiece(int solidID, int ghostID, int[][][] rotations, int[][][] kicktable, Color color) {} <- what final thing should look like
    TetrisPiece(int solidID, char charID, Color color, int[][][] rotations) {
        this.solidID = solidID;
        this.color = color;
        this.rotations = rotations;
        this.charID = charID;
    }
}
