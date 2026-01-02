package dev.coolname;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JPanel;

public class TetrisFrame extends JPanel implements Runnable {

    // just one more variable bro trust me we just need one more variabel and itll
    // fix everything just trust me bro

    private Thread thread;
    private boolean running = false;
    public long sleep = 10;
    public long last_drawn = System.nanoTime();
    public long frame = 1;

    public static final int ROWS = 20;
    public static final int COLS = 10;
    public static final int CELL_SIZE = 30;

    public int DAS = 80;
    public int ARR = 0;
    public int ARR_timer = ARR;
    public int SDF = 0;
    public int SDF_timer = SDF;
    // public int DAS_DIRECTION = 0;
    public int gravity = 1000;
    public long gravity_timer;
    public boolean IRS;
    public boolean IHS;

    public final Color[] colors = { Color.BLACK, Color.CYAN, Color.BLUE, Color.ORANGE, Color.GREEN, Color.RED,
            Color.YELLOW, Color.PINK };

    public final int BOARD_XOFFSET = 5 * CELL_SIZE;
    public final int BOARD_YOFFSET = 0;
    private int board[][] = new int[ROWS * 2][COLS];

    public final int HOLD_XOFFSET = 0;
    public final int HOLD_YOFFSET = 0;
    private TetrisPiece hold;

    public final int QUEUE_DISPLAY_LENGTH = 5;
    public final int QUEUE_XOFFSET = 16 * CELL_SIZE;
    private final TetrisPiece[] all_pieces = TetrisPiece.values();
    public final int[] QUEUE_YOFFSET = { 0, 3 * CELL_SIZE, 6 * CELL_SIZE, 9 * CELL_SIZE, 12 * CELL_SIZE }; // TODO:
                                                                                                           // do
                                                                                                           // this
                                                                                                           // dynamically
    public ArrayList<TetrisPiece> queue = new ArrayList<>();

    private TetrisPiece currentPiece;
    private int xCoord = 3;
    private int yCoord = ROWS - 1;
    private int ghost_xCoord = xCoord;
    private int ghost_yCoord = yCoord;
    private int rotation = 0;
    private int prevRotation;
    private Integer kicktableKey;

    private ArrayList<Integer> keysHeld = new ArrayList<>();
    private HashMap<Integer, Long> keysHeldDuration = new HashMap<>();

    // ohh maybe i use like a hashmap named stats that would make alot more sense
    // huh
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
    public double final_time = 0;
    public double pieces_per_second = 0;
    public double keys_per_second = 0;

    public boolean on_ground = false;
    public long time_since_ground = 0;
    public long total_time_on_ground = 0;
    public double average_time_on_ground = 0;

    public double[] kpm_keys = new double[COLS];
    public double[] kpm_minos = new double[COLS];
    public boolean[] kpm_detected = new boolean[COLS];

    HashMap<Character, Integer[][]> finesseMap = new HashMap<>();

    TetrisSettings settings;
    TetrisLinkedList history;
    Font customFont = new Font("Serif", Font.PLAIN, 18);

    public double mkpp_window = 1000; // ms
    public long mkpp_lastactive = 0;
    public boolean mkpp_active = false;
    public ArrayList<TetrisGameAction> actions;

    public TetrisFrame(TetrisSettings settings) {
        this.settings = settings;
        gravity_timer = System.nanoTime();
        history = new TetrisLinkedList();

        initBoard();
        setFinesseMap();
        // addKeyListener();
        // setFocusable(true);
        // requestFocusInWindow();

        addToQueue();
        addToQueue();
        addToQueue();
        addToQueue();
        addToQueue();

        currentPiece = removeFirstPiece();
        insertGhostPiece();

        addToHistory();

        actions = new ArrayList<>();

        for (int i = 0; i < COLS; i++) {
            kpm_detected[i] = false;
        }

    }

    public void undo2() { //deprecate this soon
        history.resetCursor();
        setStuff(history.cursor);
    }

    private void setStuff(TetrisNode node) {

        board = copyBoard(node.getBoard());
        hold = node.getHold();
        queue = copyQueue(node.getQueue());
        currentPiece = node.getCurrentPiece();
        resetPieceStuff();

    }

    public void redo() {

        if (history.advanceCursor()) {
            TetrisNode node = history.cursor;
            setStuff(node);
        }

    }

    public void undo() {
        if (history.regressCursor()) {
            setStuff(history.cursor);
        }
    }

