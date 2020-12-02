package gyrolike.model.sprite;

import gyrolike.model.mover.SpriteMover;

public class Player extends Sprite {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    public Player() {
        super(true, false, WIDTH, HEIGHT);
    }

    @Override
    public SpriteMover getAi() {
        throw new Error("unimplemented");
    }
}
