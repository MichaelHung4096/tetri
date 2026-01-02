package dev.coolname;

public class TetrisStats {
    
    public int lines = 0;
    public double pieces_placed = 0;
    public double keys_pressed = 0;
    public double keys_per_piece = 0.0;
    public int finesse_faults = 0;
    public long ghost_timer = Long.MAX_VALUE;
    public boolean peek = false;
    public int current_keys_pressed = 0;
    public double time = 0;
    public long start;
    public double final_time = 0;
    public double pieces_per_second = 0;
    public double keys_per_second = 0;

    public boolean on_ground = false;
    public long time_since_ground = 0;
    public long total_time_on_ground = 0;
    public double average_time_on_ground = 0;

    public double[] kpm_keys = new double[TetrisFrame.COLS];
    public double[] kpm_minos = new double[TetrisFrame.COLS];
    public boolean[] kpm_detected = new boolean[TetrisFrame.COLS];

    public TetrisStats() {
                for (int i = 0; i < TetrisFrame.COLS; i++) {
            kpm_detected[i] = false;
        }
    }
}
