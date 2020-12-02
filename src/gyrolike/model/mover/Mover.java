package gyrolike.model.mover;

import gyrolike.model.Game;

import java.util.List;

public interface Mover {
    int getInterval();
    void move(Game gameStatus);

    static Mover get(String name, List<String> param) {
        throw new Error("Unimplemented");
    }
}
