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
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.Frame;
import es.eucm.eadventure.common.resources.assets.drawable.basics.impl.animation.FramesAnimation;

public class BasicActorFactoryTest {

	public static EAdActor getActor() {
		EAdBasicActor basicActor = new EAdBasicActor("Actor_id");

		FramesAnimation animation = new FramesAnimation();
		for (int i = 1; i <= 8; i++)
			animation.addFrame(new Frame("@drawable/paniel_wlr_0"
					+ i + ".png"));

		basicActor.getResources().addAsset(basicActor.getInitialBundle(),
				EAdBasicActor.appearance, animation);

		EAdString name = new EAdString("panielName");
		basicActor.setName(name);

		EAdString description = new EAdString("panielDescription");
		basicActor.setDescription(description);

		basicActor.getActions().add(BasicActionFactoryTest.getGrabAction());

		basicActor.getActions().add(BasicActionFactoryTest.getGrabAction());

		basicActor.getActions().add(BasicActionFactoryTest.getGrabAction());

		
		return basicActor;
	}
	
}
