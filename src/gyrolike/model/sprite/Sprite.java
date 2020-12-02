package gyrolike.model.sprite;

import gyrolike.model.BoundingBox;
import gyrolike.model.ContinuousPosition;
import gyrolike.model.mover.SpriteMover;

abstract public class Sprite {
    protected boolean gravity;
    protected boolean ai;
//    protected Producer<SpriteAI> aiType;
    protected double width;
    protected double height;
    protected boolean faceLeft;

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
        this.faceLeft = false;
    }

    public BoundingBox getBoundingBox(ContinuousPosition pos) {
        return new BoundingBox(pos, this.getWidth(), this.getHeight());
    }

	public static Sprite get(String name) {
		try {
			Class<?> clazz = Class.forName("gyrolike.model.sprite."+name);
			return (Sprite) clazz.getConstructor().newInstance();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public abstract SpriteMover getAi();

    public boolean isFaceLeft() {
        return faceLeft;
    }

    public void setFaceLeft(boolean faceLeft) {
        this.faceLeft = faceLeft;
    }
}
