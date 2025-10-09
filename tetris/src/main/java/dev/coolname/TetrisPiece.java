package dev.coolname;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public enum TetrisPiece {
    
    I_PIECE(1, 'I', Color.CYAN, new int[][][] {
                                                {{0,0,0,0},
                                                 {0,0,0,0},
                                                 {1,1,1,1},
                                                 {0,0,0,0}},

                                                 
                                                {{0,1,0,0},
                                                 {0,1,0,0},
                                                 {0,1,0,0},
                                                 {0,1,0,0}},

                                                 
                                                {{0,0,0,0},
                                                 {1,1,1,1},
                                                 {0,0,0,0},
                                                 {0,0,0,0}},

                                                 
                                                {{0,0,1,0},
                                                 {0,0,1,0},
                                                 {0,0,1,0},
                                                 {0,0,1,0}}


                                                }, TetrisPiece.createJLSTZMap()),
    J_PIECE(2, 'J', Color.BLUE, new int[][][] {
                                                    {{2,0,0},
                                                     {2,2,2},
                                                     {0,0,0}},

                                                    {{0,2,0},
                                                     {0,2,0},
                                                     {2,2,0}},

                                                    {{0,0,0},
                                                     {2,2,2},
                                                     {0,0,2}},

                                                    {{0,2,2},
                                                     {0,2,0},
                                                     {0,2,0}}
                                                }, TetrisPiece.createJLSTZMap()),
    L_PIECE(3, 'L', Color.ORANGE, new int[][][] {
                                                  {{0,0,3},
                                                   {3,3,3},
                                                   {0,0,0}},

                                                  {{3,3,0},
                                                   {0,3,0},
                                                   {0,3,0}},

                                                  {{0,0,0},
                                                   {3,3,3},
                                                   {3,0,0}},

                                                  {{0,3,0},
                                                   {0,3,0},
                                                   {0,3,3}},
                                                   
                                                }, TetrisPiece.createJLSTZMap()),
    S_PIECE(4, 'S', Color.GREEN, new int[][][] {
                                                    {{0,0,0},
                                                     {0,4,4},
                                                     {4,4,0}},

                                                    {{4,0,0},
                                                     {4,4,0},
                                                     {0,4,0}},

                                                    {{0,4,4},
                                                     {4,4,0},
                                                     {0,0,0}},

                                                    {{0,4,0},
                                                     {0,4,4},
                                                     {0,0,4}}
                                                }, TetrisPiece.createJLSTZMap()),
    Z_PIECE(5, 'Z', Color.RED, new int[][][] {
                                                    {{0,0,0},
                                                     {5,5,0},
                                                     {0,5,5}},

                                                     {{0,5,0},
                                                     {5,5,0},
                                                     {5,0,0}},

                                                    {
                                                     {5,5,0},
                                                     {0,5,5},
                                                     {0,0,0}},

                                                      {{0,0,5},
                                                     {0,5,5},
                                                     {0,5,0}},                                                    


                                                }, TetrisPiece.createJLSTZMap()),
    O_PIECE(6, 'O', Color.YELLOW, new int[][][] {
                                                    {{6,6},
                                                     {6,6}},
                                                     {{6,6},
                                                     {6,6}},
                                                     {{6,6},
                                                     {6,6}},
                                                     {{6,6},
                                                     {6,6}}
                                                    }, TetrisPiece.createJLSTZMap()),
    T_PIECE(7, 'T', Color.PINK, new int[][][] {
                                                    {{0,7,0},
                                                     {7,7,7},
                                                     {0,0,0}},

                                                     
                                                    {{0,7,0},
                                                     {7,7,0},
                                                     {0,7,0}},
                                                     
                                                    {{0,0,0},
                                                     {7,7,7},
                                                     {0,7,0}},

                                                     
                                                    {{0,7,0},
                                                     {0,7,7},
                                                     {0,7,0}}

                                                     
                                                    }, TetrisPiece.createJLSTZMap());

    public final int solidID;
    public final Color color;
    public final int[][][] rotations;
    public final char charID;
    public final HashMap<Integer, int[][]> kicktable;

    
    private static  HashMap<Integer, int[][]> createJLSTZMap() {
        HashMap<Integer, int[][]> map = new HashMap<>();
        map.put(3, new int[][]{{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}});
        map.put(30, new int[][]{{0,0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}});

        map.put(32, new int[][]{{0,0}, {1,0}, {1,1}, {0, -2}, {1, -2}});
        map.put(23, new int[][]{{0,0}, {-1,0}, {-1,-1}, {0, 2}, {-1, 2}});

        map.put(21, new int[][]{{0,0}, {1,0}, {1, -1}, {0, 2}, {1, 2}});
        map.put(12, new int[][]{{0,0}, {-1,0}, {-1, 1}, {0, -2}, {-1, -2}});

        map.put(10, new int[][]{{0,0}, {-1, 0}, {-1,1}, {0, -2}, {-1, -2}});
        map.put(1, new int[][]{{0,0}, {1, 0}, {1,-1}, {0, 2}, {1, 2}});

        map.put(2, new int[][]{{0,0}});
        map.put(20, new int[][]{{0,0}});
        map.put(13, new int[][]{{0,0}});
        map.put(31, new int[][]{{0,0}});

        return map;
    }

    private HashMap<Integer, int[][]> createOMap() { return createJLSTZMap();}

    private HashMap<Integer, int[][]> createIMap() { return createJLSTZMap();}


    //TetrisPiece(int solidID, int ghostID, int[][][] rotations, int[][][] kicktable, Color color) {} <- what final thing should look like
    TetrisPiece(int solidID, char charID, Color color, int[][][] rotations, HashMap<Integer, int[][]> kicktable)  {
        this.solidID = solidID;
        this.color = color;
        this.rotations = rotations;
        this.charID = charID;
        this.kicktable = kicktable;


    }
}
