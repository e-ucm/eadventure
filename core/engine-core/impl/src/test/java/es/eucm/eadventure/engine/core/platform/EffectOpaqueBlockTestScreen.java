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

package es.eucm.eadventure.engine.core.platform;

import com.google.inject.Inject;

import es.eucm.eadventure.common.model.effects.impl.timedevents.EAdWaitEffect;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicSceneElement;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.params.geom.impl.EAdPositionImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.ImageImpl;
import es.eucm.eadventure.engine.core.game.GameLoop;

public class EffectOpaqueBlockTestScreen extends EAdSceneImpl implements
		EAdScene {

	private EAdSceneElementDefImpl buttonActor;
	private StringHandler stringHandler;
	private EAdBasicSceneElement buttonReference;
	private EAdBasicSceneElement buttonReference2;
	private EAdBasicSceneElement buttonActor2;

	@Inject
	public EffectOpaqueBlockTestScreen(StringHandler stringHandler) {
		super();
		this.setId("LoadingScreen");

		this.stringHandler = stringHandler;

		initButtonActor();
		initButtonActor2();

		getComponents().add(buttonReference);
		getComponents().add(buttonReference2);
	}

	private void initButtonActor() {
		buttonActor = new EAdSceneElementDefImpl();
		buttonActor.setId("StartGame");
		buttonActor.getResources().addAsset(buttonActor.getInitialBundle(),
				EAdSceneElementDefImpl.appearance,
				new ImageImpl("@drawable/start.png"));
		stringHandler.setString(buttonActor.getName(), "Start game");

		// EAdBehavior b = new StandardBehavior(buttonActor, "b");

		// buttonActor.setBehavior(b);

		buttonReference = new EAdBasicSceneElement(buttonActor);
		buttonReference.setPosition(new EAdPositionImpl(
				EAdPositionImpl.Corner.BOTTOM_CENTER, 200, 200));
	}

	private void initButtonActor2() {
		buttonActor2 = new EAdBasicSceneElement();
		buttonActor2.setId("StartGame");
		buttonActor2
				.getDefinition()
				.getResources()
				.addAsset(buttonActor2.getDefinition().getInitialBundle(),
						EAdSceneElementDefImpl.appearance,
						new ImageImpl("@drawable/start.png"));

		EAdWaitEffect waitEffect = new EAdWaitEffect(
				GameLoop.SKIP_MILLIS_TICK + 1);
		buttonActor2
				.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, waitEffect);

		EAdWaitEffect waitEffect2 = new EAdWaitEffect(
				GameLoop.SKIP_MILLIS_TICK + 1);
		waitEffect2.setOpaque(false);
		buttonActor2.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
				waitEffect2);

		EAdWaitEffect waitEffect3 = new EAdWaitEffect(
				GameLoop.SKIP_MILLIS_TICK + 1);
		waitEffect3.setOpaque(false);
		waitEffect3.setBlocking(false);
		buttonActor2.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK,
				waitEffect3);

		buttonActor2.setPosition(new EAdPositionImpl(
				EAdPositionImpl.Corner.BOTTOM_CENTER, 10, 10));
	}

}
