package gyrolike.model.sprite;

import gyrolike.model.mover.BirbMover;
import gyrolike.model.mover.SpriteMover;

public class Birb extends Enemy {
    private final BirbMover birbMover;

    public Birb() {
        super(true, true, 0.8, 0.8);
        this.birbMover = new BirbMover(this);
    }

    @Override
    public SpriteMover getAi() {
        return this.birbMover;
    }

	@Override
	public int getImageId() { return faceLeft ? 0 : 1; }

	@Override
	public int getImageCount() { return 2; }
}
