package model;

public enum Tile {
    ROPE (false, true, false),
    WALL (true, false, false),
    FLOOR (true, false, false),
    AIR (false, false, false),
    COLUMN (true, false, true),
    PLATFORM(true, false, true);

    private final boolean solid;
    private final boolean rope;
    private final boolean dynamic;

    Tile(boolean solid, boolean rope, boolean dynamic) {
        this.solid = solid;
        this.rope = rope;
        this.dynamic = dynamic;
    }

    boolean isSolid() {
        return solid;
    }

    boolean isRope() {
        return rope;
    }

    boolean isDynamic() {
        return dynamic;
    }
}

