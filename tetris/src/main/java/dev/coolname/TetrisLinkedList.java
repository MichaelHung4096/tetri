package dev.coolname;

import java.util.ArrayList;

//make double linked list
class TetrisNode {
    private int[][] board;
    private TetrisPiece hold;
    private ArrayList<TetrisPiece> queue;
    private TetrisPiece currentPiece;
    private TetrisNode prev;
    private TetrisNode next;

    public TetrisNode(int[][] board, TetrisPiece hold, ArrayList<TetrisPiece> queue, TetrisPiece currentPiece) {
        this.board = board;
        this.hold = hold;
        this.queue = queue;
        this.currentPiece = currentPiece;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setNext(TetrisNode next) {
        this.next = next;
    }

    public void setPrev(TetrisNode prev) {
        this.prev = prev;
    }

    public TetrisNode getNext() {
        return next;
    }

    
    public TetrisNode getPrev() {
        return prev;
    }

}


//will be used for history (so i can undo and redo). could be used for replays too? might need to implement that separately tho
public class TetrisLinkedList {
    protected TetrisNode cursor;
    protected TetrisNode head;
    protected TetrisNode tail;
    
    public TetrisLinkedList() {
        head = null;
        tail = null;
        cursor = null;
    }

	public void addNewHead(TetrisNode newNode) {
		newNode.setNext(head);
        head.setPrev(newNode);
		head = newNode;
		if (tail == null) tail = head;
		cursor = head;
	}


    //make this doublelinked 
	public void Insert(TetrisNode newNode) {
		if (cursor == null) {head = tail = cursor = newNode;}
		else {
			newNode.setNext(cursor.getNext());
			cursor.setNext(newNode); 
			cursor = newNode;//if we advance the cursor
			if (cursor.getNext() == null) tail = cursor;
		}
	}

    //make this doublelinekd
	public void Delete() {
		if (cursor != tail){
			cursor.setNext(cursor.getNext().getNext());
			if (cursor.getNext() == null) tail = cursor;
		}
	}


	public boolean advanceCursor() {
		if (cursor != tail) {
			cursor = cursor.getNext();
			return true;
		}
		else
			return false;
	}


	public boolean regressCursor() {
		if (cursor != head) {
			cursor = cursor.getPrev();
			return true;
		}
		else
			return false;
	}


}
