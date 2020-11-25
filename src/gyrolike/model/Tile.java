package gyrolike.model;

public enum Tile {
    ROPE (false, true, false),
    WALL (true, false, false),
    FLOOR (true, false, false),
    AIR (false, false, false),
    COLUMN_RED (true, false, true),
    COLUMN_BLUE (true, false, true),
    PLATFORM(true, false, true);

    public final boolean solid;
    public final boolean rope;
    public final boolean dynamic;

    Tile(boolean solid, boolean rope, boolean dynamic) {
        this.solid = solid;
        this.rope = rope;
        this.dynamic = dynamic;
    }
}

