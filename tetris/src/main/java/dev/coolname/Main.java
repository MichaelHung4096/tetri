package dev.coolname;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.io.File;
import javax.swing.JFrame;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    private static JFrame frame;

    private static void init() {
        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);frame.setFocusable(true);
frame.requestFocusInWindow();
    }

    public static void main(String[] args) {
        System.out.println("sdfsdf");

        ObjectMapper obj = new ObjectMapper();
        try {
            
        TetrisSettings test = obj.readValue(new File("settings.json"), TetrisSettings.class);
        System.out.println("f");
        System.out.println(test);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }





        frame = new JFrame("haha biggie bock ride");
        frame.setLayout(new FlowLayout());



        TetrisFrame pan = new TetrisFrame();
        //pan.setVisible(true);
        Dimension d = new Dimension((TetrisFrame.COLS + 10) * TetrisFrame.CELL_SIZE, TetrisFrame.ROWS * TetrisFrame.CELL_SIZE);
        pan.setPreferredSize(d);
        pan.setBackground(Color.BLACK);
        frame.add(pan);

        // TetrisFrame p2 = new TetrisFrame();
        // p2.setPreferredSize(d);
        // p2.setBackground(Color.BLACK);
        // frame.add(p2);

        
        // TetrisFrame p3 = new TetrisFrame();
        // p3.setPreferredSize(d);
        // p3.setBackground(Color.BLACK);
        // frame.add(p3);


                frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                pan.key_handle(code);
                // p2.key_handle(code);
                // p3.key_handle(code);

            }   

            @Override
            public void keyReleased(KeyEvent e) {
                pan.key_released(e);
                // p2.key_released(e);
                // p3.key_released(e);

            }
        });

        pan.start();
        // // p2.start();
        // // p3.start();

        // // TetrisFrame peter = new TetrisFrame();
        // // peter.setPreferredSize(d);
        // // peter.setBackground(Color.BLACK);
        // // frame.add(peter);
        // // peter.start();





        init();





    }
}