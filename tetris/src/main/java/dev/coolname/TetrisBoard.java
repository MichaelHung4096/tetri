package dev.coolname;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class TetrisBoard extends JPanel{
    public static final int ROWS = 20;
    public static final int COLS = 10;
    public static final GridLayout layout  = new GridLayout(ROWS, COLS);
    private int board[][] = new int[ROWS][COLS];
    public TetrisBoard(int height, int width) {
        super(layout);

        initBoard();
        initCells();


    }

    private void initBoard() {
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLS; j++) {
                board[i][j] = 0;
            }
        }

    }

    private void initCells() {
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLS; j++) {
                TetrisInteractiveButton button = new TetrisInteractiveButton(30, 30, i, j);
                button.setText(Integer.toString(board[i][j]));
                button.setMino(board[i][j]);
                this.add(button);
            }
        }
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
    public int[][] getBoard() {
        return board;
    }
}
