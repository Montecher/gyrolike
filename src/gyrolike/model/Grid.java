package gyrolike.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gyrolike.model.sprite.Sprite;

public class Grid {
    private Tile[][] tileGrid;
    private HashMap<Sprite, ContinuousPosition> spriteHash;
    int width, height;

    private Grid(int width, int height) {
        this.tileGrid = new Tile[width][height];
        this.spriteHash = new HashMap<>();
        this.width = width;
        this.height = height;
    }

    public Grid loadLevel(String levelName) {
        // implement here loading logic
        throw new IllegalStateException("unimplemented");
    }

    public ContinuousPosition getSpritePos(Sprite sprite) {
        return this.spriteHash.get(sprite);
    }

    public void setSpritePos(Sprite sprite, ContinuousPosition pos) {
        this.spriteHash.put(sprite, pos);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Set<Sprite> getIntersectingSprites(DiscretePosition pos) {
        Set<Sprite> intersecting = new HashSet<>();

        for (Map.Entry<Sprite, ContinuousPosition> e: spriteHash.entrySet()) {
            if (e.getKey().getBoundingBox(e.getValue()).intersects(pos)) {
                intersecting.add(e.getKey());
            }
        }

        return intersecting;
    }

    public Set<Sprite> getIntersectingSprites(Sprite sprite) {
        Set<Sprite> intersecting = new HashSet<>();
        BoundingBox spriteBox = sprite.getBoundingBox(this.getSpritePos(sprite));

        for (Map.Entry<Sprite, ContinuousPosition> e: spriteHash.entrySet()) {
            if (e.getKey().getBoundingBox(e.getValue()).intersects(spriteBox)) {
                intersecting.add(e.getKey());
            }
        }

        return intersecting;
    }

    public Tile getTileAt(DiscretePosition coord) {
        return this.tileGrid[coord.getX()][coord.getY()];
    }

    public void setTileAt(DiscretePosition goal, Tile tile) {
        this.tileGrid[goal.getX()][goal.getY()] = tile;
    }
}
