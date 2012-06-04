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

package ead.engine.test.core.platform;

import com.google.inject.Inject;

import ead.common.model.elements.effects.timedevents.WaitEf;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.scene.EAdScene;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.SceneElement;
import ead.common.model.elements.scenes.SceneElementDef;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.util.EAdPosition;
import ead.common.util.StringHandler;

public class EffectOpaqueBlockTestScreen extends BasicScene implements
		EAdScene {

	private SceneElementDef buttonActor;
	private StringHandler stringHandler;
	private SceneElement buttonReference;
	private SceneElement buttonReference2;
	private SceneElement buttonActor2;

	@Inject
	public EffectOpaqueBlockTestScreen(StringHandler stringHandler) {
		super();
		this.setId("LoadingScreen");

		this.stringHandler = stringHandler;

		initButtonActor();
		initButtonActor2();

		getSceneElements().add(buttonReference);
		getSceneElements().add(buttonReference2);
	}

	private void initButtonActor() {
		buttonActor = new SceneElementDef();
		buttonActor.setId("StartGame");
		buttonActor.getResources().addAsset(buttonActor.getInitialBundle(),
				SceneElementDef.appearance,
				new Image("@drawable/start.png"));
		stringHandler.setString(buttonActor.getName(), "Start game");

		// EAdBehavior b = new StandardBehavior(buttonActor, "b");

		// buttonActor.setBehavior(b);

		buttonReference = new SceneElement(buttonActor);
		buttonReference.setPosition(new EAdPosition(
				EAdPosition.Corner.BOTTOM_CENTER, 200, 200));
	}

	private void initButtonActor2() {
		buttonActor2 = new SceneElement();
		buttonActor2.setId("StartGame");
		buttonActor2
				.getDefinition()
				.getResources()
				.addAsset(buttonActor2.getDefinition().getInitialBundle(),
						SceneElementDef.appearance,
						new Image("@drawable/start.png"));

		WaitEf waitEffect = new WaitEf(
				60 + 1);
		buttonActor2
				.addBehavior(MouseGEv.MOUSE_LEFT_CLICK, waitEffect);

		WaitEf waitEffect2 = new WaitEf(
				60 + 1);
		waitEffect2.setOpaque(false);
		buttonActor2.addBehavior(MouseGEv.MOUSE_LEFT_CLICK,
				waitEffect2);

		WaitEf waitEffect3 = new WaitEf(
				60 + 1);
		waitEffect3.setOpaque(false);
		waitEffect3.setBlocking(false);
		buttonActor2.addBehavior(MouseGEv.MOUSE_LEFT_CLICK,
				waitEffect3);

		buttonActor2.setPosition(new EAdPosition(
				EAdPosition.Corner.BOTTOM_CENTER, 10, 10));
	}

}
