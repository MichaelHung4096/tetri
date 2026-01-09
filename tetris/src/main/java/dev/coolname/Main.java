package dev.coolname;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.io.File;

import javax.swing.JButton;
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

        ObjectMapper obj = new ObjectMapper();
        TetrisSettings settings = null;
        try {
            
        settings = obj.readValue(new File("settings.json"), TetrisSettings.class);
        } catch (Exception e) {
            System.out.println(e);
        }





        frame = new JFrame("haha biggie bock ride");
        frame.setLayout(new FlowLayout());



        TetrisFrame pan = new TetrisFrame(settings);
        //pan.setVisible(true);
        Dimension d = new Dimension((TetrisFrame.COLS + 10) * TetrisFrame.CELL_SIZE, TetrisFrame.ROWS * TetrisFrame.CELL_SIZE + 100);
        pan.setPreferredSize(d);
        pan.setBackground(Color.BLACK);
        frame.add(pan);


        TetrisSettingsFrame settingsMenu = new TetrisSettingsFrame(settings);
        frame.add(settingsMenu);
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
                pan.keyPressQueueAdd(code);
                // pan.key_handle(code);
                // p2.key_handle(code);
                // p3.key_handle(code);

            }   

            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                pan.keyReleaseQueue.add(code);
                // pan.key_released(code);
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

        pan.generateQueueSeeded(1104486338, 1);





        init();





    }
}