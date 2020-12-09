package gyrolike.model.sprite;

import gyrolike.model.mover.SpriteMover;

public class Bomb extends Sprite {
	public Bomb() {
		super(false, false, .8, .8);
	}

	@Override
	public SpriteMover getAi() {
		return null;
	}

	@Override
	public int getImageId() {
		return 0;
	}

	@Override
	public int getImageCount() {
		return 1;
	}
}
