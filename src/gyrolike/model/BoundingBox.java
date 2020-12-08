package gyrolike.model;

import java.util.ArrayList;
import java.util.List;

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
		return new ContinuousPosition(x, y+h);
	}
	public ContinuousPosition getTopRight() {
		return new ContinuousPosition(x+w, y+h);
	}
	public ContinuousPosition getBottomLeft() {
		return new ContinuousPosition(x, y);
	}
	public ContinuousPosition getBottomRight() {
		return new ContinuousPosition(x+w, y);
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

	public List<DiscretePosition> getIntersectingCells() {
		List<DiscretePosition> pos = new ArrayList<>();
		for(int x=(int) this.x; x<(int) this.x+this.w+1; x++) {
			for(int y=(int) this.y; y<(int) this.y+this.h+1; y++) {
				DiscretePosition dpos = new DiscretePosition(x, y);
				if(this.intersects(dpos)) pos.add(dpos);
			}
		}
		return pos;
	}
}
