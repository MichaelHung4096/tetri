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

public class TetrisFrame extends JPanel implements Runnable {

    private Thread thread;
    private boolean running = false;
    public long sleep = 10;
    public long last_drawn = System.nanoTime();
    public long frame = 1;

    public static final int ROWS = 20;
    public static final int COLS = 10;
    public static final int CELL_SIZE = 30;

    public static int DAS = 80;
    public static int ARR = 0;
    public int ARR_timer = ARR;
    public static int SDF = 0;
    public int SDF_timer = SDF;
    // public static int DAS_DIRECTION = 0;
    public static int gravity = 1000;
    public long gravity_timer;

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
    public static final int[] QUEUE_YOFFSET = { 0, 3 * CELL_SIZE, 6 * CELL_SIZE, 9 * CELL_SIZE, 12 * CELL_SIZE };
    public ArrayList<TetrisPiece> queue = new ArrayList<>();

    private TetrisPiece currentPiece;
    private int xCoord = 3;
    private int yCoord = 0;
    private int ghost_xCoord = xCoord;
    private int ghost_yCoord = yCoord;
    private int rotation = 0;
    private int prevRotation;
    private Integer kicktableKey;

    private ArrayList<Integer> keysHeld = new ArrayList<>();
    private HashMap<Integer, Long> keysHeldDuration = new HashMap<>();

    public int lines = 0;
    public double pieces_placed = 0;
    public double keys_pressed = 0;
    public double keys_per_piece = 0.0;
    public int finesse_faults = 0;
    public long ghost_timer = Long.MAX_VALUE;
    public boolean peek = false;
    public int current_keys_pressed = 0;
    public double time = 0;
    public long start;

    //TODO: make data to show how long a piece was on the ground without being harddropped, do this per piece and average of all pieces

    public boolean on_ground = false;
    public long time_since_ground = 0;
    public long total_time_on_ground = 0;
    public double average_time_on_ground = 0;
    
    HashMap<Character, Integer[][]> finesseMap = new HashMap<>();

    

    public TetrisFrame() {
        gravity_timer = System.nanoTime();



        initBoard();
        setFinesseMap();
        addKeyListener();
        setFocusable(true);
        requestFocusInWindow();
        
        addToQueue();
        addToQueue();
        addToQueue();
        addToQueue();
        addToQueue();

        updateCurrentPiece();
        insertGhostPiece();

    }

    //temp, set finesse table
    public void setFinesseMap() {
        finesseMap.put('T', new Integer[][] {
            {2, 3, 2, 1, 2, 3, 3, 2},
            {3, 4, 3, 2, 3, 4, 4, 3, 3}, //ccw idk if this is the right index but wtv
            {3, 4, 3, 2, 3, 4, 4, 3}, //180
            {3, 3, 4, 3, 2, 3, 4, 4, 3}, //cw (index by xCoord + 1)
        });


        finesseMap.put('O', new Integer[][] {
            {2, 3, 3, 2, 1, 2, 3, 3, 2}, //index all rotations by xCoord+1
            {2, 3, 3, 2, 1, 2, 3, 3, 2},
            {2, 3, 3, 2, 1, 2, 3, 3, 2},
            {2, 3, 3, 2, 1, 2, 3, 3, 2},
        });


        
        finesseMap.put('Z', new Integer[][] {
            {2, 3, 2, 1, 2, 3, 3, 2}, //0
            {3, 3, 3, 2, 2, 3, 4, 3, 3}, //ccw
            {2, 3, 2, 1, 2, 3, 3, 2},
            {3, 3, 3, 2, 2, 3, 4, 3, 3}, //cw (index by xCoord + 1))
        });

        finesseMap.put('S', new Integer[][] {
            {2, 3, 2, 1, 2, 3, 3, 2}, //0
            {3, 3, 3, 2, 2, 3, 4, 3, 3}, //ccw
            {2, 3, 2, 1, 2, 3, 3, 2},
            {3, 3, 3, 2, 2, 3, 4, 3, 3}, //cw (index by xCoord + 1))
        });



        finesseMap.put('L', new Integer[][] {
            {2, 3, 2, 1, 2, 3, 3, 2}, //0
            {3, 4, 3, 2, 3, 4, 4, 3, 3}, //ccw
            {3, 4, 3, 2, 3, 4, 4, 3},
            {3, 3, 4, 3, 2, 3, 4, 4, 3}, //cw (index by xCoord + 1))
        });


        
        finesseMap.put('J', new Integer[][] {
            {2, 3, 2, 1, 2, 3, 3, 2}, //0
            {3, 4, 3, 2, 3, 4, 4, 3, 3}, //ccw
            {3, 4, 3, 2, 3, 4, 4, 3},
            {3, 3, 4, 3, 2, 3, 4, 4, 3}, //cw (index by xCoord + 1))
        });



        finesseMap.put('I', new Integer[][] {
            {2, 3, 2, 1, 2, 3, 2}, //0
            {3, 3, 3, 3, 2, 2, 3, 3, 3, 3}, //ccw (index by xCoord + 1)
            {2, 3, 2, 1, 2, 3, 2},
            {3, 3, 3, 3, 2, 2, 3, 3, 3, 3}, //cw (index by xCoord + 2))
        });
    }


