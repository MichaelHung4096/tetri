package dev.coolname;

public enum TetrisGameAction {
    MOVE_LEFT,
    MOVE_RIGHT,
    SOFT_DROP,
    HARD_DROP,
    ROTATE_CW,
    ROTATE_CCW, 
    ROTATE_180,
    HOLD,


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
