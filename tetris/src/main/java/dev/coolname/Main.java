package dev.coolname;


import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

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

        
        TetrisBoard board = new TetrisBoard(30, 30);
        TetrisCurrentPiece currentPiece = new TetrisCurrentPiece();
        TetrisHold hold = new TetrisHold();
        TetrisQueue queue = new TetrisQueue(30, 30);


        board.setCurrentPieceRef(currentPiece);
        currentPiece.setBoardReference(board);
        currentPiece.setQueueReference(queue);

        currentPiece.setPiece(TetrisPiece.S_PIECE);


        board.insertPiece();

        

;
        frame.add(hold);
        frame.add(board);
        frame.add(queue);

        queue.syncQueue();










        init();
    }
}