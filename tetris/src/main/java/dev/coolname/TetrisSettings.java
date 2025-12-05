package dev.coolname;

import javax.swing.JPanel;

public class TetrisSettings{
    public int DAS;
    public int TEST;
    public int[] LEFTKEYS;
    public TetrisSettings() {}

    public String toString() {
        return "DAS: " + DAS + " TEST: " + TEST + " LEFTKEYS: " + java.util.Arrays.toString(LEFTKEYS);
    }

}
