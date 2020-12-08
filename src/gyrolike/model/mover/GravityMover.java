package gyrolike.model.mover;

import gyrolike.model.*;
import gyrolike.model.sprite.Sprite;

public class GravityMover extends SpriteMover {
    private static final double G = 0.1;

    public GravityMover(Sprite sprite) {
        super(sprite);
    }


    @Override
    public void move(Game gamaStatus) {
		if(gamaStatus.getGrid().touches(sprite, t -> t.rope)) return;
		if(gamaStatus.getGrid().getSpritePos(sprite).getY() <= 0) {
			gamaStatus.getGrid().setSpritePos(sprite, new ContinuousPosition(gamaStatus.getGrid().getSpritePos(sprite).getX(), 0));
			return;
		}

        BoundingBox spriteBox = this.sprite.getBoundingBox(gamaStatus.getGrid().getSpritePos(sprite));

        DiscretePosition leftCheck = new DiscretePosition((int) spriteBox.getBottomLeft().getX(), (int) (spriteBox.getBottomLeft().getY() - G));
        DiscretePosition rightCheck = new DiscretePosition((int) spriteBox.getBottomRight().getX(), (int) (spriteBox.getBottomRight().getY() - G));
        if (gamaStatus.getGrid().getTileAt(leftCheck) == Tile.AIR || gamaStatus.getGrid().getTileAt(rightCheck) == Tile.AIR) {
            gamaStatus.getGrid().setSpritePos(this.sprite, new ContinuousPosition(spriteBox.getX(), spriteBox.getY() - G));
        } else {
            gamaStatus.getGrid().setSpritePos(this.sprite, new ContinuousPosition(spriteBox.getX(), (int) spriteBox.getY()));
        }
    }
}
