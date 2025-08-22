package dev.coolname;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
public class Main {
    private static JFrame frame;

    private static void init() {
                // Close operation
        frame.setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE);

        // Make the frame visible
        frame.setVisible(true);

        frame.pack();
    }
    public static void main(String[] args) {
        frame = new JFrame("My First JFrame");





        int[][] board = new int[20][10];
        for(int i = 0; i < 20; i++) {
            for(int j = 0; j < 10; j++) {
                board[i][j] = i*100 + j;
            }
        }

        GridLayout layout = new GridLayout(20,10);
        JPanel panel = new JPanel(layout);
        Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        for(int i = 0; i < 200; i++) {
            JButton button = new JButton(Integer.toString(i));
            button.setBackground(Color.red);
            button.setPreferredSize(new Dimension(30, 30));
            button.setBorder(border);
            panel.add(button);
        }

        frame.add(panel);

        init();
    }
}