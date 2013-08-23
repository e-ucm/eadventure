package es.eucm.ead.model.elements.predef.sceneelements;

import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.elements.effects.variables.ChangeFieldEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.operations.MathOp;
import es.eucm.ead.model.elements.operations.SystemFields;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.params.util.Position;

public class LoaderSceneElement extends SceneElement {

	public static final String ID = "#engine.loader";

	public LoaderSceneElement() {
		super(new Image("@drawable/loader.png"));
		this.setId(ID);
		setPosition(Position.Corner.CENTER, 30, 30);
		setInitialScale(0.5f);
		SceneElementEv event = new SceneElementEv();
		MathOp rotate = new MathOp("[0] + 0.5*[1]");
		rotate.addOperation(getField(SceneElement.VAR_ROTATION));
		rotate.addOperation(SystemFields.ELAPSED_TIME_PER_UPDATE);
		event.addEffect(SceneElementEvType.ALWAYS, new ChangeFieldEf(
				getField(SceneElement.VAR_ROTATION), rotate));
		getEvents().add(event);
		this.setInitialVisible(false);
	}
}
