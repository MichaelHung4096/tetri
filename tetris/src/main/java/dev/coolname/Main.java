package dev.coolname;


import java.awt.Component;
import java.awt.FlowLayout;

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
        frame.setLayout(new FlowLayout());

        TetrisHold hold = new TetrisHold();

        
        TetrisBoard board = new TetrisBoard(30, 30);
        TetrisCurrentPiece currentPiece = new TetrisCurrentPiece();

        board.setCurrentPieceRef(currentPiece);
        currentPiece.setBoardReference(board);

        currentPiece.setPiece(TetrisPiece.S_PIECE);


        board.insertPiece(TetrisPiece.L_PIECE.rotations[0], 0, 0);

        TetrisQueue queue = new TetrisQueue(30, 30);

        hold.setAlignmentY(Component.TOP_ALIGNMENT);
;
        frame.add(hold);
        frame.add(board);
        frame.add(queue);


        System.out.println(queue.getReadableQueue());



        init();
    }
}