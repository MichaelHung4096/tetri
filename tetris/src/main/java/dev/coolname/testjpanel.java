package dev.coolname;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;

public class testjpanel extends JPanel{


    public int index = 1;
        @Override
        protected void paintComponent(Graphics g) {
            // Call the superclass's paintComponent for proper painting and background clearing
            super.paintComponent(g); 

            index++;

            // Custom drawing operations
            g.setColor(Color.BLUE);
            g.fillRect(50, 50, 100, 75); // Draw a blue rectangle
            g.setColor(Color.WHITE);
            g.drawString("Hello Swing!  " + index, 60, 150); // Draw red text

            System.out.println(index);
        }

}
