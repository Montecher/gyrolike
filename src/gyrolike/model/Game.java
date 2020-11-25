package gyrolike.model;

public class Game {
//    private Level currentLevel;
    private Grid grid;
    private int timeRemaing;
    private Direction columnDirection;
    private Tile selectedColor;

    public Game(/* String levelName */) {
        this.columnDirection = Direction.NONE;
        this.selectedColor = Tile.COLUMN_RED;
        // this.currentLevel = LevelManager.getLevel(levelName);
        // this.grid = this.currentLevel.getGrid()
        // this.timeRemaining = this.currentLevel.getTime()
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

    public void setSelectedColor(Tile selectedColor) {
        if (selectedColor == Tile.COLUMN_RED || selectedColor == Tile.COLUMN_BLUE) {
            this.selectedColor = selectedColor;
        } else {
            throw new IllegalArgumentException("not a column !");
        }
    }
}
