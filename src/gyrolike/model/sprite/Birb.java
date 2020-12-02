package gyrolike.model.sprite;

import gyrolike.model.mover.BirbMover;
import gyrolike.model.mover.SpriteMover;

public class Birb  extends Enemy{
    private final BirbMover birbMover;

    public Birb() {
        super(true, true, 1, 1);
        this.birbMover = new BirbMover(this);
    }

    @Override
    public SpriteMover getAi() {
        return this.birbMover;
    }
}
