package dev.coolname;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class SettingsButton extends JButton {
    private TetrisSettings settings;
    private String control;
    private ObjectMapper obj;
    public SettingsButton(String t, String control) {
        super(t);
        this.control = control;
        init();
        obj = new ObjectMapper();
        TetrisSettings settings = null;
        try {
            
        settings = obj.readValue(new File("settings.json"), TetrisSettings.class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private void init() {
            this.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            // setText("Press new key(s)");
            requestFocusInWindow();
        }
    });

    this.addKeyListener(new KeyListener() {
        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println(e.getKeyChar());
            System.out.println(t());

            byte[] jsonData = null;
            try {
                jsonData = Files.readAllBytes(Paths.get("settings.json"));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                ObjectNode rootNode = (ObjectNode) obj.readTree(jsonData);
                JsonNode a = rootNode.get(control);
                int[] keys = obj.convertValue(a,int[].class);
                rootNode.set(control, obj.valueToTree(addKey(keys, e.getKeyCode())));
                obj.writeValue(new File("settings.json"), rootNode);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
                        q();
            

        }

        @Override
        public void keyReleased(KeyEvent e) {
        //    System.out.println("released");
            
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // System.out.println("tpyed ");
            
        }
    });
    }

    public String t() {
        return this.getText();
    }

    public void q() {
        this.getParent().requestFocusInWindow();
    }

    public int[] addKey(int[] arr, int key) {
        int[] newArr = new int[arr.length + 1];
        for(int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i];
        }
        newArr[arr.length] = key;
        return newArr;
    }

}


public class TetrisSettingsFrame extends JPanel{
    TetrisSettings settings;
    public TetrisSettingsFrame(TetrisSettings settings) {
        this.settings = settings;
        init();
        
    }


    public String str(int[] list) { //there has to be a one line solution to this 

        ArrayList<String> keynames = new ArrayList<>();
        
        for(int i : list) {
            keynames.add(KeyEvent.getKeyText(i));
        }
        return String.join(", ", keynames);
    }


    public void init() {

        //TODO: add SettingsSettingsButton to implement editting contorlss
        SettingsButton left = new SettingsButton("left: " + str(settings.MOVE_LEFT), "MOVE_LEFT");
        this.add(left);

        SettingsButton right = new SettingsButton("right: " + str(settings.MOVE_RIGHT), "MOVE_RIGHT");
        this.add(right);

        SettingsButton soft = new SettingsButton("softdrop: " + str(settings.SOFT_DROP), "SOFT_DROP");
        this.add(soft);

        SettingsButton hard = new SettingsButton("harddrop: " + str(settings.HARD_DROP), "HARD_DROP");
        this.add(hard);

        SettingsButton cw = new SettingsButton("clockwise rotation: " + str(settings.ROTATE_CW), "ROTATE_CW");
        this.add(cw);

        SettingsButton ccw = new SettingsButton("counterclockwise rotation: " + str(settings.ROTATE_CCW), "ROTATE_CCW");
        this.add(ccw);

        SettingsButton r180 = new SettingsButton("180 rotation: " + str(settings.ROTATE_180), "ROTATE_180");
        this.add(r180);

        SettingsButton hold = new SettingsButton("hold: " + str(settings.HOLD), "HOLD");
        this.add(hold);

        SettingsButton reset = new SettingsButton("reset: " + str(settings.RESET), "RESET");
        this.add(reset);

        SettingsButton ghost_action = new SettingsButton("ghost: " + str(settings.GHOST_ACTION), "GHOST_ACTION");
        this.add(ghost_action);


    }


}
