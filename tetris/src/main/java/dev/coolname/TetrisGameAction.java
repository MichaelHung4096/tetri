package dev.coolname;

public class TetrisGameAction {
    long time;
    String action;
    String type;
    public TetrisGameAction(long time, String action, String type) {
        this.time = time;
        this.action = action;
        this.type = type;

    }

    public String toString() {
        return "{\"time\": " + time + ", \"action\": \"" + action + "\", \"type\": \"" + type + "\"}";
    }

    public String getAction() { //idk how do that bro :(
        return action;
    }


    // MOVE_LEFT,
    // MOVE_RIGHT,
    // SOFT_DROP,
    // HARD_DROP,
    // ROTATE_CW,
    // ROTATE_CCW, 
    // ROTATE_180,
    // HOLD,


    /*
    final product should be something like:
    {
    time: 123, //idk what format time should be in but yea
    action: "moveLeft",
    type:"down"
    }

    history:
    user settings : {settings},
    history: [list of game actions like above],
    game_data : {stuff like kpp mkpp etc.} //could incorporate per bag variations of each stat would be cool
    game_data should also include seed
     */
}
