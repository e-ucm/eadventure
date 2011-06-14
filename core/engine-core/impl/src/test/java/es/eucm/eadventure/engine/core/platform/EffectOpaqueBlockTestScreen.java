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

import es.eucm.eadventure.common.model.effects.impl.EAdWaitEffect;
import es.eucm.eadventure.common.model.elements.EAdScene;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;
import es.eucm.eadventure.common.model.guievents.impl.EAdMouseEventImpl;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.resources.EAdString;
import es.eucm.eadventure.common.resources.StringHandler;
import es.eucm.eadventure.common.resources.assets.drawable.impl.ImageImpl;
import es.eucm.eadventure.engine.core.GameLoop;

public class EffectOpaqueBlockTestScreen extends EAdSceneImpl implements EAdScene {

	private EAdBasicActor buttonActor;
	private StringHandler stringHandler;
	private EAdActorReferenceImpl buttonReference;
	private EAdActorReferenceImpl buttonReference2;
	private EAdBasicActor buttonActor2;

	@Inject
	public EffectOpaqueBlockTestScreen(StringHandler stringHandler) {
			super("LoadingScreen");
			
			this.stringHandler = stringHandler;
			
			initButtonActor();
			initButtonActor2();
			
			getSceneElements().add(buttonReference);
			getSceneElements().add(buttonReference2);
	}
	
	private void initButtonActor() {
		buttonActor = new EAdBasicActor("StartGame");
		buttonActor.getResources().addAsset(buttonActor.getInitialBundle(),
				EAdBasicActor.appearance, new ImageImpl("@drawable/start.png"));
		EAdString name = new EAdString("stringName");
		buttonActor.setName(name);
		stringHandler.addString(name, "Start game");
		
//		EAdBehavior b = new StandardBehavior(buttonActor, "b");
		
//		buttonActor.setBehavior(b);
		
		buttonReference = new EAdActorReferenceImpl( "id4", buttonActor);
		buttonReference.setPosition(new EAdPosition(
				EAdPosition.Corner.BOTTOM_CENTER, 200, 200));
	}
	
	private void initButtonActor2() {
		buttonActor2 = new EAdBasicActor("StartGame");
		buttonActor2.getResources().addAsset(buttonActor2.getInitialBundle(),
				EAdBasicActor.appearance, new ImageImpl("@drawable/start.png"));
		EAdString name = new EAdString("stringName");
		buttonActor2.setName(name);
		stringHandler.addString(name, "Start game");
		
		EAdWaitEffect waitEffect = new EAdWaitEffect( "wait", GameLoop.SKIP_MILLIS_TICK + 1);
		buttonActor2.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, waitEffect );
		
		EAdWaitEffect waitEffect2 = new EAdWaitEffect( "wait", GameLoop.SKIP_MILLIS_TICK  + 1);
		waitEffect2.setOpaque( false );
		buttonActor2.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, waitEffect2 );
		
		EAdWaitEffect waitEffect3 = new EAdWaitEffect(  "wait", GameLoop.SKIP_MILLIS_TICK + 1 );
		waitEffect3.setOpaque( false );
		waitEffect3.setBlocking( false );
		buttonActor2.addBehavior(EAdMouseEventImpl.MOUSE_LEFT_CLICK, waitEffect3 );
		
		buttonReference2 = new EAdActorReferenceImpl( "id4", buttonActor2);
		buttonReference2.setPosition(new EAdPosition(
				EAdPosition.Corner.BOTTOM_CENTER, 10, 10));
	}

}
