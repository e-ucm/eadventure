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

package ead.importer.subimporters.chapter.cutscene;

import es.eucm.ead.model.elements.EAdEffect;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.QuitGameEf;
import es.eucm.ead.model.elements.effects.TriggerMacroEf;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.scenes.EAdScene;
import es.eucm.ead.model.elements.transitions.DisplaceTransition;
import es.eucm.ead.model.elements.transitions.EAdTransition;
import es.eucm.ead.model.elements.transitions.EmptyTransition;
import es.eucm.ead.model.elements.transitions.FadeInTransition;
import es.eucm.ead.model.elements.transitions.enums.DisplaceTransitionType;
import es.eucm.ead.model.params.text.EAdString;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.interfaces.EffectsImporterFactory;
import ead.importer.interfaces.ResourceImporter;
import ead.importer.subimporters.effects.TriggerSceneImporter;
import ead.tools.StringHandler;
import es.eucm.eadventure.common.data.animation.Transition;
import es.eucm.eadventure.common.data.chapter.scenes.Cutscene;
import es.eucm.eadventure.common.data.chapter.scenes.Slidescene;

public abstract class CutsceneImporter<T extends Cutscene> implements
		EAdElementImporter<T, EAdScene> {

	protected StringHandler stringHandler;

	protected EAdElementFactory factory;

	protected EffectsImporterFactory effectsImporter;

	protected ResourceImporter resourceImporter;

	protected ImportAnnotator annotator;

	public CutsceneImporter(StringHandler stringHandler,
			EAdElementFactory factory, EffectsImporterFactory effectsImporter,
			ResourceImporter resourceImporter, ImportAnnotator annotator) {
		this.stringHandler = stringHandler;
		this.factory = factory;
		this.effectsImporter = effectsImporter;
		this.resourceImporter = resourceImporter;
		this.annotator = annotator;
	}

	@Override
	public EAdScene convert(T oldObject, Object newElement) {
		EAdScene scene = (EAdScene) newElement;
		factory.getCurrentChapterModel().getScenes().add(scene);
		scene.setReturnable(false);
		// Appearance
		importResources(oldObject, scene);
		// Configuration
		importConfiguration(scene, getEndEffect(oldObject));
		// Documentation
		importDocumentation(scene, oldObject);

		return scene;
	}

	private void importDocumentation(EAdScene scene, Cutscene oldScene) {
		EAdString doc = stringHandler.generateNewString();
		stringHandler.setString(doc, oldScene.getDocumentation());
		scene.getDefinition().setDoc(doc);

		EAdString name = stringHandler.generateNewString();
		stringHandler.setString(name, oldScene.getName());
		scene.getDefinition().setName(name);
	}

	protected EAdEffect getEndEffect(Cutscene cutscene) {
		EAdScene nextScene = null;
		EAdTransition transition = EmptyTransition.instance();
		switch (cutscene.getNext()) {
		case Slidescene.GOBACK:
			nextScene = null;
			break;
		case Slidescene.ENDCHAPTER:
			if (factory.getOldDataModel().getChapters().size() == 1) {
				return new QuitGameEf();
			} else {
				// FIXME end chapter if there's more than one chapter
			}
			break;
		case Slidescene.NEWSCENE:
			nextScene = (EAdScene) factory.getElementById(cutscene
					.getTargetId());
			transition = TriggerSceneImporter.getTransition(cutscene
					.getTransitionType(), cutscene.getTransitionTime());
			break;
		}
		ChangeSceneEf changeScene = new ChangeSceneEf();
		changeScene.setNextScene(nextScene);
		changeScene.setTransition(transition);

		EAdList<EAdEffect> macro = effectsImporter.getMacroEffects(cutscene
				.getEffects());
		if (macro != null) {
			TriggerMacroEf triggerMacro = new TriggerMacroEf();
			triggerMacro.putEffects(EmptyCond.TRUE, macro);
			changeScene.getNextEffects().add(triggerMacro);
		}
		return changeScene;
	}

	protected abstract void importConfiguration(EAdScene scene,
			EAdEffect endEffect);

	protected abstract void importResources(T oldCutscene, EAdScene scene);

	/**
	 * Builds a transition from the type and the time
	 * 
	 * @param type
	 *            transition type
	 * @param time
	 *            transition time
	 * @return
	 */
	public static EAdTransition getTransition(int type, int time) {
		switch (type) {
		case Transition.TYPE_FADEIN:
			return new FadeInTransition(time);
		case Transition.TYPE_HORIZONTAL:
			return new DisplaceTransition(time,
					DisplaceTransitionType.HORIZONTAL, true);
		case Transition.TYPE_VERTICAL:
			return new DisplaceTransition(time,
					DisplaceTransitionType.VERTICAL, true);
		default:
			return EmptyTransition.instance();
		}
	}

}
