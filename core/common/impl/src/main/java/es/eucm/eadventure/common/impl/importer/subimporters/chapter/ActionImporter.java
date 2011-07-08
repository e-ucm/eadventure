package es.eucm.eadventure.common.impl.importer.subimporters.chapter;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.CustomAction;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;

public class ActionImporter implements EAdElementImporter<Action, EAdAction> {

	private StringHandler stringHandler;

	private EffectsImporterFactory effectsImporterFactory;

	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

	/**
	 * Resources importer
	 */
	private ResourceImporter resourceImporter;

	public static final String DRAWABLE_PATH = "@drawable/";

	@Inject
	public ActionImporter(StringHandler stringHandler,
			EffectsImporterFactory effectsImporterFactory,
			ResourceImporter resourceImporter,
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter) {
		this.stringHandler = stringHandler;
		this.effectsImporterFactory = effectsImporterFactory;
		this.conditionsImporter = conditionsImporter;
		this.resourceImporter = resourceImporter;
	}

	@Override
	public EAdAction init(Action oldObject) {
		return new EAdBasicAction(oldObject.getTargetId()
				+ "_action");
	}

	@Override
	public EAdAction convert(Action oldObject, Object object) {
		EAdBasicAction action = (EAdBasicAction) object;

		EAdCondition condition = conditionsImporter.init(oldObject
				.getConditions());
		condition = conditionsImporter.convert(oldObject
				.getConditions(), condition);
		if (condition != null)
			action.setCondition(condition);

		// FIXME ¿Y qué pasa con los NoEffects?

		String actionName = "Action";
		if (oldObject instanceof CustomAction) {
			CustomAction customAction = (CustomAction) oldObject;
			actionName = customAction.getName();
		} else {
			actionName = getActionName(oldObject.getType());
		}

		EAdString actionNameString = new EAdString(stringHandler.getUniqueId());
		stringHandler.addString(actionNameString, actionName);
		action.setName(actionNameString);

		for (Effect e : oldObject.getEffects().getEffects()) {
			EAdEffect effect = effectsImporterFactory.getEffect(e);
			if (effect != null)
				action.getEffects().add(effect);
		}

		// FIXME No effects, keep distance

		if (oldObject instanceof CustomAction) {
			// TODO highlight and pressed are now appearances, but resource
			// converter does not support it.
			// old resources are named "buttonOver" and "buttonPressed"

			Map<String, String> resourcesStrings = new HashMap<String, String>();
			resourcesStrings.put("buttonNormal", EAdBasicAction.appearance);

			Map<String, Class<?>> resourcesClasses = new HashMap<String, Class<?>>();
			resourcesClasses.put("buttonNormal", ImageImpl.class);

			resourceImporter.importResources(action,
					((CustomAction) oldObject).getResources(),
					resourcesStrings, resourcesClasses);

			action.getResources()
					.addAsset(
							action.getHighlightBundle(),
							EAdBasicAction.appearance,
							new ImageImpl(getHighlightDrawablePath(oldObject
									.getType())));
		} else {
			action.getResources().addAsset(action.getNormalBundle(),
					EAdBasicAction.appearance,
					new ImageImpl(getDrawablePath(oldObject.getType())));
			action.getResources()
					.addAsset(
							action.getHighlightBundle(),
							EAdBasicAction.appearance,
							new ImageImpl(getHighlightDrawablePath(oldObject
									.getType())));
			//TODO should add default effects (e.g. grab should place in inventory, if there is no cancel effect?)
		}

		return action;
	}


	public static String getDrawablePath(int actionType) {
		String image = null;
		switch (actionType) {
		case Action.DRAG_TO:
		case Action.GIVE_TO:
		case Action.GRAB:
		case Action.USE:
		case Action.USE_WITH:
			image = "olddefaults/btnHand.png";
			break;
		case Action.EXAMINE:
			image = "olddefaults/btnEye.png";
			break;
		case Action.TALK_TO:
			image = "olddefaults/btnMouth.png";
			break;
		default:
			image = "olddefaults/btnError.png";
		}
		image = DRAWABLE_PATH + image;

		return image;
	}

	public static String getHighlightDrawablePath(int actionType) {
		String image = null;
		switch (actionType) {
		case Action.DRAG_TO:
		case Action.GIVE_TO:
		case Action.GRAB:
		case Action.USE:
		case Action.USE_WITH:
			image = "olddefaults/btnHandHighlighted.png";
			break;
		case Action.EXAMINE:
			image = "olddefaults/btnEyeHighlighted.png";
			break;
		case Action.TALK_TO:
			image = "olddefaults/btnMouthHighlighted.png";
			break;
		default:
			image = "olddefaults/btnError.png";
		}
		image = DRAWABLE_PATH + image;

		return image;

	}

	private String getActionName(int actionType) {
		switch (actionType) {
		case Action.DRAG_TO:
			return "Drag to";
		case Action.EXAMINE:
			return "Examine";
		case Action.GIVE_TO:
			return "Give to";
		case Action.GRAB:
			return "Grab";
		case Action.TALK_TO:
			return "Talk to";
		case Action.USE:
			return "Use";
		case Action.USE_WITH:
			return "Use with";
		default:
			return "Action";
		}
	}

}
