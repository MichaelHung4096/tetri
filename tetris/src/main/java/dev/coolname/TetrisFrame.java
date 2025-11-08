package dev.coolname;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


import javax.swing.JPanel;

public class TetrisFrame extends JPanel implements Runnable{

    private Thread thread;
    private boolean running = false;
    public long sleep = 1;

    public static final int ROWS = 20;
    public static final int COLS = 10;
    public static final int CELL_SIZE = 30;

    public static int DAS = 65;
    public int DAS_timer = DAS;
    public static int ARR = 0;
    public int ARR_timer = ARR;
    //public static int DAS_DIRECTION = 0;

    public static final Color[] colors = { Color.BLACK, Color.CYAN, Color.BLUE, Color.ORANGE, Color.GREEN, Color.RED,
            Color.YELLOW, Color.PINK };

    public static final int BOARD_XOFFSET = 5 * CELL_SIZE;
    public static final int BOARD_YOFFSET = 0;
    private int board[][] = new int[ROWS][COLS];

    public static final int HOLD_XOFFSET = 0;
    public static final int HOLD_YOFFSET = 0;
    private TetrisPiece hold;

    public static final int QUEUE_DISPLAY_LENGTH = 5;
    public static final int QUEUE_XOFFSET = 16 * CELL_SIZE;
    private static final TetrisPiece[] all_pieces = TetrisPiece.values();
    public static final int[] QUEUE_YOFFSET = {0, 3 * CELL_SIZE, 6 * CELL_SIZE, 9 * CELL_SIZE, 12 * CELL_SIZE };
    public ArrayList<TetrisPiece> queue = new ArrayList<>();

    private TetrisPiece currentPiece;
    private int xCoord = 3;
    private int yCoord = 0;
    private int rotation = 0;
    private int prevRotation;
    private Integer kicktableKey;

    private ArrayList<Integer> keysHeld = new ArrayList<>();
    private HashMap<Integer, Long> keysHeldDuration = new HashMap<>();

    public int lines = 0;
    

    public TetrisFrame() {
        initBoard();
        addKeyListener();
        setFocusable(true);
        requestFocusInWindow();
        addToQueue();
        addToQueue();
        addToQueue();
        addToQueue();
        addToQueue();

        updateCurrentPiece(); 
    }


