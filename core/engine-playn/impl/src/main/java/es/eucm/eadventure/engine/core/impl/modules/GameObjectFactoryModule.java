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

package es.eucm.eadventure.engine.core.impl.modules;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.TypeLiteral;

import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.model.EAdElement;

import es.eucm.eadventure.engine.core.gameobjects.*;
import es.eucm.eadventure.engine.core.gameobjects.impl.*;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.ActorGOImpl;
import es.eucm.eadventure.engine.core.gameobjects.impl.sceneelements.ActorReferenceGOImpl;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.EffectGameObjectFactoryConfigurator;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.ElementGameObjectFactoryConfigurator;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.EventGameObjectFactoryConfigurator;
import es.eucm.eadventure.engine.core.impl.factorymapproviders.GameObjectFactoryMapProvider;

public class GameObjectFactoryModule extends AbstractGinModule {
	
	@Override
	protected void configure() {
		bind(SceneGO.class).to(SceneGOImpl.class);
		bind(ActorReferenceGO.class).to(ActorReferenceGOImpl.class);
		bind(ActorGO.class).to(ActorGOImpl.class);
		bind(TimerGO.class).to(TimerGOImpl.class);
		
		bind(ElementGameObjectFactoryConfigurator.class);
		bind(EffectGameObjectFactoryConfigurator.class);
		bind(EventGameObjectFactoryConfigurator.class);
		
		bind(new TypeLiteral<MapProvider<Class<? extends EAdElement>, Class<? extends GameObject<?>>>>() {}).to(GameObjectFactoryMapProvider.class);
		bind(GameObjectFactory.class).to(PlayNGameObjectFactoryImpl.class);
	}


}
