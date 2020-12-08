package gyrolike.model.sprite;

import gyrolike.model.mover.SpriteMover;

public class Player extends Sprite {
    public Player() {
        super(true, false, .8, .8);
    }

    @Override
    public SpriteMover getAi() {
        throw new Error("unimplemented");
    }

	@Override
	public int getImageId() { return faceLeft ? 0 : 1; }

	@Override
	public int getImageCount() { return 2; }
}
