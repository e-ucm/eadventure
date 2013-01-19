package ead.common.model.elements.huds;

import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.scenes.GhostElement;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.BasicField;
import ead.common.model.elements.variables.SystemFields;
import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.util.EAdPosition.Corner;

public class MouseHud extends GroupElement {

	private static final String CURSOR_ID = "#engine.cursor";

	public MouseHud() {
		this.setId("#predefined.hud.mouse");
		EAdDrawable cursor = new Image("@drawable/default_cursor.png");
		SceneElement mouse = new GhostElement(cursor, null);
		mouse.setId(CURSOR_ID);
		mouse.setInitialEnable(false);
		mouse.setPosition(Corner.TOP_LEFT, 0, 0);

		SceneElementEv followMouseEvent = new SceneElementEv();
		followMouseEvent.addEffect(SceneElementEvType.ALWAYS,
				new ChangeFieldEf(new BasicField<Integer>(mouse,
						SceneElement.VAR_X), SystemFields.MOUSE_X));
		followMouseEvent.addEffect(SceneElementEvType.ALWAYS,
				new ChangeFieldEf(new BasicField<Integer>(mouse,
						SceneElement.VAR_Y), SystemFields.MOUSE_Y));
		mouse.getEvents().add(followMouseEvent);
		getSceneElements().add(mouse);
	}

}
