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

package es.eucm.eadventure.engine.core.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.elements.EAdEvent;
import es.eucm.eadventure.common.model.elements.effects.ChangeSceneEf;
import es.eucm.eadventure.common.model.elements.events.SystemEv;
import es.eucm.eadventure.common.model.elements.events.enums.SystemEventType;
import es.eucm.eadventure.common.model.elements.scene.EAdScene;
import es.eucm.eadventure.common.model.elements.scenes.SceneElementDefImpl;
import es.eucm.eadventure.common.model.elements.scenes.SceneImpl;
import es.eucm.eadventure.common.resources.assets.drawable.basics.ImageImpl;

public class LoadingScreen extends SceneImpl implements EAdScene {

	private Logger logger = Logger.getLogger("LoadingScreen");

	@Param("effect")
	private ChangeSceneEf effect;

	@Inject
	public LoadingScreen() {
		super();
		setId("LoadingScreen");
		logger.log(Level.INFO, "New instance");

		getBackground()
				.getDefinition()
				.getResources()
				.addAsset(getBackground().getDefinition().getInitialBundle(),
						SceneElementDefImpl.appearance,
						new ImageImpl("@drawable/loading.png"));
		EAdEvent event = new SystemEv();
		event.setId("startEvent");
		effect = new ChangeSceneEf();
		effect.setId("LoadingChangeScreen");
		event.addEffect(SystemEventType.GAME_LOADED, effect);
		this.getEvents().add(event);
	}

	public void setInitialScreen(EAdScene screen) {
		effect.setNextScene(screen);
	}

}
