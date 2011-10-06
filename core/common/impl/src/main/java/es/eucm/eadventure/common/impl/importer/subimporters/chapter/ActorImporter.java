/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.eadventure.common.impl.importer.subimporters.chapter;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.Element;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.actions.impl.EAdBasicAction;
import es.eucm.eadventure.common.model.conditions.impl.ANDCondition;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdShowSceneElement;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Caption;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.CaptionImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public abstract class ActorImporter<P extends Element> implements
		EAdElementImporter<P, EAdActor> {

	protected StringHandler stringHandler;

	protected ResourceImporter resourceImporter;

	protected Map<String, Object> objectClasses;

	protected Map<String, String> properties;

	protected EAdElementFactory elementFactory;

	protected EAdElementImporter<Action, EAdAction> actionImporter;

	protected P element;

	@Inject
	public ActorImporter(StringHandler stringHandler,
			ResourceImporter resourceImporter,
			EAdElementFactory elementFactory,
			EAdElementImporter<Action, EAdAction> actionImporter) {
		this.stringHandler = stringHandler;
		this.resourceImporter = resourceImporter;
		this.elementFactory = elementFactory;
		this.actionImporter = actionImporter;
	}

	@Override
	public EAdActor init(P oldObject) {
		this.element = oldObject;
		return new EAdBasicActor(oldObject.getId());
	}

	@Override
	public EAdActor convert(P oldObject, Object object) {
		EAdBasicActor actor = (EAdBasicActor) object;
		elementFactory.getCurrentChapterModel().getActors().add(actor);

		stringHandler.setString(actor.getName(), oldObject.getName());
		stringHandler.setString(actor.getDescription(), oldObject.getDescription());
		stringHandler.setString(actor.getDetailedDescription(), oldObject.getDetailedDescription());
		stringHandler.setString(actor.getDocumentation(), oldObject.getDocumentation());

		initResourcesCorrespondencies();

		resourceImporter.importResources(actor, oldObject.getResources(),
				properties, objectClasses);

		addActions(oldObject, actor, actionImporter, stringHandler);

		return actor;
	}

	public static <P extends Element> void addActions(P oldObject,
			EAdBasicActor actor,
			EAdElementImporter<Action, EAdAction> actionImporter,
			StringHandler stringHandler) {
		// Add examine action if it's not defined in oldObject actions
		boolean addExamine = true;

		HashMap<String, EAdCondition> previousConditions = new HashMap<String, EAdCondition>();

		for (Action a : oldObject.getActions()) {
			if (addExamine && a.getType() == Action.EXAMINE)
				addExamine = false;

			EAdAction action = actionImporter.init(a);
			action = ((ActionImporter) actionImporter)
					.convert(a, action, actor);
			actor.getActions().add(action);

			String name = stringHandler.getString(action.getName());
			if (previousConditions.containsKey(name)) {
				EAdCondition conditions = action.getCondition();
				action.setCondition(new ANDCondition(conditions,
						previousConditions.get(name)));
				conditions = new ANDCondition(new NOTCondition(conditions),
						previousConditions.get(name));
				previousConditions.remove(name);
				previousConditions.put(name, conditions);
			} else {
				previousConditions.put(name,
						new NOTCondition(action.getCondition()));
			}
		}

		if (addExamine) {
			EAdBasicAction examineAction = new EAdBasicAction(actor.getId()
					+ "_action_examinate");

			EAdShowSceneElement effect = new EAdShowSceneElement(
					examineAction.getId() + "_showText");

			Caption caption = new CaptionImpl(actor.getDescription());
			effect.setCaption(caption, 300, 300);

			examineAction.getEffects().add(effect);

			examineAction.getResources().addAsset(
					examineAction.getNormalBundle(),
					EAdBasicAction.appearance,
					new ImageImpl(ActionImporter
							.getDrawablePath(Action.EXAMINE)));
			examineAction.getResources().addAsset(
					examineAction.getHighlightBundle(),
					EAdBasicAction.appearance,
					new ImageImpl(ActionImporter
							.getHighlightDrawablePath(Action.EXAMINE)));

			actor.getActions().add(examineAction);

		}

	}

	public abstract void initResourcesCorrespondencies();

}
