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

package ead.common.model.elements.predef;

import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.elements.EAdEvent;
import ead.common.model.elements.effects.ChangeSceneEf;
import ead.common.model.elements.events.SystemEv;
import ead.common.model.elements.events.enums.SystemEvType;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.scenes.EAdScene;
import ead.common.model.elements.scenes.SceneElementDef;

public class LoadingScreen extends BasicScene implements EAdScene {

	private static final String ID = "#engine.LoadingScreen";

	private ChangeSceneEf effect;

	public LoadingScreen() {
		this.setId(ID);

		getBackground().getDefinition().addAsset(SceneElementDef.appearance,
				new Image("@drawable/loading.png"));
		EAdEvent event = new SystemEv();
		effect = new ChangeSceneEf();
		event.addEffect(SystemEvType.GAME_LOADED, effect);
		this.getEvents().add(event);
	}

	public void setInitialScreen(EAdScene screen) {
		effect.setNextScene(screen);
	}

}
