package ead.common.params;

import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;

public class LinearGradientFillTest extends ParamsTest<LinearGradientFill> {

	@Override
	public LinearGradientFill[] getObjects() {
		LinearGradientFill[] fills = new LinearGradientFill[20];
		for (int i = 0; i < fills.length; i += 2) {
			ColorFill c1 = new ColorFill(i * 3, i * 5, i * 7);
			ColorFill c2 = new ColorFill(i * 4, i * 1, i * 8);
			float x1 = i * 50;
			float y1 = i * 100;
			float x2 = i * 20;
			float y2 = i * 70;
			LinearGradientFill fill1 = new LinearGradientFill(c1, c2, x1, y1,
					x2, y2);
			ColorFill c3 = new ColorFill(i * 3, i * 5, i * 7);
			ColorFill c4 = new ColorFill(i * 4, i * 1, i * 8);
			LinearGradientFill fill2 = new LinearGradientFill(c3, c4, x1, y1,
					x2, y2);
			fills[i] = fill1;
			fills[i + 1] = fill2;
		}

		return fills;
	}

	@Override
	public LinearGradientFill buildParam(String data) {
		return new LinearGradientFill(data);
	}

	@Override
	public LinearGradientFill defaultValue() {
		return LinearGradientFill.BLACK;
	}

}
