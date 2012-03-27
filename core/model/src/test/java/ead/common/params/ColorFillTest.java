package ead.common.params;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ead.common.params.fills.ColorFill;

public class ColorFillTest extends ParamsTest<ColorFill> {

	@Test
	public void testMaxMin() {
		// Negative values and values over 255 must be normalized
		ColorFill c = new ColorFill(-1, -20, 1000);
		assertTrue(c.equals(new ColorFill(0, 0, 255)));
		c = new ColorFill(20, 1000, -1020);
		assertTrue(c.equals(new ColorFill(20, 255, 0)));

	}

	@Override
	public ColorFill[] getObjects() {
		ColorFill[] colors = new ColorFill[20];
		for (int i = 0; i < colors.length; i += 2) {
			colors[i] = new ColorFill(1 * i, 7 * i, i * 10);
			colors[i + 1] = new ColorFill(1 * i, 7 * i, i * 10);
		}
		return colors;
	}
	
	@Test
	public void testWellKnownColors(){
		assertTrue(new ColorFill("0xFF0000FF").equals(ColorFill.RED));
		assertTrue(new ColorFill("0xFFFFFFFF").equals(ColorFill.WHITE));
		assertTrue(new ColorFill("0x000000FF").equals(ColorFill.BLACK));
		assertTrue(new ColorFill("0x0000FFFF").equals(ColorFill.BLUE));
		assertTrue(new ColorFill("0x00FF00FF").equals(ColorFill.GREEN));
	}

	@Override
	public ColorFill buildParam(String data) {
		return new ColorFill(data);
	}

	@Override
	public ColorFill defaultValue() {
		return ColorFill.BLACK;
	}

}
