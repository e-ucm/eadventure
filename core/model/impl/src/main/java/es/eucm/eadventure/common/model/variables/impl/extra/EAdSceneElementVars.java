package es.eucm.eadventure.common.model.variables.impl.extra;

import es.eucm.eadventure.common.interfaces.features.Oriented.Orientation;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.elements.EAdSceneElement.CommonStates;
import es.eucm.eadventure.common.model.variables.EAdVarDef;
import es.eucm.eadventure.common.model.variables.impl.EAdElementVarsImpl;
import es.eucm.eadventure.common.model.variables.impl.EAdVarDefImpl;
import es.eucm.eadventure.common.params.geom.EAdPosition;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;

public class EAdSceneElementVars extends EAdElementVarsImpl {

	public static final EAdVarDef<Orientation> VAR_ORIENTATION = new EAdVarDefImpl<Orientation>(
			"orientation", Orientation.class, Orientation.S);

	public static final EAdVarDef<String> VAR_STATE = new EAdVarDefImpl<String>(
			"state", String.class, CommonStates.EAD_STATE_DEFAULT.toString());

	public static final EAdVarDef<Float> VAR_SCALE = new EAdVarDefImpl<Float>(
			"scale", Float.class, 1.0f);

	public static final EAdVarDef<Float> VAR_ALPHA = new EAdVarDefImpl<Float>(
			"alpha", Float.class, 1.0f);

	public static final EAdVarDef<Float> VAR_ROTATION = new EAdVarDefImpl<Float>(
			"rotation", Float.class, 0.0f);

	public static final EAdVarDef<Boolean> VAR_VISIBLE = new EAdVarDefImpl<Boolean>(
			"visible", Boolean.class, Boolean.TRUE);

	public static final EAdVarDef<Integer> VAR_X = new EAdVarDefImpl<Integer>(
			"x", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_Y = new EAdVarDefImpl<Integer>(
			"y", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_WIDTH = new EAdVarDefImpl<Integer>(
			"width", Integer.class, 0);

	public static final EAdVarDef<Integer> VAR_HEIGHT = new EAdVarDefImpl<Integer>(
			"height", Integer.class, 0);

	public static final EAdVarDef<EAdPosition> VAR_POSITION = new EAdVarDefImpl<EAdPosition>(
			"position", EAdPosition.class, new EAdPositionImpl(0, 0));
	
	public static final EAdVarDef<Integer> VAR_TIME_DISPLAYED = new EAdVarDefImpl<Integer>(
			"timeDisplayed", Integer.class, 0);

	public EAdSceneElementVars(EAdElement element) {
		super(element);
		add(VAR_ORIENTATION);
		add(VAR_STATE);
		add(VAR_SCALE);
		add(VAR_ALPHA);
		add(VAR_ROTATION);
		add(VAR_VISIBLE);
		add(VAR_X);
		add(VAR_Y);
		add(VAR_WIDTH);
		add(VAR_HEIGHT);
		add(VAR_POSITION);
		add(VAR_TIME_DISPLAYED);

	}

}
