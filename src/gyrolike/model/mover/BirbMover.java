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
            if (birbLeft == 0) {
                    sprite.setFaceLeft(false);
            } else if (gameStatus.getGrid().getTileAt(birbLeft-1, birbBottom) == Tile.AIR || gameStatus.getGrid().getTileAt(birbLeft-1, birbBottom) == Tile.ROPE) {
                if (birbBottom == 0) {
                    gameStatus.getGrid().setSpritePos(sprite, new ContinuousPosition(birbLeft-1, birbBottom));
                } else if (!(gameStatus.getGrid().getTileAt(birbLeft-1, birbBottom-1) == Tile.AIR || gameStatus.getGrid().getTileAt(birbLeft-1, birbBottom-1) == Tile.ROPE)) {
                    gameStatus.getGrid().setSpritePos(sprite, new ContinuousPosition(birbLeft-1, birbBottom));
                } else {
                    sprite.setFaceLeft(false);
                }
            } else {
                sprite.setFaceLeft(false);
            }
        } else {
            if (birbLeft == gameStatus.getGrid().getWidth()-1) {
                sprite.setFaceLeft(true);
            } else if (gameStatus.getGrid().getTileAt(birbLeft+1, birbBottom) == Tile.AIR || gameStatus.getGrid().getTileAt(birbLeft+1, birbBottom) == Tile.ROPE) {
                if (birbBottom == 0) {
                    gameStatus.getGrid().setSpritePos(sprite, new ContinuousPosition(birbLeft+1, birbBottom));
                } else if (!(gameStatus.getGrid().getTileAt(birbLeft+1, birbBottom-1) == Tile.AIR || gameStatus.getGrid().getTileAt(birbLeft+1, birbBottom-1) == Tile.ROPE)) {
                    gameStatus.getGrid().setSpritePos(sprite, new ContinuousPosition(birbLeft+1, birbBottom));
                } else {
                    sprite.setFaceLeft(true);
                }
            } else {
                sprite.setFaceLeft(true);
            }
        }
    }
}
