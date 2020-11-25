package gyrolike.model.mover;

import gyrolike.model.Game;

public interface Mover {
    int getInterval();
    void move(Game gameStatus);
}
