package dev.coolname;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;

import javax.swing.JFrame;

public class Main {
    private static JFrame frame;

    private static void init() {
        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        frame = new JFrame("haha biggie bock ride");
        frame.setLayout(new FlowLayout());



        TetrisFrame pan = new TetrisFrame();
        //pan.setVisible(true);
        Dimension d = new Dimension((TetrisFrame.COLS + 10) * TetrisFrame.CELL_SIZE, TetrisFrame.ROWS * TetrisFrame.CELL_SIZE);
        pan.setPreferredSize(d);
        pan.setBackground(Color.BLACK);
        frame.add(pan);

        pan.start();

        HashMap<Character, Integer[][]> finesseMap = new HashMap<>();
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
            {3, 3, 4, 3, 2, 3, 4, 4, 3}, //cw (index by xCoord + 2))
        });










        init();
    }
}