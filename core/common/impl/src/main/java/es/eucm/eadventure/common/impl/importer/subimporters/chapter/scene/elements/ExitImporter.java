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

package es.eucm.eadventure.common.impl.importer.subimporters.chapter.scene.elements;

import com.google.inject.Inject;

import es.eucm.eadventure.common.EAdElementImporter;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.ExitLook;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.Effect;
import es.eucm.eadventure.common.impl.importer.interfaces.EAdElementFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.EffectsImporterFactory;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.model.conditions.impl.NOTCondition;
import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeScene;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.EAdSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.transitions.EAdTransition;
import es.eucm.eadventure.common.params.EAdString;
import es.eucm.eadventure.common.predef.model.effects.EAdChangeCursorEffect;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.Image;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;

public class ExitImporter extends ElementImporter<Exit> {

	private static int ID_GENERATOR = 0;
	private EffectsImporterFactory effectsImporterFactory;
	private ResourceImporter resourceImporter;

	@Inject
	public ExitImporter(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			EAdElementFactory factory,
			EffectsImporterFactory effectsImporterFactory,
			StringHandler stringHandler, ResourceImporter resourceImporter) {
		super(factory, conditionsImporter, stringHandler);
		this.effectsImporterFactory = effectsImporterFactory;
		this.resourceImporter = resourceImporter;
	}

	public EAdSceneElement init(Exit oldObject) {
		EAdBasicSceneElement newExit = new EAdBasicSceneElement();
		newExit.setId("exit" + ID_GENERATOR++);
		return newExit;
	}

	@Override
	public EAdSceneElement convert(Exit oldObject, Object object) {
		EAdBasicSceneElement newExit = (EAdBasicSceneElement) object;

		// Shape
		setShape(newExit, oldObject);

		// Enable condition
		EAdCondition enableCondition = getEnableCondition(oldObject
				.getConditions());

		super.addEnableEvent(newExit, enableCondition);

		// Effects
		addEfects(newExit, oldObject, enableCondition);

		// Add influence area
		addInfluenceArea(newExit, oldObject, oldObject.getInfluenceArea());

		// Add get to the exit
		addGoToExit(newExit, oldObject, enableCondition);

		// Add appearance (name and cursor)
		addAppearance(newExit, oldObject);

		return newExit;
	}

	private void addGoToExit(EAdBasicSceneElement newExit, Exit oldObject,
			EAdCondition enableCondition) {

		// Change scene effect
		EAdScene scene = (EAdScene) factory.getElementById(oldObject
				.getNextSceneId());
		EAdChangeScene changeScene = new EAdChangeScene(scene,
				EAdTransition.BASIC);
		changeScene.setId("change_screen_" + newExit.getId());
		changeScene.setCondition(enableCondition);

		// Post effects
		for (Effect e : oldObject.getPostEffects().getEffects()) {
			EAdEffect eadEffect = effectsImporterFactory.getEffect(e);
			changeScene.getFinalEffects().add(eadEffect);
		}

		addGoToExit(newExit, oldObject, enableCondition, changeScene);

	}

	private void addAppearance(EAdBasicSceneElement newExit, Exit oldObject) {
		// Add name
		ExitLook exitLook = oldObject.getDefaultExitLook();

		EAdString name = EAdString.newEAdString("exitLookName");
		stringHandler.setString(name, exitLook.getExitText());
		newExit.setVarInitialValue(EAdBasicSceneElement.VAR_NAME, name);

		// Change cursor
		Image cursor = null;
		if (exitLook.getCursorPath() == null)
			// Default
			cursor = new ImageImpl("@drawable/exit.png");
		else
			cursor = (Image) resourceImporter.getAssetDescritptor(
					exitLook.getCursorPath(), ImageImpl.class);
		EAdChangeCursorEffect changeCursor = new EAdChangeCursorEffect(cursor);
		EAdChangeCursorEffect changeCursorBack = new EAdChangeCursorEffect(
				factory.getDefaultCursor());

		newExit.addBehavior(EAdMouseEventImpl.MOUSE_ENTERED, changeCursor);
		newExit.addBehavior(EAdMouseEventImpl.MOUSE_EXITED, changeCursorBack);
	}

	private void addEfects(EAdBasicSceneElement newExit, Exit oldObject,
			EAdCondition enableCondition) {
		// Normal effects
		for (Effect e : oldObject.getEffects().getEffects()) {
			EAdEffect eadEffect = effectsImporterFactory.getEffect(e);
			eadEffect.setCondition(enableCondition);
			newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, eadEffect);
		}

		// No effects
		for (Effect e : oldObject.getNotEffects().getEffects()) {
			EAdEffect eadEffect = effectsImporterFactory.getEffect(e);
			eadEffect.setCondition(new NOTCondition(enableCondition));
			newExit.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, eadEffect);
		}
	}

}
