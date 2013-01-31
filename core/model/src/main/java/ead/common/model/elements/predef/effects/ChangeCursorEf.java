package ead.common.model.elements.predef.effects;

import ead.common.model.elements.BasicElement;
import ead.common.model.elements.huds.MouseHud;

public class ChangeCursorEf extends ChangeAppearanceEf {

	public ChangeCursorEf(String bundle) {
		super(new BasicElement(MouseHud.CURSOR_ID), bundle);
	}

}
