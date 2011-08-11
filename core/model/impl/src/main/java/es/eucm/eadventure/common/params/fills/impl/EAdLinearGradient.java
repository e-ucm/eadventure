package es.eucm.eadventure.common.params.fills.impl;

import es.eucm.eadventure.common.params.EAdFill;

/**
 * Linear gradient fill
 * 
 */
public class EAdLinearGradient implements EAdFill {

	private boolean vertical;

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
	public EAdLinearGradient(EAdColor color1, EAdColor color2, boolean vertical) {
		this.color1 = color1;
		this.color2 = color2;
		this.vertical = vertical;
	}

	public EAdLinearGradient(String string) {
		parse(string);
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

	/**
	 * Returns if the gradient is vertical
	 * 
	 * @return
	 */
	public boolean isVertical() {
		return vertical;
	}

	public String toString() {
		return toStringData();
	}

	@Override
	public String toStringData() {
		return color1.toStringData() + ";" + color2.toStringData() + ";"
				+ vertical;
	}

	@Override
	public void parse(String data) {
		String temp[] = data.split(";");
		int i = 0;
		color1 = new EAdColor(temp[i++]);
		color2 = new EAdColor(temp[i++]);
		vertical = Boolean.parseBoolean(temp[i++]);

	}
}
