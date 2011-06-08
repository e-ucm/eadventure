package es.eucm.eadventure.common.impl.importer.subimporters.chapter;

import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.Element;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.effects.impl.text.EAdShowText;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;

public abstract class ActorImporter<P extends Element> implements
		Importer<P, EAdActor> {

	protected StringHandler stringHandler;

	protected ResourceImporter resourceImporter;

	protected Map<String, Class<?>> classes;

	protected Map<String, String> properties;

	protected EAdElementFactory elementFactory;

	protected Importer<Action, EAdAction> actionImporter;

	@Inject
	public ActorImporter(StringHandler stringHandler,
			ResourceImporter resourceImporter,
			EAdElementFactory elementFactory,
			Importer<Action, EAdAction> actionImporter) {
		this.stringHandler = stringHandler;
		this.resourceImporter = resourceImporter;
		this.elementFactory = elementFactory;
		this.actionImporter = actionImporter;
	}

	@Override
	public EAdActor convert(P oldObject) {
		EAdBasicActor actor = (EAdBasicActor) elementFactory
				.getActorByOldId(oldObject.getId());

		actor.setName(new EAdString(stringHandler.getUniqueId()));
		stringHandler.addString(actor.getName(), oldObject.getName());

		actor.setDescription(new EAdString(stringHandler.getUniqueId()));
		stringHandler.addString(actor.getDescription(),
				oldObject.getDescription());

		actor.setDetailedDescription(new EAdString(stringHandler.getUniqueId()));
		stringHandler.addString(actor.getDetailedDescription(),
				oldObject.getDetailedDescription());

		actor.setDocumentation(new EAdString(stringHandler.getUniqueId()));
		stringHandler.addString(actor.getDocumentation(),
				oldObject.getDocumentation());

		initResourcesCorrespondencies();

		resourceImporter.importResources(actor, oldObject.getResources(),
				properties, classes);

		addActions(oldObject, actor);

		return actor;
	}

	protected void addActions(P oldObject, EAdBasicActor actor) {
		// Add examine action if it's not defined in oldObject actions
		boolean addExamine = true;

		for (Action a : oldObject.getActions()) {
			if (addExamine && a.getType() == Action.EXAMINE)
				addExamine = false;

			EAdAction action = actionImporter.convert(a);
			actor.getActions().add(action);
		}

		if (addExamine) {
			EAdBasicAction examineAction = new EAdBasicAction(actor.getId()
					+ "_action_examinate");
			EAdString description = new EAdString(stringHandler.getUniqueId());
			stringHandler.addString(description, oldObject.getDescription());

			EAdShowText effect = new EAdShowText(examineAction.getId()
					+ "_showText");

			Caption caption = new CaptionImpl(description);
			effect.setCaption(caption);

			examineAction.getEffects().add(effect);

			
			examineAction.getResources().addAsset(examineAction.getNormalBundle(), EAdBasicAction.appearance, new ImageImpl(ActionImporter.getDrawablePath(Action.EXAMINE)));
			examineAction.getResources().addAsset(examineAction.getHighlightBundle(), EAdBasicAction.appearance, new ImageImpl(ActionImporter.getHighlightDrawablePath(Action.EXAMINE)));

			actor.getActions().add(examineAction);

		}

	}

	public abstract void initResourcesCorrespondencies();

	@Override
	public boolean equals(P oldObject, EAdActor newObject) {
		// TODO Implement equals actor

		// resourceImporter.equals(oldObject.getResources(),
		// newObject.getResources());
		return false;
	}

}
