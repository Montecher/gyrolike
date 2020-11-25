package gyrolike.model.mover;

import gyrolike.model.Game;
import gyrolike.model.sprite.Sprite;

public abstract class SpriteMover implements Mover{
    protected Sprite sprite;

    @Override
    public abstract void move(Game gamaStatus);
}
