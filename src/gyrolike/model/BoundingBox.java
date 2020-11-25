package gyrolike.model;

public class BoundingBox {
	private double x, y, w, h;

	public BoundingBox(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public BoundingBox(ContinuousPosition pos, double w, double h) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.w = w;
		this.h = h;
	}

	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	public double getWidth() {
		return this.w;
	}
	public double getHeight() {
		return this.h;
	}

	public ContinuousPosition getTopLeft() {
		return new ContinuousPosition(x, y);
	}
	public ContinuousPosition getTopRight() {
		return new ContinuousPosition(x+w, y);
	}
	public ContinuousPosition getBottomLeft() {
		return new ContinuousPosition(x, y+h);
	}
	public ContinuousPosition getBottomRight() {
		return new ContinuousPosition(x+w, y+h);
	}

	public boolean intersects(DiscretePosition pos) {
		return intersects(new BoundingBox(pos.getX(), pos.getY(), 1., 1.));
	}
	public boolean intersects(ContinuousPosition pos) {
		return pos.getX() >= x && pos.getX() <= x + w && pos.getY() >= y && pos.getY() <= y + h;
	}
	public boolean intersects(BoundingBox o) {
		return intersects(o.getTopLeft()) || intersects(o.getTopRight()) || intersects(o.getBottomLeft()) || intersects(o.getBottomRight());
	}
}
