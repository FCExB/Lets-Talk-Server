package server;

public class Vector {
	float x, y;

	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public int getXAsInt() {
		return Math.round(x);
	}

	public int getYAsInt() {
		return Math.round(y);
	}

	public float magintude() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public void normalise() {
		float mag = magintude();
		x = x / mag;
		y = y / mag;
	}

	public static Vector sub(Vector one, Vector two) {
		return new Vector(one.x - two.x, one.y - two.y);
	}
}
