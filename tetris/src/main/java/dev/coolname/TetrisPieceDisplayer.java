package dev.coolname;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class TetrisPieceDisplayer extends JPanel{
    private static final int ROWS = 3;
    private static final int COLS = 4;
    private static final GridLayout layout = new GridLayout(ROWS, COLS);


    
    private int[][] board = new int[ROWS][COLS];
    private List<List<TetrisLayoutButton>> displayBoard = new ArrayList<>();
    private TetrisPiece piece;

    public TetrisPieceDisplayer(int width, int height) {
        super(layout);
        Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
        this.setBorder(redBorder);

        initCells();
        initBoard();

    }

    private void initCells() {
        for(int i = 0; i < ROWS; i++) {
            List<TetrisLayoutButton> row = new ArrayList<>();
            for(int j = 0; j < COLS; j++) {
                TetrisLayoutButton cell = new TetrisLayoutButton(30, 30, i, j);
                cell.setParentComponentRefernce(this);
                cell.setInteractivity(false);
                row.add(cell);
                this.add(cell);
            }
            displayBoard.add(row);
        }
    }

    private void initBoard() {
        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLS; j++) {
                board[i][j] = 0;
            }
        }

    }

    public void setPiece(TetrisPiece piece) {
        this.piece = piece;
    }
    
    public TetrisPiece getPiece() {
        return piece;
    }

    private void updateBoardData() {
        initBoard();
        int[][] displayData = piece.rotations[0];
        for(int i = 0; i < ROWS; i++) {
            if(i >= displayData.length) {continue;}
            for(int j = 0; j < COLS; j++) {
                if(j >= displayData[i].length) {continue;}
                    board[i][j] = displayData[i][j];
            }
        }
    }

    private void displayPiece() {

        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLS; j++) {
                TetrisLayoutButton cell =  displayBoard.get(i).get(j);
                cell.setMino(board[i][j]);
                cell.updateMino();
            }
        }
    }

    public void updatePiece(TetrisPiece piece) {
        setPiece(piece);
        updateBoardData();
        displayPiece();
    }
}

