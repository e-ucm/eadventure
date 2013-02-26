package ead.common.model.elements.widgets;

import ead.common.model.assets.drawable.basics.Caption;
import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.assets.text.EAdFont;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.conditions.OperationCond;
import ead.common.model.elements.effects.variables.ChangeFieldEf;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.enums.ConditionedEvType;
import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.common.model.elements.operations.ValueOp;
import ead.common.model.elements.scenes.GroupElement;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.model.params.fills.ColorFill;
import ead.common.model.params.guievents.MouseGEv;
import ead.common.model.params.util.Position;
import ead.common.model.params.variables.EAdVarDef;
import ead.common.model.params.variables.VarDef;

public class CheckBox extends GroupElement {

	public static final String CHECKED_BUNDLE = "checked_bundle";

	public static final EAdVarDef<Boolean> CHECKED = new VarDef<Boolean>(
			"checkbox_checked", Boolean.class, false);

	public CheckBox(boolean checked, String stringId, EAdFont font) {
		setVarInitialValue(CHECKED, checked);
		setVarInitialValue(SceneElement.VAR_BUNDLE_ID,
				(checked ? CHECKED_BUNDLE : SceneElementDef.INITIAL_BUNDLE));
		setAppearance(new Image("@drawable/checkboxoff.png"));
		setAppearance(CHECKED_BUNDLE, new Image("@drawable/checkboxon.png"));

		ConditionedEv event = new ConditionedEv();
		EAdField<Boolean> checkedField = new BasicField<Boolean>(this, CHECKED);
		EAdField<String> bundleField = new BasicField<String>(this,
				SceneElement.VAR_BUNDLE_ID);

		EAdCondition c = new OperationCond(checkedField);
		event.setCondition(c);
		event.addEffect(ConditionedEvType.CONDITIONS_MET, new ChangeFieldEf(
				bundleField, new ValueOp(CHECKED_BUNDLE)));
		event.addEffect(ConditionedEvType.CONDITIONS_UNMET, new ChangeFieldEf(
				bundleField, new ValueOp(SceneElementDef.INITIAL_BUNDLE)));

		getEvents().add(event);
		ChangeFieldEf changeCheck = new ChangeFieldEf(checkedField,
				new NOTCond(c));
		addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, changeCheck);

		Caption text = new Caption(stringId);
		text.setFont(font);
		text.setBubblePaint(ColorFill.WHITE);
		text.setPadding(5);
		text.setTextPaint(ColorFill.BLACK);
		SceneElement e = new SceneElement(text);
		e.addBehavior(MouseGEv.MOUSE_LEFT_PRESSED, changeCheck);
		e.setPosition(new Position(45, 20, 0, 0.5f));
		addSceneElement(e);
	}

}
