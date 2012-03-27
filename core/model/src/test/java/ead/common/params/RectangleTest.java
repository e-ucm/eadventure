package ead.common.params;

import ead.common.util.EAdRectangle;

public class RectangleTest extends ParamsTest<EAdRectangle> {

	@Override
	public EAdRectangle buildParam(String data) {
		return new EAdRectangle(data);
	}

	@Override
	public EAdRectangle defaultValue() {
		return new EAdRectangle();
	}

	@Override
	public EAdRectangle[] getObjects() {
		EAdRectangle[] rectangles = new EAdRectangle[20];
		for (int i = 0; i < rectangles.length; i += 2) {
			rectangles[i] = new EAdRectangle(i - 2, i * 50, i * 7, i * 9);
			rectangles[i + 1] = new EAdRectangle(i - 2, i * 50, i * 7, i * 9);
		}
		return rectangles;
	}

}
