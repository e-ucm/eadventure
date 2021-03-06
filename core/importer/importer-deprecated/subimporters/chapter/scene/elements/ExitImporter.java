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

package ead.importer.subimporters.chapter.scene.elements;

import com.google.inject.Inject;
import ead.importer.EAdElementImporter;
import ead.importer.annotation.ImportAnnotator;
import ead.importer.interfaces.EAdElementFactory;
import ead.importer.interfaces.EffectsImporterFactory;
import ead.importer.interfaces.ResourceImporter;
import ead.importer.subimporters.effects.TriggerSceneImporter;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.multimedia.EAdSound;
import es.eucm.ead.model.assets.multimedia.Sound;
import es.eucm.ead.model.elements.conditions.NOTCond;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.PlaySoundEf;
import es.eucm.ead.model.elements.effects.TriggerMacroEf;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.huds.MouseHud;
import es.eucm.ead.model.elements.predef.effects.ChangeCursorEf;
import es.eucm.ead.model.elements.scenes.GhostElement;
import es.eucm.ead.model.elements.scenes.Scene;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.ead.model.elements.scenes.SceneElementDef;
import es.eucm.ead.model.elements.transitions.Transition;
import es.eucm.ead.model.params.fills.ColorFill;
import es.eucm.ead.model.params.fills.Paint;
import es.eucm.ead.model.params.guievents.MouseGEv;
import es.eucm.ead.model.params.text.EAdString;
import es.eucm.ead.tools.StringHandler;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.ExitLook;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;

public class ExitImporter extends ElementImporter<Exit> {

	private EffectsImporterFactory effectsImporterFactory;
	private ResourceImporter resourceImporter;
	private static Paint EXIT_PAINT = new Paint(new ColorFill(109, 20, 0, 100),
			ColorFill.RED);

	@Inject
	public ExitImporter(
			EAdElementImporter<Conditions, Condition> conditionsImporter,
			EAdElementFactory factory,
			EffectsImporterFactory effectsImporterFactory,
			StringHandler stringHandler, ResourceImporter resourceImporter,
			ImportAnnotator annotator) {
		super(factory, conditionsImporter, stringHandler, annotator);
		this.effectsImporterFactory = effectsImporterFactory;
		this.resourceImporter = resourceImporter;
	}

	public SceneElement init(Exit oldObject) {
		GhostElement newExit = new GhostElement();
		return newExit;
	}

	@Override
	public SceneElement convert(Exit oldObject, Object object) {
		GhostElement newExit = (GhostElement) object;
		oldObject.getDefaultExitLook();

		// Shape
		setShape(newExit, oldObject, EXIT_PAINT);

		// Enable condition
		Condition enableCondition = getEnableCondition(oldObject
				.getConditions());

		// If the exit has not-effects, it is always visible
		if (!oldObject.isHasNotEffects()) {
			super.addVisibleEvent(newExit, enableCondition);
		}

		// Add influence area
		addInfluenceArea(newExit, oldObject, oldObject.getInfluenceArea());

		// Change scene effect
		ChangeSceneEf changeScene = createChangeScene(newExit, oldObject,
				enableCondition);

		// Effects
		Effect finalEffect = addEffects(newExit, oldObject, enableCondition,
				changeScene);

		// Add get to the exit
		addGoToExit(newExit, oldObject, enableCondition, finalEffect);

		// Add appearance (name and cursor)
		addAppearance(newExit, oldObject);

		return newExit;
	}

	private ChangeSceneEf createChangeScene(SceneElement newExit,
			Exit oldObject, Condition enableCondition) {
		// Change scene effect
		Scene scene = (Scene) factory.getElementById(oldObject
				.getNextSceneId());

		Transition transition = TriggerSceneImporter.getTransition(oldObject
				.getTransitionType(), oldObject.getTransitionTime());

		ChangeSceneEf changeScene = new ChangeSceneEf(scene, transition);
		changeScene.setCondition(enableCondition);

		Effect previousEffect = changeScene;
		// Post effects
		for (es.eucm.eadventure.common.data.chapter.effects.Effect e : oldObject
				.getPostEffects().getEffects()) {
			Effect eadEffect = effectsImporterFactory.getEffect(e);
			if (eadEffect != null) {
				previousEffect.getNextEffects().add(eadEffect);
				previousEffect = eadEffect;
			}
		}

		return changeScene;
	}

	private void addAppearance(SceneElement newExit, Exit oldObject) {
		// Add name
		ExitLook exitLook = oldObject.getDefaultExitLook();

		EAdString name = stringHandler.generateNewString();
		stringHandler.setString(name, exitLook.getExitText());
		newExit.getDefinition().setVarInitialValue(
				SceneElementDef.VAR_DOC_NAME, name);

		// Change cursor
		String cursor = null;
		if (exitLook.getCursorPath() == null)
			// Default
			cursor = factory.getDefaultCursor(AdventureData.EXIT_CURSOR);
		else
			cursor = factory
					.createCursor((Image) resourceImporter.getAssetDescritptor(
							exitLook.getCursorPath(), Image.class));

		ChangeCursorEf changeCursor = new ChangeCursorEf(cursor);
		ChangeCursorEf changeCursorBack = new ChangeCursorEf(
				MouseHud.DEFAULT_CURSOR);

		newExit.addBehavior(MouseGEv.MOUSE_ENTERED, changeCursor);
		newExit.addBehavior(MouseGEv.MOUSE_EXITED, changeCursorBack);

		// Sound
		if (exitLook.getSoundPath() != null) {
			EAdSound s = (EAdSound) resourceImporter.getAssetDescritptor(
					exitLook.getSoundPath(), Sound.class);

			PlaySoundEf playSound = null; //new PlaySoundEf(s, false);
			newExit.addBehavior(MouseGEv.MOUSE_ENTERED, playSound);

		}
	}

	private Effect addEffects(SceneElement newExit, Exit oldObject,
			Condition enableCondition, Effect changeSceneEffect) {
		TriggerMacroEf triggerMacro = new TriggerMacroEf();

		// Normal effects
		EAdList<Effect> normalMacro = effectsImporterFactory
				.getMacroEffects(oldObject.getEffects());
		if (normalMacro != null) {
			triggerMacro.putEffects(enableCondition, normalMacro);
		}

		// No effects
		if (oldObject.isHasNotEffects()) {
			EAdList<Effect> noEAdList = effectsImporterFactory
					.getMacroEffects(oldObject.getNotEffects());
			if (noEAdList != null) {
				triggerMacro
						.putEffects(new NOTCond(enableCondition), noEAdList);
			}
		}

		// To maintain the execution order, the change scene effect must be
		// added after effects/not-effects
		triggerMacro.getNextEffects().add(changeSceneEffect);
		return triggerMacro;
	}

}
