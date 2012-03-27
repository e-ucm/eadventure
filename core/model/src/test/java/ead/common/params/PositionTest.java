package ead.common.params;

import ead.common.util.EAdPosition;

public class PositionTest extends ParamsTest<EAdPosition> {

	@Override
	public EAdPosition buildParam(String data) {
		return new EAdPosition(data);
	}

	@Override
	public EAdPosition defaultValue() {
		return new EAdPosition();
	}

	@Override
	public EAdPosition[] getObjects() {
		EAdPosition[] positions = new EAdPosition[20];
		for (int i = 0; i < positions.length; i += 2) {
			positions[i] = new EAdPosition(i * 3, i * 4,
					(float) (i - 1) * 800.f, (float) (i) * 600.f);
			positions[i + 1] = new EAdPosition(i * 3, i * 4,
					(float) (i - 1) * 800.f, (float) (i) * 600.f);
		}
		return positions;
	}

}
