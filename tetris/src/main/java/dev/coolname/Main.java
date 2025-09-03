package dev.coolname;


import javax.swing.JFrame;
public class Main {
    private static JFrame frame;

    private static void init() {
        frame.setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
    }
    public static void main(String[] args) {
        frame = new JFrame("My First JFrame");

        TetrisBoard board = new TetrisBoard(30, 39);

        frame.add(board);



        int[] queue = new int[7];
        init();

        TetrisPiece[] bag = TetrisPiece.values();
    }
}