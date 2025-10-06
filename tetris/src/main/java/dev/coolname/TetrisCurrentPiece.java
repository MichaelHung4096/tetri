package dev.coolname;

//TODO: what i want is for current piece to handle the collisions and kicktabel, whereas board to display everytihng



public class TetrisCurrentPiece {
    private TetrisPiece piece;
    private int xCoord = 5;
    private int yCoord = 5;
    private int[][] localBoard = new int[TetrisBoard.ROWS][TetrisBoard.COLS];
    private TetrisBoard boardRef;
    private int rotation = 0;
    private TetrisQueue queueRef;
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
    public void setQueueReference(TetrisQueue queueRef) {
        this.queueRef = queueRef;
    }


    public int[][] insertPiece() {
        localBoard = boardRef.getBoard().clone();

        for(int i = 0; i < piece.rotations[rotation].length; i++) {
            for(int j = 0; j < piece.rotations[rotation].length; j++) {
                localBoard[xCoord+i][yCoord+j] = piece.rotations[rotation][i][j];
            }
        }

        resetPieceStuff();
        updateCurrentPiece();

        return localBoard;

    }

    private void resetPieceStuff() {

        xCoord = 0;
        yCoord = 0;
        rotation = 0;
    }

    private void updateCurrentPiece() { //from queeuue right now
        piece = queueRef.removeFirstPiece();
        
    }

    public void counterClockwiseRotation() {
        rotation = (rotation + 1) % 4;

    }

    public void OneEightyRotation() {
        rotation = (rotation + 2) % 4;
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
