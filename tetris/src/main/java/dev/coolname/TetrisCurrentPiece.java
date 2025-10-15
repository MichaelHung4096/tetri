package dev.coolname;

import java.util.ArrayList;

public class TetrisCurrentPiece {
    private TetrisPiece piece;
    private int xCoord = 5;
    private int yCoord = 5;
    private int[][] localBoard = new int[TetrisBoard.ROWS][TetrisBoard.COLS];
    private TetrisBoard board;
    private int rotation = 0;
    private int prevRotation;
    private Integer kicktableKey;
    private TetrisQueue queue;
    private TetrisHold hold;


    public TetrisCurrentPiece() {
    }

    public void setPiece(TetrisPiece piece) {
        this.piece = piece;
    }


    public TetrisPiece getPiece() {
        return piece;
    }

    public void setBoardReference(TetrisBoard boardRef) {
        this.board = boardRef;
    } 
    public void setQueueReference(TetrisQueue queueRef) {
        this.queue = queueRef;
    }
    public void setHoldReference(TetrisHold holdRef) {
        this.hold = holdRef;
    }


    public int[][] insertPiece() {
        localBoard = board.getBoard().clone();
        int[][] data = piece.rotations[rotation];


        while(true) {
            if(!changeCoord(0, 1)) break;

        }

        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data.length; j++) {
                if(data[i][j] == 0) continue;
                localBoard[yCoord+i][xCoord+j] = data[i][j];
            }
        }


        clearLines();
        resetPieceStuff();
        updateCurrentPiece();

        return localBoard;

    }

    private void clearLines() {
        localBoard = board.getBoard().clone();
                // Start from bottom row and go upward
        for (int r = TetrisBoard.ROWS - 1; r >= 0; r--) {
            if (isLineFull(r)) {
                removeLine(r);
                r++; // Recheck the same row index (since rows above moved down)
            }
        }
    }

    private boolean isLineFull(int row) {
        for (int c = 0; c < TetrisBoard.COLS; c++) {
            if (localBoard[row][c] == 0) return false;
        }
        return true;
    }


    private void removeLine(int line) {
        // Move every row above `line` one step down
        for (int r = line; r > 0; r--) {
            System.arraycopy(localBoard[r - 1], 0, localBoard[r], 0, TetrisBoard.COLS);
        }

        // Clear the top row
        for (int c = 0; c < TetrisBoard.COLS; c++) {
            localBoard[0][c] = 0;
        }
    }




    private void resetPieceStuff() {

        xCoord = 3;
        yCoord = 0;
        rotation = 0;
        prevRotation = 0;
    }

    public void updateCurrentPiece() { //from queeuue right now
        piece = queue.removeFirstPiece();
        
    }

    public void holdPiece() {
        TetrisPiece heldPiece = hold.getPiece();
        if(heldPiece == null) {
            System.out.println("no piece in hold");
            hold.setPiece(piece);
            updateCurrentPiece();
        }
        else {
            hold.setPiece(piece);
            setPiece(heldPiece);
        }
        
        resetPieceStuff();
        board.displayBoard();
        board.displayCurrentPiece();
    }

    public void rotatePiece(int rotate) {
        int tempPrevRot = prevRotation;
        prevRotation = rotation;
        rotation = (rotation + rotate) % 4;
        kicktableKey = prevRotation*10 + rotation;
        int[][] kicktable = piece.kicktable.get(kicktableKey);
        


        for(int i = 0; i < kicktable.length; i++) { 
            if (changeCoord(kicktable[i][0], kicktable[i][1])) {
                return;
            }     
        }
        
        
        rotation = (rotation + (4-rotate)) % 4;
        prevRotation = tempPrevRot;
    }

    public int getRotation() {
        return rotation;
    }


    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;

    }


    public boolean changeCoord(int xChange, int yChange) {
        xCoord += xChange;
        yCoord += yChange;
        if(isColliding()) {
            xCoord -= xChange;
            yCoord -= yChange;
            return false;
        }
        return true;
    }

    private boolean isColliding() {
        int[][] data =piece.rotations[rotation];
        localBoard = board.getBoard();

        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data.length; j++) {
                //bounds collision detection
                if(j + xCoord < 0) {

                    if(data[i][j] != 0) {
                       // System.out.println("left: fck u");
                        return true;
                    }
                    
                    if(data[i][j] == 0) {
                       // System.out.println("left: gotchu");
                    }
                }

                if(j + xCoord >= TetrisBoard.COLS) {
                    if(data[i][j] != 0) {
                     //   System.out.println("right: fck u");
                        return true;
                    }
                    
                    if(data[i][j] == 0) {
                     //   System.out.println("right: gotchu");
                    }
                }

                if(i + yCoord >= TetrisBoard.ROWS) {
                    if(data[i][j] != 0) {
                       // System.out.println("down:fck u");
                        return true;
                    }
                    
                    if(data[i][j] == 0) {
                      //  System.out.println("down: gotchu");
                    }
                }

                if(i + yCoord < 0) {
                    if(data[i][j] != 0) {
                       // System.out.println("up:fck u");
                        return true;
                    }
                    
                    if(data[i][j] == 0) {
                       // System.out.println("up:gotchu");
                    }
                }


                //block collision detection
                if(data[i][j] != 0) {
                    if(localBoard[yCoord + i][xCoord + j] != 0) {
                       // System.out.println("WEEEWOOOWOOOWOWOWWOWOWOWOWOWOW");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
