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

import java.util.Map;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.Description;
import es.eucm.eadventure.common.data.chapter.elements.Element;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.actions.EAdAction;
import es.eucm.eadventure.common.model.effects.impl.EAdActorActionsEffect;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.resources.StringHandler;

public abstract class ActorImporter<P extends Element> implements
		EAdElementImporter<P, EAdSceneElementDef> {

	protected StringHandler stringHandler;

	protected ResourceImporter resourceImporter;

	protected Map<String, Object> objectClasses;

	protected Map<String, String> properties;

	protected EAdElementFactory elementFactory;

	protected ActionImporter actionImporter;

	protected P element;

	protected EAdElementFactory factory;

	@Inject
	public ActorImporter(StringHandler stringHandler,
			ResourceImporter resourceImporter,
			EAdElementFactory elementFactory,
			EAdElementImporter<Action, EAdAction> actionImporter,
			EAdElementFactory factory) {
		this.stringHandler = stringHandler;
		this.resourceImporter = resourceImporter;
		this.elementFactory = elementFactory;
		this.actionImporter = (ActionImporter) actionImporter;
		this.factory = factory;
	}

	@Override
	public EAdSceneElementDef init(P oldObject) {
		this.element = oldObject;
		return new EAdSceneElementDefImpl();
	}

	@Override
	public EAdSceneElementDef convert(P oldObject, Object object) {
		EAdSceneElementDefImpl actor = (EAdSceneElementDefImpl) object;
		actor.setId(oldObject.getId());
		elementFactory.getCurrentChapterModel().getActors().add(actor);

		// Add strings
		// FIXME multiple descriptions not supported
		if (oldObject.getDescriptions().size() > 0) {
			Description desc = oldObject.getDescription(0);
			stringHandler.setString(actor.getName(), desc.getName());
			stringHandler
					.setString(actor.getDesc(), desc.getDescription());
			stringHandler.setString(actor.getDetailDesc(),
					desc.getDetailedDescription());
			stringHandler.setString(actor.getDoc(),
					oldObject.getDocumentation());
		}

		// Add resources
		initResourcesCorrespondencies();
		resourceImporter.importResources(actor, oldObject.getResources(),
				properties, objectClasses);

		// Add actions
		addActionsEffect(oldObject, actor);

		// Add drag
		// oldObject.isReturnsWhenDragged()

		return actor;
	}

	public abstract void initResourcesCorrespondencies();

	protected void addActionsEffect(P oldObject, EAdSceneElementDefImpl actor) {
		// add actions
		EAdActorActionsEffect showActions = new EAdActorActionsEffect(actor);
		actor.addBehavior(EAdMouseEventImpl.MOUSE_RIGHT_CLICK, showActions);
		
		// add other actions
		actionImporter.addAllActions(oldObject.getActions(), actor, false);
	}

}
