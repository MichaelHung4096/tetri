package dev.coolname;

//TODO: what i want is for current piece to handle the collisions and kicktabel, whereas board to display everytihng



public class TetrisCurrentPiece {
    private TetrisPiece piece;
    private int xCoord, yCoord;
    private int[][] localBoard = new int[TetrisBoard.ROWS][TetrisBoard.COLS];
    private TetrisBoard boardRef;
    private int rotation = 0;
    public TetrisCurrentPiece() {
    }

    public void setPiece(TetrisPiece piece) {
        this.piece = piece;
    }


    public TetrisPiece getPiece() {
        return piece;
    }

    public void setBoardReference(TetrisBoard boardRef) {
        this.boardRef = boardRef;
    } 


    public int[][] insertPiece(int[][] piece, int x, int y) {
        localBoard = boardRef.getBoard().clone();
        for(int i = 0; i < piece.length; i++) {
            for(int j = 0; j < piece[0].length; j++) {
                localBoard[x+i][y+j] = piece[i][j];
            }
        }
        return localBoard;

    }

    public void counterClockwiseRotation() {
        rotation = (rotation + 1) % 4;

    }
    
    public void clockwiseRotation() {
        rotation = (rotation + 3) % 4;

    }

    public int getRotation() {
        return rotation;
    }









    public int getXCoord() {
        return xCoord;
    }
    public void setXCoord(int newX) {
        xCoord = newX;
    }

    public int getYCoord() {
        return yCoord;

    }

    public void setYCoord(int newY) {
        yCoord = newY;
    }
}
