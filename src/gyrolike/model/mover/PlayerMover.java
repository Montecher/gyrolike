package gyrolike.model.mover;

import gyrolike.model.sprite.*;
import gyrolike.model.*;
import static gyrolike.model.Key.*;

public class PlayerMover extends SpriteMover {
	public static final double PLAYER_SPEED = 1;

	public PlayerMover(Sprite sprite) {
		super(sprite);
	}

	@Override
	public int getInterval() {
		return 10;
	}

	@Override
	public void move(Game game) {
		System.out.println("here");

		boolean left = game.hasKey(LEFT) && !game.hasKey(RIGHT);
		boolean right = game.hasKey(RIGHT) && !game.hasKey(LEFT);
		boolean up = game.hasKey(UP) && !game.hasKey(DOWN);
		boolean down = game.hasKey(DOWN) && !game.hasKey(UP);

		if(right) sprite.setFaceLeft(false);
		if(left) sprite.setFaceLeft(true);

		ContinuousDisplacement horiz = new ContinuousDisplacement(left ? -PLAYER_SPEED : right ? PLAYER_SPEED : 0, 0);
		game.getGrid().move(sprite, horiz);

		if(game.getGrid().touches(sprite, t -> t.rope)) {
			ContinuousDisplacement vert = new ContinuousDisplacement(0, up ? PLAYER_SPEED : down ? -PLAYER_SPEED : 0);
			game.getGrid().move(sprite, vert);
		}
	}
}