    //BOARD CODE
    private void initBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = 0;
            }
        }

    }
    private void clearLines() {
                // Start from bottom row and go upward
        for (int r = ROWS - 1; r >= 0; r--) {
            if (isLineFull(r)) {
                removeLine(r);
                r++; // Recheck the same row index (since rows above moved down)
            }
        }
    }

    private boolean isLineFull(int row) {
        for (int c = 0; c < COLS; c++) {
            if (board[row][c] == 0) return false;
        }
        return true;
    }


    private void removeLine(int line) {
        // Move every row above `line` one step down
        for (int r = line; r > 0; r--) {
            System.arraycopy(board[r - 1], 0, board[r], 0, TetrisBoard.COLS);
        }

        // Clear the top row
        for (int c = 0; c < TetrisBoard.COLS; c++) {
            board[0][c] = 0;
        }

        lines++;
    }









    //HOLD CODE
    public void setHoldPiece(TetrisPiece piece) {
        hold = piece;
    }












    // QUEUE CODE
    public void addToQueue() {
        int length = queue.size();
        while (length <= 2 * QUEUE_DISPLAY_LENGTH) {
            ArrayList<TetrisPiece> bag = generateBag();
            queue.addAll(bag);
            length = queue.size();
        }
    }

    private ArrayList<TetrisPiece> generateBag() {

        ArrayList<TetrisPiece> bag = new ArrayList<>(Arrays.asList(all_pieces));
        Collections.shuffle(bag);

        return bag;
    }

    public TetrisPiece removeFirstPiece() {
        // System.out.println(queue.get(0));
        TetrisPiece newCurrentPiece = queue.remove(0);
        // System.out.println(queue.get(0));
        return newCurrentPiece;
    }

    // CURRENT PIECE CODE
    private void resetPieceStuff() {

        xCoord = 3;
        yCoord = 0;
        rotation = 0;
        prevRotation = 0;
    }

    public void updateCurrentPiece() { // from queeuue right now
        currentPiece = removeFirstPiece();
    }

    public void rotatePiece(int rotate) {
        int tempPrevRot = prevRotation;
        prevRotation = rotation;
        rotation = (rotation + rotate) % 4;
        kicktableKey = prevRotation * 10 + rotation;
        int[][] kicktable = currentPiece.kicktable.get(kicktableKey);

        for (int i = 0; i < kicktable.length; i++) {
            if (changeCoord(kicktable[i][0], kicktable[i][1])) {
                return;
            }
        }

        rotation = (rotation + (4 - rotate)) % 4;
        prevRotation = tempPrevRot;
    }

    public boolean changeCoord(int xChange, int yChange) {
        // System.out.println(System.currentTimeMillis());
        xCoord += xChange;
        yCoord += yChange;
        if (isColliding()) {
            xCoord -= xChange;
            yCoord -= yChange;
            return false;
        }
        return true;
    }

    private boolean isColliding() {
        int[][] data = currentPiece.rotations[rotation];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                // bounds collision detection
                if (j + xCoord < 0) {

                    if (data[i][j] != 0) {
                        // System.out.println("left: fck u");
                        return true;
                    }

                    if (data[i][j] == 0) {
                        // System.out.println("left: gotchu");
                    }
                }

                if (j + xCoord >= TetrisBoard.COLS) {
                    if (data[i][j] != 0) {
                        // System.out.println("right: fck u");
                        return true;
                    }

                    if (data[i][j] == 0) {
                        // System.out.println("right: gotchu");
                    }
                }

                if (i + yCoord >= TetrisBoard.ROWS) {
                    if (data[i][j] != 0) {
                        // System.out.println("down:fck u");
                        return true;
                    }

                    if (data[i][j] == 0) {
                        // System.out.println("down: gotchu");
                    }
                }

                if (i + yCoord < 0) {
                    if (data[i][j] != 0) {
                        // System.out.println("up:fck u");
                        return true;
                    }

                    if (data[i][j] == 0) {
                        // System.out.println("up:gotchu");
                    }
                }

                // block collision detection
                if (data[i][j] != 0) {
                    if (board[yCoord + i][xCoord + j] != 0) {
                        // System.out.println("WEEEWOOOWOOOWOWOWWOWOWOWOWOWOW");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void holdPiece() {
        if(hold == null) {
            System.out.println("no piece in hold");
            hold = currentPiece;
            updateCurrentPiece();
        }
        else {
            TetrisPiece temp = currentPiece;
            currentPiece = hold;
            hold = temp;
        }
        
        resetPieceStuff();
    }

    public void insertPiece() {
        int[][] data = currentPiece.rotations[rotation];


        while(true) {
            if(!changeCoord(0, 1)) break;

        }

        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data.length; j++) {
                if(data[i][j] == 0) continue;
                board[yCoord+i][xCoord+j] = data[i][j];
            }
        }


        clearLines();
        resetPieceStuff();
        updateCurrentPiece();

    }

    // DRAWING STUFF
    @Override
    protected void paintComponent(Graphics g) {
        // Call the superclass's paintComponent for proper painting and background
        // clearing
        super.paintComponent(g);

        // draw hold
        if(hold != null) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < hold.rotations[0].length; j++) {
                    g.setColor(colors[hold.rotations[0][i][j]]);
                    g.fillRect(j * 30, i * 30, 30, 30);
                }
            }

        }


        // draw board
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                g.setColor(colors[board[i][j]]);
                g.fillRect(BOARD_XOFFSET + j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                g.setColor(Color.GRAY);
                g.drawRect(BOARD_XOFFSET + j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);

            }
        }
        
        // g.setColor(Color.GRAY);
        // g.drawRect(BOARD_XOFFSET, BOARD_YOFFSET, COLS*CELL_SIZE, ROWS*CELL_SIZE);

        //TODO: draw shadow piece first

        // draw current piece
        int[][] data = currentPiece.rotations[rotation];
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[i].length; j++) {
                if(data[i][j] == 0) continue;
                g.setColor(colors[data[i][j]]);
                g.fillRect(BOARD_XOFFSET + (j + xCoord) * CELL_SIZE, (i+ yCoord) * CELL_SIZE, CELL_SIZE , CELL_SIZE);
            }
        }

        // draw queue
        for (int queuedisplay = 0; queuedisplay < QUEUE_DISPLAY_LENGTH; queuedisplay++) {
            TetrisPiece piece = queue.get(queuedisplay);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < piece.rotations[0][i].length; j++) {

                    g.setColor(colors[piece.rotations[0][i][j]]);
                    g.fillRect(QUEUE_XOFFSET + j * CELL_SIZE, QUEUE_YOFFSET[queuedisplay] + i * CELL_SIZE, CELL_SIZE,
                            CELL_SIZE);
                }
            }
        }

        try {
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(BOARD_XOFFSET, (lines-20) *CELL_SIZE, BOARD_XOFFSET+ COLS*CELL_SIZE, (lines-20)*CELL_SIZE);
        } catch (Exception e) {
            // TODO: handle exception
        }

        //draw stats
        g.setColor(Color.WHITE);
        g.drawString("Lines cleared: " + lines, CELL_SIZE, 15*CELL_SIZE);

    }

    // KEY LISTENER
    private void addKeyListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                boolean added = addUserInput(code);
                if(added) {
                    switch (code) {
                        case 74: //left
                            changeCoord(-1, 0);
                            break;
                        case 76: //r
                            changeCoord(1, 0);break;
                        case 85: //u
                            changeCoord(0, -1);break;
                        case 59: //d
                            changeCoord(0, 1);break;
                        case 65: //ccw
                            rotatePiece(1);break;
                        case 83: //cw
                            rotatePiece(3);break;
                        case 16: //180
                            rotatePiece(2);break;
                        case 68: //hold
                            holdPiece();break;
                        case 75: //hd
                            insertPiece();
                            addToQueue();break;
                        default:
                            break;
                    }
                }


            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    
                int code = e.getKeyCode();
                keysHeld.remove(Integer.valueOf(code));
                keysHeldDuration.replace(Integer.valueOf(code), Long.MAX_VALUE);

                if(code == 74 || code == 76) {
                    System.out.println("trigfgere");
                    DAS_timer = DAS;}
                } catch (Exception E) {
                    
                }
            }
        });
    }

    public boolean addUserInput(int key) {
        if(keysHeld.contains(key)) {
            return false;
        }
        if(key == 74 || key == 76) {
            System.out.println("big trig");
            DAS_timer = DAS;}
        keysHeld.add(Integer.valueOf(key));
        keysHeldDuration.put(Integer.valueOf(key), System.nanoTime());
        return true;
    }
    

    //RUNNING CODE
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
        int ARR_DIRECTION = 0; //1  will make it go right, -1 make go left
        //long current_time = System.nanoTime();
        for(int i =  0; i < keysHeld.size(); i++) {
            if(keysHeld.get(i) == 74) {
                ARR_DIRECTION = -1;
                DAS_timer -= sleep;
            }
            if(keysHeld.get(i) == 76) {
                ARR_DIRECTION = 1;
                DAS_timer -= sleep;
            }
        }
        if(DAS_timer <= 0) {
            if(ARR == 0) {
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);
                changeCoord(ARR_DIRECTION, 0);

            }
            if(ARR_timer <= 0) {
                changeCoord(ARR_DIRECTION, 0);
                ARR_timer = ARR;
            }
            else {
                ARR_timer -= sleep;
            }
        }
        repaint();
    }

}
