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

import java.io.File;
import java.util.List;
import java.util.Queue;

import com.google.inject.Guice;
import com.google.inject.Injector;

import es.eucm.eadventure.common.model.effects.EAdEffect;
import es.eucm.eadventure.common.model.elements.EAdActor;
import es.eucm.eadventure.common.model.elements.EAdActorReference;
import es.eucm.eadventure.common.model.elements.impl.EAdActorReferenceImpl;
import es.eucm.eadventure.common.model.elements.impl.EAdBasicActor;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.OperatorFactory;
import es.eucm.eadventure.engine.core.ValueMap;
import es.eucm.eadventure.engine.core.gameobjects.EffectGO;
import es.eucm.eadventure.engine.core.gameobjects.GameObjectFactory;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.impl.GameImpl;

public class TestEngine {

	public GameState gameState;
	public MouseState mouseState;
	public GameImpl gameImpl;
	public GameObjectFactory gameObjectFactory;
	public ValueMap valueMap;
	public OperatorFactory operatorFactory;
	public EAdActor actor1, actor2;
	public EAdActorReference reference1, reference2;
	public EffectOpaqueBlockTestScreen screen;
	public PlatformControl platformControl;
	
	public TestEngine( ){
		Injector injector = Guice.createInjector(new TestModule(), new TestBasicGameModule());
		
		gameState = injector.getInstance(GameState.class);
		mouseState = injector.getInstance(MouseState.class);
		gameImpl = injector.getInstance(GameImpl.class);
		gameObjectFactory = injector.getInstance(GameObjectFactory.class);
		valueMap = injector.getInstance(ValueMap.class);
		operatorFactory = injector.getInstance(OperatorFactory.class);
		screen = injector.getInstance(EffectOpaqueBlockTestScreen.class);
		platformControl = injector.getInstance(PlatformControl.class);
		
		initActors();
		initReferences();
		

		platformControl.start();
	}
	
	private void initActors( ){
		actor1 = new EAdBasicActor( "actor1" );
		actor2 = new EAdBasicActor( "actor2" );
	}

	private void initReferences() {
		reference1 = new EAdActorReferenceImpl(  "reference1", actor1 );
		reference2 = new EAdActorReferenceImpl(  "reference1", actor2 );
		screen.getSceneElements().add(reference1);
		screen.getSceneElements().add(reference2);
	}

	public void update() {
		gameImpl.update();
		
	}

	public Queue<MouseAction> getMouseEvents() {
		return mouseState.getMouseEvents();
	}

	public void render(int i) {
		gameImpl.render(i);
		
	}

	public List<EffectGO<?>> getEffects() {
		return gameState.getEffects();
	}

	public void addEffect( EAdEffect effect ) {
		gameState.addEffect(effect);
		
	}
}
