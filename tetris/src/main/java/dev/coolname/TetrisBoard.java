package dev.coolname;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

public class TetrisBoard extends JPanel implements Runnable{
    
    public int ttt = 0;



    public static final int ROWS = 20;
    public static final int COLS = 10;
    public static final int CELL_SIZE = 30;


    public static final GridLayout layout  = new GridLayout(ROWS, COLS);

    public static final int DAS = 300;
    public static final int ARR = 10;
    public long lastARR = ARR;

    public long sleep = 1;
    public static final long FPS = 600;


    public ArrayList<Integer> keysHeld = new ArrayList<>();
    public HashMap<Integer, Long> keysHeldDuration = new HashMap<>();
    




    private int board[][] = new int[ROWS][COLS];
    private List<List<TetrisLayoutButton>> displayBoard = new ArrayList<>();
    private TetrisCurrentPiece currentPiece;
    private TetrisQueue queue;
    private TetrisHold hold;


    private Thread thread;
    private boolean running = false;




    public TetrisBoard(int height, int width) {
        super(layout);

        this.setFocusable(true);

        initBoard();
        initCells();

        addKeyListener();
        keysHeldDuration.put(Integer.valueOf(74), Long.MAX_VALUE);
        keysHeldDuration.put(Integer.valueOf(76), Long.MAX_VALUE);


    }

    public void start() {
        if(running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }


    @Override
    public void run() {
        while(running) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        update();
        }
    }
    

    private void update() {
        long time = System.nanoTime();
        long leftkeypressed = keysHeldDuration.get(Integer.valueOf(74));
        long rightkeypressed = keysHeldDuration.get(Integer.valueOf(76));
        if(time - leftkeypressed > DAS * 1000000) {
            lastARR -= sleep;
            if(lastARR < 0) {
                if(ARR == 0) {
                    
                currentPiece.changeCoord(-1, 0);
                currentPiece.changeCoord(-1, 0);
                currentPiece.changeCoord(-1, 0);
                currentPiece.changeCoord(-1, 0);
                currentPiece.changeCoord(-1, 0);
                currentPiece.changeCoord(-1, 0);
                currentPiece.changeCoord(-1, 0);
                currentPiece.changeCoord(-1, 0);
                currentPiece.changeCoord(-1, 0);
                currentPiece.changeCoord(-1, 0);
                currentPiece.changeCoord(-1, 0);
                }
                currentPiece.changeCoord(-1, 0);
                displayStuff();
                lastARR = ARR;
            }

        }
        if(time - rightkeypressed > DAS * 1000000) {
            lastARR -= sleep;
            if(lastARR < 0) {
                if(ARR == 0) {
                    
                currentPiece.changeCoord(1, 0);
                currentPiece.changeCoord(1, 0);
                currentPiece.changeCoord(1, 0);
                currentPiece.changeCoord(1, 0);
                currentPiece.changeCoord(1, 0);
                currentPiece.changeCoord(1, 0);
                currentPiece.changeCoord(1, 0);
                currentPiece.changeCoord(1, 0);
                currentPiece.changeCoord(1, 0);
                currentPiece.changeCoord(1, 0);
                currentPiece.changeCoord(1, 0);
                }
                currentPiece.changeCoord(-1, 0);
                displayStuff();
                lastARR = ARR;
            }

        }

        int a = 0;
        for(Integer i : keysHeld) {
            a += i;
        }
        ttt++;
    }

    public void setCurrentPieceRef(TetrisCurrentPiece currentPiece) {
        this.currentPiece = currentPiece;
    } 
    public void setQueueReference(TetrisQueue queue) {
        this.queue = queue;
    }
    public void setHoldReference(TetrisHold hold) {
        this.hold = hold;
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
                TetrisLayoutButton button = new TetrisLayoutButton(CELL_SIZE, CELL_SIZE, i, j);
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

    public void displayBoard() {

        for(int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLS; j++) {
                TetrisLayoutButton cell =  displayBoard.get(i).get(j);
                cell.setMino(board[i][j]);
                cell.updateMino();
            }
        }
    }

    public void displayCurrentPiece() {
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


    private void displayStuff() {
        displayBoard();
        displayCurrentPiece();
    }



    private void addKeyListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                //TODO: convert this to switcdh statements for the love of god holy moly
                if(code == 74) { //left
                    if(!keysHeld.contains(Integer.valueOf(code))) {
                        keysHeld.add(code);
                        keysHeldDuration.replace(Integer.valueOf(code), System.nanoTime());
                        System.out.println(keysHeldDuration.get(Integer.valueOf(code)));
                        currentPiece.changeCoord(-1, 0);
                        displayStuff();
                    }

                }
                if(code == 76) { //right
                    if(!keysHeld.contains(Integer.valueOf(code))) {
                        keysHeld.add(code);
                        keysHeldDuration.replace(Integer.valueOf(code), System.nanoTime());
                        System.out.println(keysHeldDuration.get(Integer.valueOf(code)));
                        currentPiece.changeCoord(-1, 0);
                        displayStuff();
                    }
                    currentPiece.changeCoord(1, 0);
                    displayStuff();
                }
                if(code == 85) { //up
                    if(!keysHeld.contains(Integer.valueOf(code))) {
                        keysHeld.add(code);
                    }
                    currentPiece.changeCoord(0, -1);
                    displayStuff();
                }
                if(code == 59) {//down
                    if(!keysHeld.contains(Integer.valueOf(code))) {
                        keysHeld.add(code);
                    }
                    currentPiece.changeCoord(0, 1);
                    displayStuff();
                }
                if(code == 65) {//ccw
                    if(!keysHeld.contains(Integer.valueOf(code))) {
                        keysHeld.add(code);
                    }
                    currentPiece.rotatePiece(1);
                    displayStuff();
                }
                
                if(code == 83) { //cw 
                    if(!keysHeld.contains(Integer.valueOf(code))) {
                        keysHeld.add(code);
                    }
                    currentPiece.rotatePiece(3);
                    displayStuff();
                }

                if(code == 68) {//hold
                    if(!keysHeld.contains(Integer.valueOf(code))) {
                        keysHeld.add(code);
                    }
                    currentPiece.holdPiece();
                    displayStuff();
                }

                if(code == 16) { //180 roatte
                    if(!keysHeld.contains(Integer.valueOf(code))) {
                        keysHeld.add(code);
                    }
                    currentPiece.rotatePiece(2);
                    displayStuff();

                }

                
                if(code == 75) {//hd
                    if(!keysHeld.contains(Integer.valueOf(code))) {
                        keysHeld.add(code);
                    }
                    currentPiece.insertPiece();
                    queue.addToQueue();
                    displayStuff();

                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                keysHeld.remove(Integer.valueOf(code));
                keysHeldDuration.replace(Integer.valueOf(code), Long.MAX_VALUE);
            }
        });
    }
}