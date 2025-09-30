package dev.coolname;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class TetrisBoard extends JPanel{
    public static final int ROWS = 20;
    public static final int COLS = 10;
    public static final GridLayout layout  = new GridLayout(ROWS, COLS);

    public static int temp = 0;
    //TODO: remove ^^^ later



    private int board[][] = new int[ROWS][COLS];
    private List<List<TetrisLayoutButton>> displayBoard = new ArrayList<>();
    private TetrisCurrentPiece currentPiece;



    public TetrisBoard(int height, int width) {
        super(layout);

        this.setFocusable(true);

        initBoard();
        initCells();

        addKeyListener();


    }

    public void setCurrentPieceRef(TetrisCurrentPiece currentPiece) {
        this.currentPiece = currentPiece;
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
            List<TetrisLayoutButton> row = new ArrayList<>();
            for(int j = 0; j < COLS; j++) {
                TetrisLayoutButton button = new TetrisLayoutButton(30, 30, i, j);
                button.setParentComponentRefernce(this);
                button.setText(Integer.toString(board[i][j]));
                button.setMino(board[i][j]);
                row.add(button);
                this.add(button);
            }
            displayBoard.add(row);
        }



    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
    public int[][] getBoard() {
        return board;
    }

    public void insertPiece(int[][] piece, int x, int y) {
        board = currentPiece.insertPiece(piece, x, y);
        displayBoard();

    }

    private void displayBoard() {

        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLS; j++) {
                TetrisLayoutButton cell =  displayBoard.get(i).get(j);
                cell.setMino(board[i][j]);
                cell.updateMino();
            }
        }
    }

    private void displayCurrentPiece() {
        int xCoord = currentPiece.getXCoord();
        int yCoord = currentPiece.getYCoord();
        TetrisPiece piece = currentPiece.getPiece();
        int[][] relevantInfo = piece.rotations[currentPiece.getRotation()];
        for(int i = 0; i < relevantInfo.length; i++) {
            for(int j = 0; j < relevantInfo[i].length; j++) {
                
                TetrisLayoutButton cell =  displayBoard.get(xCoord + i).get(yCoord + j);
                cell.setMino(relevantInfo[i][j]);
                cell.updateMino();
            }
        }
    }


    private void addKeyListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Key pressed parent: " + e.getKeyChar());
                if(e.getKeyChar() == 'w') {
                    currentPiece.setXCoord(currentPiece.getXCoord() - 1);
                    displayBoard();
                    displayCurrentPiece();
                }
                if(e.getKeyChar() == 's') {
                    currentPiece.setXCoord(currentPiece.getXCoord() + 1);
                    displayBoard();
                    displayCurrentPiece();
                }
                if(e.getKeyChar() == 'a') {
                    currentPiece.setYCoord(currentPiece.getYCoord() - 1);
                    displayBoard();
                    displayCurrentPiece();
                }
                if(e.getKeyChar() == 'd') {
                    currentPiece.setYCoord(currentPiece.getYCoord() + 1);
                    displayBoard();
                    displayCurrentPiece();
                }
                if(e.getKeyChar() == 'j') {
                    currentPiece.counterClockwiseRotation();
                    displayBoard();
                    displayCurrentPiece();
                }
                
                if(e.getKeyChar() == 'l') {
                    currentPiece.clockwiseRotation();
                    displayBoard();
                    displayCurrentPiece();
                }

            }
        });
    }
}