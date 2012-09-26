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

package ead.importer.subimporters.chapter;

import java.util.Map;

import com.google.inject.Inject;

import ead.common.model.elements.EAdAction;
import ead.common.model.elements.effects.ActorActionsEf;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scenes.EAdSceneElementDef;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.interfaces.ResourceImporter;
import ead.tools.StringHandler;
import es.eucm.eadventure.common.data.chapter.Action;
import es.eucm.eadventure.common.data.chapter.elements.Description;
import es.eucm.eadventure.common.data.chapter.elements.Element;

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

	protected ImportAnnotator annotator;

	@Inject
	public ActorImporter(StringHandler stringHandler,
			ResourceImporter resourceImporter,
			EAdElementFactory elementFactory,
			EAdElementImporter<Action, EAdAction> actionImporter,
			EAdElementFactory factory,
			ImportAnnotator annotator) {
		this.stringHandler = stringHandler;
		this.resourceImporter = resourceImporter;
		this.elementFactory = elementFactory;
		this.actionImporter = (ActionImporter) actionImporter;
		this.factory = factory;
		this.annotator = annotator;
	}

	@Override
	public EAdSceneElementDef init(P oldObject) {
		this.element = oldObject;
		return new SceneElementDef();
	}

	@Override
	public EAdSceneElementDef convert(P oldObject, Object object) {
		SceneElementDef actor = (SceneElementDef) object;
		actor.setId(oldObject.getId());

        annotator.annotate(actor, ImportAnnotator.Type.Open);

        annotator.annotate(actor, ImportAnnotator.Type.Entry, "type", "actor");

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

        annotator.annotate(actor, ImportAnnotator.Type.Close);

		return actor;
	}

	public abstract void initResourcesCorrespondencies();

	protected void addActionsEffect(P oldObject, SceneElementDef actor) {
		// add actions
		ActorActionsEf showActions = new ActorActionsEf(actor);
		actor.addBehavior(MouseGEv.MOUSE_RIGHT_CLICK, showActions);

		// add other actions
		actionImporter.addAllActions(oldObject.getActions(), actor, false);
	}

}