    // RESET STUFF
    private void reset() {
        initBoard();

        queue.clear();
        addToQueue();

        currentPiece = removeFirstPiece();

        hold = null;

        resetPieceStuff();

        lines = 0;
        pieces_placed = 0;
        keys_per_piece = 0;
        keys_pressed = 0;
        finesse_faults = 0;

        start = 0;
        time = 0;

        on_ground = false;
        time_since_ground = 0;
        total_time_on_ground = 0;
        average_time_on_ground = 0;
    }

    // BOARD CODE
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
            if (board[row][c] == 0)
                return false;
        }
        return true;
    }

    private void removeLine(int line) {
        // Move every row above `line` one step down
        for (int r = line; r > 0; r--) {
            System.arraycopy(board[r - 1], 0, board[r], 0, COLS);
        }

        // Clear the top row
        for (int c = 0; c < COLS; c++) {
            board[0][c] = 0;
        }

        lines++;
    }

    // HOLD CODE
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
        yCoord = -1;
        ghost_xCoord = xCoord;
        ghost_yCoord = yCoord;
        rotation = 0;
        prevRotation = 0;
        current_keys_pressed = 0;
        gravity_timer = System.nanoTime();
    }

    // ghost piece code, most of it was copyt pasted, refactor to make more
    // efficient
    // TODO: ^^^

    public boolean changeGhostCoord(int xChange, int yChange) {
        // System.out.println(System.currentTimeMillis());
        ghost_xCoord += xChange;
        ghost_yCoord += yChange;
        if (isColliding(ghost_xCoord, ghost_yCoord)) {
            ghost_xCoord -= xChange;
            ghost_yCoord -= yChange;
            return false;
        }
        return true;
    }

    public void insertGhostPiece() {
        ghost_xCoord = xCoord;
        ghost_yCoord = yCoord;
        while (true) {
            if (!changeGhostCoord(0, 1))
                break;

        }
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

    public void softDrop() {
        while (true) {
            if (!changeCoord(0, 1))
                break;

        }
    }

    public boolean changeCoord(int xChange, int yChange) {
        // System.out.println(System.currentTimeMillis());
        xCoord += xChange;
        yCoord += yChange;
        if (isColliding(xCoord, yCoord)) {
            xCoord -= xChange;
            yCoord -= yChange;
            return false;
        }
        return true;
    }

    private boolean isColliding(int x, int y) {
        int[][] data = currentPiece.rotations[rotation];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                // bounds collision detection
                if (j + x < 0) {

                    if (data[i][j] != 0) {
                        // System.out.println("left: fck u");
                        return true;
                    }

                    if (data[i][j] == 0) {
                        // System.out.println("left: gotchu");
                    }
                }

                if (j + x >= COLS) {
                    if (data[i][j] != 0) {
                        // System.out.println("right: fck u");
                        return true;
                    }

                    if (data[i][j] == 0) {
                        // System.out.println("right: gotchu");
                    }
                }

                if (i + y >= ROWS) {
                    if (data[i][j] != 0) {
                        // System.out.println("down:fck u");
                        return true;
                    }

                    if (data[i][j] == 0) {
                        // System.out.println("down: gotchu");
                    }
                }

                if (i + y < 0) {
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
                    if (board[y + i][x + j] != 0) {
                        // System.out.println("WEEEWOOOWOOOWOWOWWOWOWOWOWOWOW");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void holdPiece() {
        if (hold == null) {
            System.out.println("no piece in hold");
            hold = currentPiece;
            updateCurrentPiece();
        } else {
            TetrisPiece temp = currentPiece;
            currentPiece = hold;
            hold = temp;
        }

        resetPieceStuff();
    }

    // public void insertGhostPiece() {
    // int[][] data = ghostPiece.rotations[rotation];
    // int
    // }
    public void insertPiece() {
        int[][] data = currentPiece.rotations[rotation];

        while (true) {
            if (!changeCoord(0, 1))
                break;

        }

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                if (data[i][j] == 0)
                    continue;
                try {
                    
                    board[yCoord + i][xCoord + j] = data[i][j];
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }

        System.out.println(xCoord);

        //check finesse
        checkFinesse();



        clearLines();
        resetPieceStuff();
        updateCurrentPiece();

    }

    public void checkFinesse() {
        char pieceChar = currentPiece.charID;
        Integer[][] pieceFinesseTable = finesseMap.get(pieceChar);
        int bestMoves = 9;
        switch (pieceChar) {
            case 'O':
                bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                break;



            case 'T':
                if(rotation == 3) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                }
                else {
                    bestMoves = pieceFinesseTable[rotation][xCoord];
                }
                break;



            case 'L':
                if(rotation == 3) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                }
                else {
                    bestMoves = pieceFinesseTable[rotation][xCoord];
                }
                break;


            case 'J':
                if(rotation == 3) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                }
                else {
                    bestMoves = pieceFinesseTable[rotation][xCoord];
                }
                break;


            case 'S':
                if(rotation == 3) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                }
                else {
                    bestMoves = pieceFinesseTable[rotation][xCoord];
                }
                break;



            case 'Z':
                if(rotation == 3) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                }
                else {
                    bestMoves = pieceFinesseTable[rotation][xCoord];
                }
                break;


            case 'I':
                if(rotation == 1) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                }
                else if(rotation == 3){
                    bestMoves = pieceFinesseTable[rotation][xCoord + 2];
                }
                else {
                    bestMoves = pieceFinesseTable[rotation][xCoord];
                }
                break;
            default:
                break;
        }
        if(current_keys_pressed > bestMoves) {
            finesse_faults += current_keys_pressed - bestMoves;

        }
    }

    // DRAWING STUFF
    @Override
    protected void paintComponent(Graphics g) {
        // Call the superclass's paintComponent for proper painting and background
        // clearing
        super.paintComponent(g);

        // draw hold
        if (hold != null) {
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

                if (ghost_timer == Long.MAX_VALUE) {
                    g.setColor(colors[board[i][j]]);
                    g.fillRect(BOARD_XOFFSET + j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                }

                g.setColor(Color.GRAY);
                g.drawRect(BOARD_XOFFSET + j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);

            }
        }

        // g.setColor(Color.GRAY);
        // g.drawRect(BOARD_XOFFSET, BOARD_YOFFSET, COLS*CELL_SIZE, ROWS*CELL_SIZE);


        int[][] data = currentPiece.rotations[rotation];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] == 0)
                    continue;
                g.setColor(Color.gray);
                g.fillRect(BOARD_XOFFSET + (j + ghost_xCoord) * CELL_SIZE, (i + ghost_yCoord) * CELL_SIZE, CELL_SIZE,
                        CELL_SIZE);
            }
        }
        // draw current piece
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] == 0)
                    continue;
                g.setColor(colors[data[i][j]]);
                g.fillRect(BOARD_XOFFSET + (j + xCoord) * CELL_SIZE, (i + yCoord) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
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
            g.drawLine(BOARD_XOFFSET, (lines - 20) * CELL_SIZE, BOARD_XOFFSET + COLS * CELL_SIZE,
                    (lines - 20) * CELL_SIZE);
        } catch (Exception e) {
            // do nothing
        }

        // draw stats
        g.setColor(Color.WHITE);
        g.drawString("Lines cleared: " + lines, CELL_SIZE, 10 * CELL_SIZE);
        g.drawString("piescs placed: " + pieces_placed, CELL_SIZE, 11 * CELL_SIZE);
        g.drawString("keys pressed: " + keys_pressed, CELL_SIZE, 12 * CELL_SIZE);
        g.drawString("KPP: " + keys_per_piece, CELL_SIZE, 13 * CELL_SIZE);
        g.drawString("Finesse: " + finesse_faults, CELL_SIZE, 14 * CELL_SIZE);
        g.drawString("TIme : " + time, CELL_SIZE, 15 * CELL_SIZE);
        g.drawString("total ground : " + average_time_on_ground * pieces_placed, CELL_SIZE, 16 * CELL_SIZE);
        g.drawString("avg ground : " + average_time_on_ground, CELL_SIZE, 17 * CELL_SIZE);

    }

    // KEY LISTENER
    private void addKeyListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                // System.out.println(code);
                boolean added = addUserInput(code);
                if (added) {
                    if(start == 0) {
                        start = System.nanoTime();
                    }
                    switch (code) {
                         case 74: // left
                            keys_pressed++;
                            current_keys_pressed++;
                            changeCoord(-1, 0);
                            insertGhostPiece();
                            break;
                         case 76: // r
                            keys_pressed++;
                            current_keys_pressed++;
                            changeCoord(1, 0);
                            insertGhostPiece();
                            break;
                         case 85: // u
                            keys_pressed++;
                            current_keys_pressed++;
                            changeCoord(0, -1);
                            insertGhostPiece();
                            break;
                        case 59: // d
                            keys_pressed++;
                            current_keys_pressed++;
                            softDrop();
                            insertGhostPiece();
                            break;
                        case 65: // ccw
                            keys_pressed++;
                            current_keys_pressed++;
                            rotatePiece(1);
                            insertGhostPiece();
                            break;
                        case 83: // cw
                            keys_pressed++;
                            current_keys_pressed++;
                            rotatePiece(3);
                            insertGhostPiece();
                            break;
                        case 16: // 180
                            keys_pressed++;
                            current_keys_pressed++;
                            rotatePiece(2);
                            insertGhostPiece();
                            break;
                        case 68: // hold
                            keys_pressed++;
                            current_keys_pressed++;
                            holdPiece();
                            insertGhostPiece();
                            break;
                        case 75: // hd
                            keys_pressed++;
                            current_keys_pressed++;
                            insertPiece();
                            addToQueue();
                            average_time_on_ground = ((average_time_on_ground * pieces_placed) + (total_time_on_ground)) /(pieces_placed + 1);
                            pieces_placed++;
                            total_time_on_ground = 0;
                            keys_per_piece = keys_pressed / pieces_placed;
                            insertGhostPiece();
                            break;
                        case 8: // backspace to reset
                            reset();
                            break;
                        case 32: // ghost and is spacebar
                            ghost_timer = System.nanoTime();
                            peek = true;
                            break;
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

                } catch (Exception E) {

                }
            }
        });
    }

    public boolean addUserInput(int key) {
        if (keysHeld.contains(key)) {
            return false;
        }
        keysHeld.add(Integer.valueOf(key));
        keysHeldDuration.put(Integer.valueOf(key), System.nanoTime());
        return true;
    }

    // RUNNING CODE
    public void start() {
        if (running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        int i = 0;
        while (running) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            update(System.nanoTime());
        }
    }

    private void update(long current_time) {

        if (peek) {
            if ((current_time - ghost_timer) / 1_000_000 >= 5000) {
                ghost_timer = Long.MAX_VALUE;
                peek = false;
            }
        }




        if(ghost_yCoord == yCoord) {
            if(!on_ground) {
                on_ground = true;
                time_since_ground = current_time;
            }
        }
        else {
            if(on_ground) {
                on_ground = false;
                total_time_on_ground += ((current_time - time_since_ground) / 1_000_000);
            }
        }





        int ARR_DIRECTION = 0; // 1 will make it go right, -1 make go left
        int key = 0; // key for left/right direciton will be tm
        // long current_time = System.nanoTime();
        for (int i = 0; i < keysHeld.size(); i++) {
            if (keysHeld.get(i) == 74) {
                ARR_DIRECTION = -1;
                key = keysHeld.get(i);
            }
            if (keysHeld.get(i) == 76) {
                ARR_DIRECTION = 1;
                key = keysHeld.get(i);
            }
        }
        long time;
        try {
            time = keysHeldDuration.get(Integer.valueOf(key));
        } catch (Exception e) {
            time = -1;
        }
        if ((current_time - time) / 1_000_000 >= DAS) {
            if (ARR == 0) {
                while (true && ARR_DIRECTION != 0) {
                    if (!changeCoord(ARR_DIRECTION, 0)) {
                        break;
                    }
                }
                insertGhostPiece();

            }
            if (ARR_timer <= 0) {
                changeCoord(ARR_DIRECTION, 0);
                ARR_timer = ARR;
            } else {
                ARR_timer -= sleep;
            }
        }


        if((current_time - gravity_timer) / 1_000_000 >= gravity) {
            if(ghost_yCoord > yCoord) {
                changeCoord(0, 1);
            }
            if(yCoord > ghost_yCoord) {
                yCoord = ghost_yCoord;
            }
            gravity_timer = current_time;
        }





        this.time = ((current_time - start) / 1_000_000) / 1000.0;

        if ((current_time - last_drawn) / 1_000_000 >= frame) {
            repaint();
            last_drawn = current_time;
        }
    }

}
