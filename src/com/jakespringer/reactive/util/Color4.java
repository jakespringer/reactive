package com.jakespringer.reactive.util;

public class Color4 {

	public final static Color4 WHITE = new Color4(1, 1, 1, 1);
	public final static Color4 BLACK = new Color4(0, 0, 0, 1);
	public final static Color4 RED = new Color4(1, 0, 0, 1);
	public final static Color4 GREEN = new Color4(0, 1, 0, 1);
	public final static Color4 BLUE = new Color4(0, 0, 1, 1);
	public final static Color4 PURPLE = new Color4(.5, 0, 1, 1);
	public final static Color4 YELLOW = new Color4(1, 1, 0, 1);
	public final static Color4 ORANGE = new Color4(1, 0.5, 0, 1);
	public final static Color4 TRANSPARENT = new Color4(0, 0, 0, 0);
	public final double r;
	public final double g;
	public final double b;
	public final double a;

	public Color4(double r, double g, double b, double a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public Color4(double r, double g, double b) {
		this(r, g, b, 1);
	}

	public Color4() {
		this(1, 1, 1, 1);
	}

	public static Color4 gray(double d) {
		return new Color4(d, d, d);
	}

	public Color4 multiply(double d) {
		return new Color4(r * d, g * d, b * d, a);
	}

	public static Color4 random() {
		return new Color4(Math.random(), Math.random(), Math.random());
	}

	@Override
	public String toString() {
		return "Color[" + r + ", " + g + ", " + b + ", " + a + "]";
	}

	public Color4 withR(double r) {
		return new Color4(r, g, b, a);
	}

	public Color4 withG(double g) {
		return new Color4(r, g, b, a);
	}

	public Color4 withB(double b) {
		return new Color4(r, g, b, a);
	}

	public Color4 withA(double a) {
		return new Color4(r, g, b, a);
	}
}
