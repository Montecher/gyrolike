package gyrolike.model.mover;

import gyrolike.model.DiscretePosition;
import gyrolike.model.Game;
import gyrolike.model.Tile;

public class ColumnMover extends TileMover {
    private int x, y, height;

    public ColumnMover(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
    }

    @Override
    public void move(Game gameStatus) {
        if (gameStatus.getSelectedColor() == gameStatus.getGrid().getTileAt(x, y)) {
            switch (gameStatus.getColumnDirection()) {
                case UP:
                    if (y + height < gameStatus.getGrid().getHeight() && gameStatus.getGrid().getTileAt(new DiscretePosition(x, y + height)) == Tile.AIR) {
                        for (int i = height; i > 0; i--) {
                            gameStatus.getGrid().setTileAt(new DiscretePosition(x, y + i), gameStatus.getSelectedColor());
                        }
                        gameStatus.getGrid().setTileAt(new DiscretePosition(x, y), Tile.AIR);
                        this.y++;
                    }
                    break;
                case DOWN:
                    if (0 < y && gameStatus.getGrid().getTileAt(new DiscretePosition(x, y - 1)) == Tile.AIR) {
                        for (int i = 0; i < height; i++) {
                            gameStatus.getGrid().setTileAt(new DiscretePosition(x, y + i - 1), gameStatus.getSelectedColor());
                        }
                        gameStatus.getGrid().setTileAt(new DiscretePosition(x, y + height - 1), Tile.AIR);
                        this.y--;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