    public ArrayList<TetrisPiece> copyQueue(ArrayList<TetrisPiece> q) {
        return new ArrayList<>(q);
    }

    public int[][] copyBoard(int[][] b) {
        int[][] copy = new int[2 * ROWS][COLS];
        for (int i = 0; i < 2 * ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                copy[i][j] = b[i][j];
            }
        }
        return copy;
    }

    public void addToHistory() {
        TetrisNode node = new TetrisNode(copyBoard(this.board), hold, copyQueue(this.queue), currentPiece);
        history.Insert(node);

    }

    // yooooo lets just have an enum for all the data of each piece and then not use
    // it lets gooooo
    public void setFinesseMap() {
        finesseMap.put('T', new Integer[][] {
                { 2, 3, 2, 1, 2, 3, 3, 2 },
                { 3, 4, 3, 2, 3, 4, 4, 3, 3 }, // ccw idk if this is the right index but wtv
                { 3, 4, 3, 2, 3, 4, 4, 3 }, // 180
                { 3, 3, 4, 3, 2, 3, 4, 4, 3 }, // cw (index by xCoord + 1)
        });

        finesseMap.put('O', new Integer[][] {
                { 2, 3, 3, 2, 1, 2, 3, 3, 2 }, // index all rotations by xCoord+1
                { 2, 3, 3, 2, 1, 2, 3, 3, 2 },
                { 2, 3, 3, 2, 1, 2, 3, 3, 2 },
                { 2, 3, 3, 2, 1, 2, 3, 3, 2 },
        });

        finesseMap.put('Z', new Integer[][] {
                { 2, 3, 2, 1, 2, 3, 3, 2 }, // 0
                { 3, 3, 3, 2, 2, 3, 4, 3, 3 }, // ccw
                { 2, 3, 2, 1, 2, 3, 3, 2 },
                { 3, 3, 3, 2, 2, 3, 4, 3, 3 }, // cw (index by xCoord + 1))
        });

        finesseMap.put('S', new Integer[][] {
                { 2, 3, 2, 1, 2, 3, 3, 2 }, // 0
                { 3, 3, 3, 2, 2, 3, 4, 3, 3 }, // ccw
                { 2, 3, 2, 1, 2, 3, 3, 2 },
                { 3, 3, 3, 2, 2, 3, 4, 3, 3 }, // cw (index by xCoord + 1))
        });

        finesseMap.put('L', new Integer[][] {
                { 2, 3, 2, 1, 2, 3, 3, 2 }, // 0
                { 3, 4, 3, 2, 3, 4, 4, 3, 3 }, // ccw
                { 3, 4, 3, 2, 3, 4, 4, 3 },
                { 3, 3, 4, 3, 2, 3, 4, 4, 3 }, // cw (index by xCoord + 1))
        });

        finesseMap.put('J', new Integer[][] {
                { 2, 3, 2, 1, 2, 3, 3, 2 }, // 0
                { 3, 4, 3, 2, 3, 4, 4, 3, 3 }, // ccw
                { 3, 4, 3, 2, 3, 4, 4, 3 },
                { 3, 3, 4, 3, 2, 3, 4, 4, 3 }, // cw (index by xCoord + 1))
        });

        finesseMap.put('I', new Integer[][] {
                { 2, 3, 2, 1, 2, 3, 2 }, // 0
                { 3, 3, 3, 3, 2, 2, 3, 3, 3, 3 }, // ccw (index by xCoord + 1)
                { 2, 3, 2, 1, 2, 3, 2 },
                { 3, 3, 3, 3, 2, 2, 3, 3, 3, 3 }, // cw (index by xCoord + 2))
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

        history.reset();
        addToHistory();

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
        final_time = 0;

        for (int i = 0; i < COLS; i++) {
            kpm_keys[i] = 0;
            kpm_minos[i] = 0;
            kpm_detected[i] = false;
        }
    }

    // BOARD CODE
    private void initBoard() {
        for (int i = 0; i < 2 * ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = 0;
            }
        }

    }

    private void clearLines() {
        for (int r = 2 * ROWS - 1; r >= 0; r--) {
            if (isLineFull(r)) {
                removeLine(r);
                r++;
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
        for (int r = line; r > 0; r--) {
            System.arraycopy(board[r - 1], 0, board[r], 0, COLS);
        }

        for (int c = 0; c < COLS; c++) {
            board[0][c] = 0;
        }

        lines++;
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

    private ArrayList<TetrisPiece> generateBag() { //todo: use seed to generate bag

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
        yCoord = ROWS - 1;
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
            if (!changeGhostCoord(0, 1)) // this cannot be good practice holy
                break;

        }
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

                if (i + y >= 2 * ROWS) {
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
            currentPiece = removeFirstPiece();
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

                    if (kpm_detected[xCoord + j] == false) {
                        kpm_detected[xCoord + j] = true;

                        kpm_minos[xCoord + j] += 1;
                        kpm_keys[xCoord + j] += current_keys_pressed;
                    }
                } catch (Exception e) {
                    System.out.println("error in kpm");
                }
            }
        }
        for (int i = 0; i < COLS; i++) {
            kpm_detected[i] = false;
        }

        checkFinesse();

        clearLines();

        if (lines >= 40 && final_time == 0) {
            final_time = time;
        }

        resetPieceStuff();
        currentPiece = removeFirstPiece();

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
                if (rotation == 3) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                } else {
                    bestMoves = pieceFinesseTable[rotation][xCoord];
                }
                break;

            case 'L':
                if (rotation == 3) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                } else {
                    bestMoves = pieceFinesseTable[rotation][xCoord];
                }
                break;

            case 'J':
                if (rotation == 3) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                } else {
                    bestMoves = pieceFinesseTable[rotation][xCoord];
                }
                break;

            case 'S':
                if (rotation == 3) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                } else {
                    bestMoves = pieceFinesseTable[rotation][xCoord];
                }
                break;

            case 'Z':
                if (rotation == 3) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                } else {
                    bestMoves = pieceFinesseTable[rotation][xCoord];
                }
                break;

            case 'I':
                if (rotation == 1) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 1];
                } else if (rotation == 3) {
                    bestMoves = pieceFinesseTable[rotation][xCoord + 2];
                } else {
                    bestMoves = pieceFinesseTable[rotation][xCoord];
                }
                break;
            default:
                break;
        }
        if (current_keys_pressed > bestMoves) {
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
                    g.setColor(colors[board[i + ROWS][j]]);
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
                g.fillRect(BOARD_XOFFSET + (j + ghost_xCoord) * CELL_SIZE, (i + ghost_yCoord - ROWS) * CELL_SIZE,
                        CELL_SIZE,
                        CELL_SIZE);
            }
        }
        // draw current piece
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] == 0)
                    continue;
                try {

                    g.setColor(colors[data[i][j]]);
                    g.fillRect(BOARD_XOFFSET + (j + xCoord) * CELL_SIZE, (i + yCoord - ROWS) * CELL_SIZE, CELL_SIZE,
                            CELL_SIZE);
                } catch (Exception e) {
                    // TODO: handle exception
                }
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
        // TODO: this could probably just be all one drawString use \n
        // ^^oh would also probably be rlly good for truncating doubles
        g.setFont(customFont);
        g.drawString("Lines cleared: " + lines, CELL_SIZE - 15, 9 * CELL_SIZE);
        g.drawString("piescs placed: " + pieces_placed, CELL_SIZE - 15, 10 * CELL_SIZE);
        g.drawString("keys pressed: " + keys_pressed, CELL_SIZE - 15, 11 * CELL_SIZE);
        g.drawString("KPP: " + keys_per_piece, CELL_SIZE - 15, 12 * CELL_SIZE);
        g.drawString("Finesse: " + finesse_faults, CELL_SIZE - 15, 13 * CELL_SIZE);
        g.drawString("TIme : " + time, CELL_SIZE - 15, 14 * CELL_SIZE);
        // g.drawString("total ground : " + average_time_on_ground * pieces_placed,
        // CELL_SIZE, 12 * CELL_SIZE);
        // g.drawString("avg ground : " + average_time_on_ground, CELL_SIZE, 13 *
        // CELL_SIZE);
        g.drawString("PPS: " + pieces_per_second, CELL_SIZE - 15, 15 * CELL_SIZE);
        g.drawString("KPS: " + keys_per_second, CELL_SIZE - 15, 16 * CELL_SIZE);
        g.drawString("40L times: " + final_time, CELL_SIZE - 15, 17 * CELL_SIZE);

        if (mkpp_active) {
            g.setColor(Color.WHITE);
            g.fillRect(CELL_SIZE - 15, 18 * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

    }

    private void move_left() {

        if(mkpp_active) {
            System.out.println("moved left optimally");
        } else {
            mkpp_active = true;
            mkpp_lastactive = System.nanoTime();
        }

        keys_pressed++;
        current_keys_pressed++;
        changeCoord(-1, 0);
        insertGhostPiece();
    }

    private void move_right() {

        if(mkpp_active) {
            System.out.println("moved right optimally");
        } else {
            mkpp_active = true;
            mkpp_lastactive = System.nanoTime();
        }

        keys_pressed++;
        current_keys_pressed++;
        changeCoord(1, 0);
        insertGhostPiece();
    }

    private void move_up() {

        keys_pressed++;
        current_keys_pressed++;
        changeCoord(0, -1);
        insertGhostPiece();
    }

    private void soft_drop() {

        keys_pressed++;
        current_keys_pressed++;
        softDrop(); // yea bro lets just have two mehtods named soft drop im so good at naming stuff
        insertGhostPiece();
    }

    private void rotate_ccw() {

        keys_pressed++;
        current_keys_pressed++;
        
        if(!mkpp_active) {
            mkpp_active = true;
            mkpp_lastactive = System.nanoTime();
        } else {
            System.out.println("good ccw");
        }

        rotatePiece(1);
        insertGhostPiece();
    }

    private void rotate_cw() {

        keys_pressed++;
        current_keys_pressed++;
        
        if(!mkpp_active) {
            mkpp_active = true;
            mkpp_lastactive = System.nanoTime();
        } else {
            System.out.println("nice cw");
        }

        rotatePiece(3);
        insertGhostPiece();
    }

    private void rotate_180() {

        keys_pressed++;
        current_keys_pressed++;
        if(!mkpp_active) {
            mkpp_active = true;
            mkpp_lastactive = System.nanoTime();
        } else {
            System.out.println("banger 180");
        }
        rotatePiece(2);
        insertGhostPiece();
    }

    private void hold() {

        keys_pressed++;
        current_keys_pressed++;
        holdPiece();
        insertGhostPiece();
    }

    private void hard_drop() {

        keys_pressed++;
        current_keys_pressed++;
        insertPiece();
        addToQueue();
        average_time_on_ground = ((average_time_on_ground * pieces_placed) + (total_time_on_ground))
                / (pieces_placed + 1);
        pieces_placed++;
        total_time_on_ground = 0;
        keys_per_piece = keys_pressed / pieces_placed;
        insertGhostPiece();
        addToHistory();
    }

    private void ghost() {

        ghost_timer = System.nanoTime();
        peek = true;
    }

    public void key_released(KeyEvent e) {
        try {

            int code = e.getKeyCode();
            keysHeld.remove(Integer.valueOf(code));
            keysHeldDuration.replace(Integer.valueOf(code), Long.MAX_VALUE);

        } catch (Exception E) {

        }
    }

    public boolean containsElement(int[] list, int element) { // idk why i have to make my own method for this i cant
                                                              // believe java just doesn thave some thing that can do it
                                                              // mofr me and acutally worolk this langauage sutcks
        for (int i : list) {
            if (i == element)
                return true;
        }
        return false;
    }

    public void key_handle(int code) {

        boolean added = addUserInput(code);
        if (added) {
            if (start == 0) {
                start = System.nanoTime();
            }
            // no if else statements in case user wants one key to be binded to multiple
            // stuff
            // ^^ WAIT ACTUALLY INSIGHTFUL COMMENT WHAT
            if (containsElement(settings.MOVE_LEFT, code)) {
                actions.add(TetrisGameAction.MOVE_LEFT);
                move_left();
            }
            if (containsElement(settings.MOVE_RIGHT, code)) {
                actions.add(TetrisGameAction.MOVE_RIGHT);
                move_right();
            }
            if (containsElement(settings.SOFT_DROP, code)) {
                actions.add(TetrisGameAction.SOFT_DROP);
                soft_drop();
            }
            if (containsElement(settings.ROTATE_CCW, code)) {
                actions.add(TetrisGameAction.ROTATE_CCW);
                rotate_ccw();
            }
            if (containsElement(settings.ROTATE_CW, code)) {
                actions.add(TetrisGameAction.ROTATE_CW);
                rotate_cw();
            }
            if (containsElement(settings.ROTATE_180, code)) {
                actions.add(TetrisGameAction.ROTATE_180);
                rotate_180();
            }
            if (containsElement(settings.HOLD, code)) {
                actions.add(TetrisGameAction.HOLD);
                hold();
            }
            if (containsElement(settings.HARD_DROP, code)) {
                actions.add(TetrisGameAction.HARD_DROP);
                hard_drop();
            }
            if (containsElement(settings.RESET, code)) {
                reset();
            }
            if (containsElement(settings.GHOST_ACTION, code)) {
                ghost();
            }
            if (code == 81) {
                undo2();
            }
            if (code == 87) {
                redo();
            }
            if (code == 69) {
                undo();
            }

            if(code == 54) {
                System.out.println("Actions taken this game:");
                for(TetrisGameAction action : actions) {
                    System.out.println(action);
                }
            }

        }
    }
    // // KEY LISTENER
    // private void addKeyListener() {
    // this.addKeyListener(new KeyAdapter() {
    // @Override
    // public void keyPressed(KeyEvent e) {
    // int code = e.getKeyCode();
    // // System.out.println(code);
    // boolean added = addUserInput(code);
    // if (added) {
    // if (start == 0) {
    // start = System.nanoTime();
    // }
    // switch (code) {
    // case 74: // left
    // move_left();
    // break;
    // case 76: // r
    // move_right();
    // break;
    // case 85: // u
    // move_up();
    // break;
    // case 59: // d
    // soft_drop();
    // break;
    // case 65: // ccw
    // rotate_ccw();
    // break;
    // case 83: // cw
    // rotate_cw();
    // break;
    // case 16: // 180
    // rotate_180();
    // break;
    // case 68: // hold
    // hold();
    // break;
    // case 75: // hd
    // hard_drop();
    // break;
    // case 8: // backspace to reset
    // reset();
    // break;
    // case 32: // ghost and is spacebar
    // ghost();
    // break;
    // default:
    // break;

    // }
    // }

    // }

    // @Override
    // public void keyReleased(KeyEvent e) {
    // key_released(e);

    // }
    // });
    // }

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
    //]fixing race condition means integrating key_handle() code here?
    public void run() {
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
        keys_per_second = keys_pressed / time;
        pieces_per_second = pieces_placed / time;

        if (peek) {
            if ((current_time - ghost_timer) / 1_000_000 >= 5000) {
                ghost_timer = Long.MAX_VALUE;
                peek = false;
            }
        }

        if (ghost_yCoord == yCoord) {
            if (!on_ground) {
                on_ground = true;
                time_since_ground = current_time;
            }
        } else {
            if (on_ground) {
                on_ground = false;
                total_time_on_ground += ((current_time - time_since_ground) / 1_000_000);
            }
        }

        int ARR_DIRECTION = 0; // 1 will make it go right, -1 make go left
        int key = 0; // key for left/right direciton will be tm
        // long current_time = System.nanoTime();
        for (int i = 0; i < keysHeld.size(); i++) {
            if (containsElement(settings.MOVE_LEFT, keysHeld.get(i))) {
                ARR_DIRECTION = -1;
                key = keysHeld.get(i);
            }
            if (containsElement(settings.MOVE_RIGHT, keysHeld.get(i))) {
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
        if ((current_time - time) / 1_000_000 >= settings.DAS) {
            if (settings.ARR == 0) {
                while (true && ARR_DIRECTION != 0) {
                    if (!changeCoord(ARR_DIRECTION, 0)) {
                        break;
                    }
                }
                insertGhostPiece();

            }
            // pretty sure this is wrong for when arr not 0 but we ball
            if (ARR_timer <= 0) {
                changeCoord(ARR_DIRECTION, 0);
                ARR_timer = settings.ARR;
            } else {
                ARR_timer -= sleep;
            }
        }

        if ((current_time - gravity_timer) / 1_000_000 >= settings.GRAVITY) {
            if (ghost_yCoord > yCoord) {
                changeCoord(0, 1);
            }
            if (yCoord > ghost_yCoord) {
                yCoord = ghost_yCoord;
            }
            gravity_timer = current_time;
        }

        this.time = ((current_time - start) / 1_000_000) / 1000.0;

        if ((current_time - last_drawn) / 1_000_000 >= frame) {
            repaint();
            last_drawn = current_time;
        }



        if (mkpp_active) {
            if ((current_time - mkpp_lastactive) / 1_000_000 >= mkpp_window) {
                mkpp_active = false;
            }
        }
    }

}
