package gyrolike.model.mover;

import gyrolike.model.*;
import gyrolike.model.sprite.Sprite;

public class BirbMover extends SpriteMover {

    public BirbMover(Sprite sprite) {
        super(sprite);
    }

    @Override
    public int getInterval() {
        return 30;
    }

    @Override
    public void move(Game gameStatus) {
        BoundingBox birbBox = sprite.getBoundingBox(gameStatus.getGrid().getSpritePos(sprite));
        int birbLeft = (int) birbBox.getBottomLeft().getX();
        int birbRight = (int) birbBox.getBottomRight().getX();
        int birbBottom = (int) birbBox.getBottomRight().getY();
        if (sprite.isFaceLeft()) {
            if ((birbLeft == 0 || gameStatus.getGrid().getTileAt(new DiscretePosition(birbLeft-1, birbBottom)) == Tile.AIR) && (birbBottom == 0 || gameStatus.getGrid().getTileAt(new DiscretePosition(birbLeft-1, birbBottom-1)) == Tile.AIR)) {
                gameStatus.getGrid().setSpritePos(sprite, new ContinuousPosition(birbLeft-1, birbBottom));
            } else if ((birbRight == gameStatus.getGrid().getWidth()-1 || gameStatus.getGrid().getTileAt(new DiscretePosition(birbRight+1, birbBottom)) == Tile.AIR) && (birbBottom == 0 || gameStatus.getGrid().getTileAt(new DiscretePosition(birbLeft+1, birbBottom-1)) == Tile.AIR)) {
                sprite.setFaceLeft(false);
                gameStatus.getGrid().setSpritePos(sprite, new ContinuousPosition(birbLeft+1, birbRight));
            } else {
                sprite.setFaceLeft(false);
            }
        } else {
            if ((birbRight == gameStatus.getGrid().getWidth()-1 || gameStatus.getGrid().getTileAt(new DiscretePosition(birbRight+1, birbBottom)) == Tile.AIR) && (birbBottom == 0 || gameStatus.getGrid().getTileAt(new DiscretePosition(birbLeft+1, birbBottom-1)) == Tile.AIR)) {
                gameStatus.getGrid().setSpritePos(sprite, new ContinuousPosition(birbLeft+1, birbRight));
            } else if ((birbLeft == 0 || gameStatus.getGrid().getTileAt(new DiscretePosition(birbLeft-1, birbBottom)) == Tile.AIR) && (birbBottom == 0 || gameStatus.getGrid().getTileAt(new DiscretePosition(birbLeft-1, birbBottom-1)) == Tile.AIR)) {
                gameStatus.getGrid().setSpritePos(sprite, new ContinuousPosition(birbLeft-1, birbBottom));
                sprite.setFaceLeft(true);
            } else {
                sprite.setFaceLeft(true);
            }
        }
    }
}
