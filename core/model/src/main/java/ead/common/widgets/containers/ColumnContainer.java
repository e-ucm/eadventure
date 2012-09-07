package ead.common.widgets.containers;

import ead.common.model.elements.conditions.EmptyCond;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.SceneElementEv;
import ead.common.model.elements.events.enums.SceneElementEvType;
import ead.common.model.elements.scene.EAdSceneElement;
import ead.common.model.elements.scenes.ComplexSceneElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.variables.BasicField;
import ead.common.util.EAdPosition.Corner;

public class ColumnContainer extends ComplexSceneElement {

	private EAdSceneElement lastAdded;
	
	public ColumnContainer( ){
		this.setDragCond(EmptyCond.TRUE_EMPTY_CONDITION);
	}

	public void add(EAdSceneElement element) {
		element.setPosition(Corner.TOP_LEFT, 0, 0);
		if (lastAdded != null) {
			BasicField<Integer> fieldBottom = new BasicField<Integer>(
					lastAdded, SceneElement.VAR_BOTTOM);
			BasicField<Integer> fieldY = new BasicField<Integer>(element,
					SceneElement.VAR_Y);

			SceneElementEv event = new SceneElementEv();
			ChangeFieldEf updateField = new ChangeFieldEf(fieldY, fieldBottom);
			event.addEffect(SceneElementEvType.ALWAYS, updateField);
			element.getEvents().add(event);
		}
		getSceneElements().add(element);
		lastAdded = element;
	}

}
