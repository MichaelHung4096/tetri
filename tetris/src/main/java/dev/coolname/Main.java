package dev.coolname;


import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
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

        TetrisQueue queue = new TetrisQueue(30, 30);
;
        frame.add(hold);
        frame.add(board);
        frame.add(queue);


        System.out.println(queue.getReadableQueue());



        init();
    }
}