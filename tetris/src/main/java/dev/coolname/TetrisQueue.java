package dev.coolname;

import java.awt.GridLayout;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JPanel;

//TODO: 2x4 is probably the best dimensions
public class TetrisQueue extends JPanel{
    public static final int QUEUE_LENGTH = 5;
    public static final GridLayout layout = new GridLayout(QUEUE_LENGTH, 1);

    private static final TetrisPiece[]   all_pieces = TetrisPiece.values();
    private List<TetrisPiece> actually_all_pieces = new ArrayList<>(Arrays.asList(all_pieces));
    private ArrayList<TetrisPiece> queue = new ArrayList<>(actually_all_pieces);

    public List<TetrisPieceDisplayer> queue_displays = new ArrayList<>();


    public TetrisQueue(int width, int height) {
        super(layout);

        initQueueCells();
        syncQueue();


        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
        this.addToQueue(generateBag());
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

    public void setQueue(ArrayList<TetrisPiece> someQueue)  {
        queue = someQueue;
    }

    public void addToQueue(ArrayList<TetrisPiece> bag) {
        queue.addAll(bag);
    }

    public void syncQueue() {
        for(int i = 0; i < QUEUE_LENGTH; i++) {
            queue_displays.get(i).updatePiece(queue.get(i));
        }
    }

    public TetrisPiece removeFirstPiece() {
        //System.out.println(queue.get(0));
        TetrisPiece newCurrentPiece = queue.remove(0);
        //System.out.println(queue.get(0));
        syncQueue();
        return newCurrentPiece;
    }

    public void initQueueCells() {
        for(int i = 0; i < QUEUE_LENGTH; i++) {
            TetrisPieceDisplayer displayer = new TetrisPieceDisplayer(30, 30);
            queue_displays.add(displayer);
            this.add(displayer);
        }

    }

    public List<TetrisPieceDisplayer> getQueueDisplayers() {
        return queue_displays;
    }


    private ArrayList<TetrisPiece> generateBag() {

        ArrayList<TetrisPiece> bag =  new ArrayList<>(Arrays.asList(all_pieces));
        Collections.shuffle(bag);

        return bag;
    }
}
