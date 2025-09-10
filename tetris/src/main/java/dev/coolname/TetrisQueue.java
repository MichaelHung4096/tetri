package dev.coolname;

import java.awt.GridLayout;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

//TODO: 2x4 is probably the best dimensions
public class TetrisQueue extends JPanel{
    public static final int QUEUE_LENGTH = 5;
    public static final GridLayout layout = new GridLayout(QUEUE_LENGTH, 1);

    private static final TetrisPiece[]   all_pieces = TetrisPiece.values();
    private List<TetrisPiece> actually_all_pieces = Arrays.asList(all_pieces);
    private List<TetrisPiece> queue = List.copyOf(actually_all_pieces);

    private List<TetrisPieceDisplayer> queue_displays = new ArrayList<>();


    public TetrisQueue(int width, int height) {
        super(layout);

        initQueueCells();

        queue_displays.get(2).updatePiece(TetrisPiece.O_PIECE);
    }

    public List<TetrisPiece> getQueue() {
        return queue;
    }

    public String getReadableQueue() {
        String readableQueue = "";
        for(TetrisPiece piece : queue) {
            readableQueue = readableQueue + piece.charID;
        }
        return readableQueue;
    }

    public void setQueue(List<TetrisPiece> someQueue)  {
        queue = someQueue;
    }

    public void initQueueCells() {
        for(int i = 0; i < QUEUE_LENGTH; i++) {
            TetrisPieceDisplayer displayer = new TetrisPieceDisplayer(30, 30);
            queue_displays.add(displayer);
            this.add(displayer);
        }

    }
}
