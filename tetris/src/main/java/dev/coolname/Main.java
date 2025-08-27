package dev.coolname;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
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

        init();
    }
}