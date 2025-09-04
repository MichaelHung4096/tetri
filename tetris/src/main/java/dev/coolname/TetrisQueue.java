package dev.coolname;

import java.awt.GridLayout;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JPanel;

public class TetrisQueue extends JPanel{
    public static final int QUEUE_LENGTH = 5;
    public static final GridLayout layout = new GridLayout(QUEUE_LENGTH, 1);

    private static final TetrisPiece[]   all_pieces = TetrisPiece.values();
    private List<TetrisPiece> actually_all_pieces = Arrays.asList(all_pieces);
    private List<TetrisPiece> queue = List.copyOf(actually_all_pieces);


    public TetrisQueue(int width, int height) {
        super(layout);

        initQueueCells();
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
        GridLayout layout = new GridLayout(4, 4);
        for(int i = 0; i < QUEUE_LENGTH; i++) {
            JPanel tetrisPieceSlot = new JPanel();
            tetrisPieceSlot.setLayout(layout);
            
            for(int j = 0; j < 4; j++) {
                for(int k = 0; k < 4; k++) {
                    TetrisNoninteractiveButton button = new TetrisNoninteractiveButton(30, 30, j, k);
                    tetrisPieceSlot.add(button);
                }
            }

            this.add(tetrisPieceSlot);
        }
    }
}
