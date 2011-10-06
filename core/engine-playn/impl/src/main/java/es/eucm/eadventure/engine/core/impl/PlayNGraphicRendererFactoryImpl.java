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
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import es.eucm.eadventure.common.interfaces.AbstractFactory;
import es.eucm.eadventure.common.interfaces.EAdRuntimeException;
import es.eucm.eadventure.common.interfaces.MapProvider;
import es.eucm.eadventure.common.interfaces.ReflectionProvider;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.platform.AssetRenderer;
import es.eucm.eadventure.engine.core.platform.GameObjectRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRenderer;
import es.eucm.eadventure.engine.core.platform.GraphicRendererFactory;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;
import es.eucm.eadventure.engine.core.util.EAdTransformation;

@Singleton
public abstract class PlayNGraphicRendererFactoryImpl<S> extends AbstractFactory<GraphicRenderer<?, ?>> implements GraphicRendererFactory<S> {

	private static final Logger logger = Logger.getLogger("GraphicRendererFactoryImpl");

	@Inject
	public PlayNGraphicRendererFactoryImpl(@Named("GraphicRenderer") MapProvider<Class<?>, GraphicRenderer<?, ?>> mapProvider,
			ReflectionProvider interfacesProvider) {
		super(mapProvider, interfacesProvider);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RuntimeAsset<?>> void render(S graphicContext, T asset) {
		if (graphicContext != null && asset != null) {
			AssetRenderer<S, T> assetRenderer = (AssetRenderer<S, T>) get(asset.getClass());
			assetRenderer.render(graphicContext, asset);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RuntimeAsset<?>> boolean contains(int x, int y, T asset) {
		AssetRenderer<S, T> assetRenderer = (AssetRenderer<S, T>) get(asset.getClass());
		return assetRenderer.contains(x, y, asset);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends GameObject<?>> void render(S graphicContext, T object, EAdTransformation t) {
		try {
			GameObjectRenderer<S, T> gameObjectRenderer = (GameObjectRenderer<S, T>) get(object.getClass());
			gameObjectRenderer.render(graphicContext, object, t);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new EAdRuntimeException(e.getMessage());
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T extends GameObject<?>> boolean contains(T gameObject, int virtualX,
			int virtualY) {
		GameObjectRenderer<S, T> gameObjectRenderer = (GameObjectRenderer<S, T>) get(gameObject.getClass());
		return gameObjectRenderer.contains(gameObject, virtualX, virtualY);
	}

}
