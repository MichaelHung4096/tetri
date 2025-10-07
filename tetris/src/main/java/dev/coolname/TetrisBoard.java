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



    private int board[][] = new int[ROWS][COLS];
    private List<List<TetrisLayoutButton>> displayBoard = new ArrayList<>();
    private TetrisCurrentPiece currentPiece;
    private TetrisQueue queue;



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
    public void setQueueReference(TetrisQueue queue) {
        this.queue = queue;
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

    public void insertPiece() {
        board = currentPiece.insertPiece();
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
                if(relevantInfo[i][j] == 0) continue;
                
                TetrisLayoutButton cell =  displayBoard.get(yCoord + i).get(xCoord + j);
                cell.setMino(relevantInfo[i][j]);
                cell.updateMino();
            }
        }
    }


    private void addKeyListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //TODO: convert this to switcdh statements for the love of god holy moly
                if(e.getKeyChar() == 'j') { //left
                    currentPiece.changeCoord(-1, 0);
                    displayBoard();
                    displayCurrentPiece();
                }
                if(e.getKeyChar() == 'l') { //right
                    currentPiece.changeCoord(1, 0);
                    displayBoard();
                    displayCurrentPiece();
                }
                if(e.getKeyChar() == 'u') { //up
                    currentPiece.changeCoord(0, -1);
                    displayBoard();
                    displayCurrentPiece();
                }
                if(e.getKeyChar() == ';') {//down
                    currentPiece.changeCoord(0, 1);
                    displayBoard();
                    displayCurrentPiece();
                }
                if(e.getKeyChar() == 'a') {
                    currentPiece.counterClockwiseRotation();
                    displayBoard();
                    displayCurrentPiece();
                }
                
                if(e.getKeyChar() == 's') {
                    currentPiece.clockwiseRotation();
                    displayBoard();
                    displayCurrentPiece();
                }

                if(e.getKeyCode() == 16) {
                    currentPiece.OneEightyRotation();
                    displayBoard();
                    displayCurrentPiece();

                }

                
                if(e.getKeyCode() == 75) {
                    currentPiece.insertPiece();
                    displayBoard();
                    displayCurrentPiece();

                }

            }
        });
    }
}