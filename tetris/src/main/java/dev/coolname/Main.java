package dev.coolname;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;

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
        frame = new JFrame("haha biggie bock ride");
        frame.setLayout(new FlowLayout());



        TetrisFrame pan = new TetrisFrame();
        //pan.setVisible(true);
        Dimension d = new Dimension((TetrisFrame.COLS + 10) * TetrisFrame.CELL_SIZE, TetrisFrame.ROWS * TetrisFrame.CELL_SIZE);
        pan.setPreferredSize(d);
        pan.setBackground(Color.BLACK);
        frame.add(pan);

        pan.start();

        // TetrisFrame peter = new TetrisFrame();
        // peter.setPreferredSize(d);
        // peter.setBackground(Color.BLACK);
        // frame.add(peter);
        // peter.start();









        init();
    }
}