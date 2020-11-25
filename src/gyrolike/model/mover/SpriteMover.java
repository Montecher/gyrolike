package gyrolike.model.mover;

import gyrolike.model.Game;
import gyrolike.model.sprite.Sprite;

public abstract class SpriteMover implements Mover{
    protected Sprite sprite;

    protected SpriteMover(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public int getInterval() {
        return 1;
    }
}
