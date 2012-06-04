package ead.common.test.model.elements.guievents;

import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.MouseGEvButtonType;
import ead.common.model.elements.guievents.enums.MouseGEvType;
import ead.common.test.EqualsHashCodeTest;

public class MouseGEvTest extends EqualsHashCodeTest<MouseGEv> {

	@Override
	public MouseGEv[] getObjects() {
		MouseGEv[] events = new MouseGEv[] { MouseGEv.MOUSE_DRAG,
				MouseGEv.MOUSE_DRAG, MouseGEv.MOUSE_DROP,
				new MouseGEv(MouseGEvType.DROP, MouseGEvButtonType.NO_BUTTON),
				MouseGEv.MOUSE_LEFT_PRESSED, MouseGEv.MOUSE_LEFT_PRESSED,
				MouseGEv.MOUSE_MOVED,
				new MouseGEv(MouseGEvType.MOVED, MouseGEvButtonType.NO_BUTTON) };
		return events;
	}
}
