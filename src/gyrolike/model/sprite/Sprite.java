package gyrolike.model.sprite;

abstract public class Sprite {
    protected boolean gravity;
    protected boolean ai;
//    protected Producer<SpriteAI> aiType;
    protected double width;
    protected double height;

    public boolean hasGravity() {
        return gravity;
    }

    public boolean hasAi() {
        return ai;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Sprite(boolean gravity, boolean ai, double width, double height) {
        this.gravity = gravity;
        this.ai = ai;
        this.width = width;
        this.height = height;
    }
}
