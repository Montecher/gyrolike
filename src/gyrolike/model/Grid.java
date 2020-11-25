package gyrolike.model;

import java.util.HashMap;

import gyrolike.model.sprite.Sprite;

public class Grid {
    private Tile[][] tileGrid;
    private HashMap<Sprite, ContinuousPosition> spriteHash;

    private Grid(int width, int height) {
        this.tileGrid = new Tile[width][height];
        this.spriteHash = new HashMap<>();
    }

    public Grid loadLevel(String levelName) {
        // implement here loading logic
        throw new IllegalStateException("unimplemented");
    }
}
