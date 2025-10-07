package dev.coolname;

//TODO: what i want is for current piece to handle the collisions and kicktabel, whereas board to display everytihng



public class TetrisCurrentPiece {
    private TetrisPiece piece;
    private int xCoord = 5;
    private int yCoord = 5;
    private int[][] localBoard = new int[TetrisBoard.ROWS][TetrisBoard.COLS];
    private TetrisBoard boardRef;
    private int rotation = 0;
    private TetrisQueue queue;
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
        this.queue = queueRef;
    }


    public int[][] insertPiece() {
        localBoard = boardRef.getBoard().clone();
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

        resetPieceStuff();
        updateCurrentPiece();

        return localBoard;

    }

    private void resetPieceStuff() {

        xCoord = 2;
        yCoord = 0;
        rotation = 0;
    }

    private void updateCurrentPiece() { //from queeuue right now
        piece = queue.removeFirstPiece();
        
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
        localBoard = boardRef.getBoard();

        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data.length; j++) {
                //bounds collision detection
                if(j + xCoord < 0) {

                    if(data[i][j] != 0) {
                        System.out.println("left: fck u");
                        return true;
                    }
                    
                    if(data[i][j] == 0) {
                        System.out.println("left: gotchu");
                    }
                }

                if(j + xCoord >= TetrisBoard.COLS) {
                    if(data[i][j] != 0) {
                        System.out.println("right: fck u");
                        return true;
                    }
                    
                    if(data[i][j] == 0) {
                        System.out.println("right: gotchu");
                    }
                }

                if(i + yCoord >= TetrisBoard.ROWS) {
                    if(data[i][j] != 0) {
                        System.out.println("down:fck u");
                        return true;
                    }
                    
                    if(data[i][j] == 0) {
                        System.out.println("down: gotchu");
                    }
                }

                if(i + yCoord < 0) {
                    if(data[i][j] != 0) {
                        System.out.println("up:fck u");
                        return true;
                    }
                    
                    if(data[i][j] == 0) {
                        System.out.println("up:gotchu");
                    }
                }


                //block collision detection
                if(data[i][j] != 0) {
                    if(localBoard[yCoord + i][xCoord + j] != 0) {
                        System.out.println("WEEEWOOOWOOOWOWOWWOWOWOWOWOWOW");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
