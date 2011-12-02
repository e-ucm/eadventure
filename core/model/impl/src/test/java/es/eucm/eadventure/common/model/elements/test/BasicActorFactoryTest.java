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

package es.eucm.eadventure.common.model.elements.test;

import es.eucm.eadventure.common.model.actions.test.BasicActionFactoryTest;
import es.eucm.eadventure.common.model.elements.EAdSceneElementDef;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneElementDefImpl;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.FramesAnimation;

public class BasicActorFactoryTest {

	public static EAdSceneElementDef getActor(StringHandler sh) {
		EAdSceneElementDefImpl basicActor = new EAdSceneElementDefImpl();
		basicActor.setId("Actor_id");

		FramesAnimation animation = new FramesAnimation();
		for (int i = 1; i <= 8; i++)
			animation.addFrame(new Frame("@drawable/paniel_wlr_0"
					+ i + ".png"));

		basicActor.getResources().addAsset(basicActor.getInitialBundle(),
				EAdSceneElementDefImpl.appearance, animation);
		
		sh.setString(basicActor.getName(), "Paniel");

		basicActor.getActions().add(BasicActionFactoryTest.getGrabAction(sh));

		basicActor.getActions().add(BasicActionFactoryTest.getGrabAction(sh));

		basicActor.getActions().add(BasicActionFactoryTest.getGrabAction(sh));

		
		return basicActor;
	}
	
}
