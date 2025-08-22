package dev.coolname;

import java.awt.Dimension;

import javax.swing.JButton;

public class TetrisLayoutButton extends JButton{
    private int row;
    private int column;

    public TetrisLayoutButton(int width, int height, int row, int column) {
        this.setPreferredSize(new Dimension(width, height));
        this.row = row;
        this.column = column;
    }

    private void listener() {
    }
}
