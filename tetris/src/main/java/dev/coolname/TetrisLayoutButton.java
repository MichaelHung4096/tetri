package dev.coolname;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class TetrisLayoutButton extends JButton{
    private int row;
    private int column;
    public static Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
    public int mino = 0;
    private boolean isInteractable = true;
    private Component parent;


    public TetrisLayoutButton(int width, int height, int row, int column) {
        this.setPreferredSize(new Dimension(width, height));
        this.row = row;
        this.column = column;
        this.setBackground(Color.white);
        this.setText(Integer.toString(row * 100 + column));
        this.setBorder(border);

        addMouseListener();


    }

    public void setParentComponentRefernce(Component parent) {
        this.parent = parent;
    }

    public int getId() {
        return row * 100 + column;
    }

    public int getMino() {
        return mino;
    }
    public void setMino(int mino) {
        this.mino = mino;
    }

    public void updateMino() {
        int mino = getMino();
        switch (mino) {
            case 0:
                this.setBackground(Color.WHITE);
                break;
            case 1:
                this.setBackground(Color.CYAN);
                break;

            case 2:
                this.setBackground(Color.BLUE);
                break;
            case 3:
                this.setBackground(Color.ORANGE);
                break;
            case 4:
                this.setBackground(Color.GREEN);
                break;
            case 5:
                this.setBackground(Color.RED);
                break;
            case 6:
                this.setBackground(Color.YELLOW);
                break;
            case 7:
                this.setBackground(Color.PINK);
                break;
        
            default:
                break;
        }

        
        TetrisLayoutButton.this.setText(Integer.toString(mino));
    }

    private void addMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println("Cell: " + TetrisLayoutButton.this.getId());
                parent.requestFocusInWindow();
            }
        });
    }


    public boolean getInteractivity() {
        return isInteractable;
    }

    public void setInteractivity(boolean interactability) {
        isInteractable = interactability;
    }




}
