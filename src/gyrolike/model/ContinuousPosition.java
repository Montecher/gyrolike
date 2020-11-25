package gyrolike.model;

public class ContinuousPosition {
	private double x, y;

	public ContinuousPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}

	public ContinuousPosition displace(ContinuousDisplacement disp) {
		return new ContinuousPosition(x + disp.getX(), y + disp.getY());
	}
}
