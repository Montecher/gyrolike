package gyrolike.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private Level currentLevel;
    private Grid grid;
    private int timeRemaining;
    private Direction columnDirection;
    private Tile selectedColor;
    private Map<Key, Boolean> keys = new HashMap<>();
    {
        for(Key k: Key.values()) keys.put(k, false);
    }
    private List<Runnable> tickListeners = new ArrayList<>();

    public Game() {
        this.columnDirection = Direction.NONE;
        this.selectedColor = Tile.COLUMN_RED;
    }

    public void loadLevel(String id) {
        this.currentLevel = Level.getLevel(id);
        this.grid = Grid.loadFrom(this.currentLevel);
        this.timeRemaining = this.currentLevel.time;
    }

    public Direction getColumnDirection() {
        return columnDirection;
    }

    public Grid getGrid() {
        return grid;
    }

    public Tile getSelectedColor() {
        return selectedColor;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setSelectedColor(Tile selectedColor) {
        if (selectedColor == Tile.COLUMN_RED || selectedColor == Tile.COLUMN_BLUE) {
            this.selectedColor = selectedColor;
        } else {
            throw new IllegalArgumentException("not a column !");
        }
    }

    public void setColumnDirection(Direction dir) {
        if(dir == Direction.UP || dir == Direction.DOWN || dir == Direction.NONE) {
            this.columnDirection = dir;
        } else {
            throw new IllegalArgumentException("not a column direction !");
        }
    }

    public void setKeys(Map<Key, Boolean> keys) {
        this.keys = keys;
    }

    public void addTickListener(Runnable listener) {
        this.tickListeners.add(listener);
    }
}
