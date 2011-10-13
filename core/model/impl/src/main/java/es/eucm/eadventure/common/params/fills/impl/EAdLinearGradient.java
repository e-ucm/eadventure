package es.eucm.eadventure.common.params.fills.impl;

import es.eucm.eadventure.common.params.EAdParamImpl;
import es.eucm.eadventure.common.params.paint.EAdFill;

/**
 * Linear gradient fill
 * 
 */
public class EAdLinearGradient extends EAdParamImpl implements EAdFill {

	public static final String SEPARATOR = ";";

	private float x0, y0, x1, y1;

	private EAdColor color1;

	private EAdColor color2;

	/**
	 * Constructs a linear gradient fill.
	 * 
	 * @param color1
	 *            Start color
	 * @param color2
	 *            End color
	 * @param vertical
	 *            if the gradient is vertical (or horizontal)
	 */
	public EAdLinearGradient(EAdColor color1, EAdColor color2, float x0,
			float y0, float x1, float y1) {
		this.color1 = color1;
		this.color2 = color2;
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;
	}

	public EAdLinearGradient(String string) {
		parse(string);
	}

	public EAdLinearGradient(EAdColor c1, EAdColor c2, int width, int height,
			boolean vertical) {
		this(c1, c2, 0, 0, vertical ? 0 : width, vertical ? height : 0);
	}

	public EAdLinearGradient(EAdColor c1, EAdColor c2, int width, int height) {
		this(c1, c2, width, height, true);
	}

	/**
	 * Returns the first color
	 * 
	 * @return
	 */
	public EAdColor getColor1() {
		return color1;
	}

	/**
	 * Returns the second color
	 * 
	 * @return
	 */
	public EAdColor getColor2() {
		return color2;
	}

	public float getX0() {
		return x0;
	}

	public float getX1() {
		return x1;
	}

	public float getY0() {
		return y0;
	}

	public float getY1() {
		return y1;
	}

	@Override
	public EAdFill getBorder() {
		return null;
	}

	@Override
	public EAdFill getFill() {
		return this;
	}

	@Override
	public int getBorderWidth() {
		return 0;
	}

	public String toString() {
		return toStringData();
	}

	@Override
	public String toStringData() {
		return color1.toStringData() + SEPARATOR + color2.toStringData()
				+ SEPARATOR + x0 + SEPARATOR + y0 + SEPARATOR + x1 + SEPARATOR
				+ y1;
	}

	@Override
	public void parse(String data) {
		String temp[] = data.split(SEPARATOR);
		int i = 0;
		color1 = new EAdColor(temp[i++]);
		color2 = new EAdColor(temp[i++]);
		x0 = Float.parseFloat(temp[i++]);
		y0 = Float.parseFloat(temp[i++]);
		x1 = Float.parseFloat(temp[i++]);
		y1 = Float.parseFloat(temp[i++]);

	}

	public boolean isVertical() {
		return false;
	}
}
