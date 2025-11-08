package dev.coolname;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
public class Main {
    private static JFrame frame;

    private static void init() {
        frame.setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE);
            
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        frame = new JFrame("My First JFrame");
        frame.setLayout(new FlowLayout());



        TetrisFrame pan = new TetrisFrame();
        //pan.setVisible(true);
        Dimension d = new Dimension((TetrisBoard.COLS + 10) * TetrisBoard.CELL_SIZE, TetrisBoard.ROWS * TetrisBoard.CELL_SIZE);
        pan.setPreferredSize(d);
        pan.setBackground(Color.BLACK);
        frame.add(pan);

        pan.start();


        
        // TetrisBoard board = new TetrisBoard(TetrisBoard.CELL_SIZE, TetrisBoard.CELL_SIZE);
        // TetrisCurrentPiece currentPiece = new TetrisCurrentPiece();
        // TetrisHold hold = new TetrisHold();
        // TetrisQueue queue = new TetrisQueue(TetrisBoard.CELL_SIZE, TetrisBoard.CELL_SIZE);




        // board.setCurrentPieceRef(currentPiece);
        // board.setQueueReference(queue);
        // board.setHoldReference(hold);

        // currentPiece.setBoardReference(board);
        // currentPiece.setQueueReference(queue);
        // currentPiece.setHoldReference(hold);
        
        // currentPiece.updateCurrentPiece();

        // board.start();
        
        // queue.syncQueue();
        // frame.add(hold);
        // frame.add(board);
        // frame.add(queue);











        init();
    }
}