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

package es.eucm.ead.engine.factories;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import es.eucm.ead.engine.factories.mapproviders.SceneElementsMapProvider;
import es.eucm.ead.engine.gameobjects.sceneelements.SceneElementGO;
import es.eucm.ead.model.elements.scenes.EAdSceneElement;
import es.eucm.ead.tools.GenericInjector;
import es.eucm.ead.tools.reflection.ReflectionProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Default implementation of the {@link GameObjectFactor}.
 * </p>
 */
@Singleton
public class SceneElementGOFactoryImpl extends
		GOFactoryImpl<EAdSceneElement, SceneElementGO> implements
		SceneElementGOFactory {

	private List<SceneElementGO> auxList = new ArrayList<SceneElementGO>();

	@Inject
	public SceneElementGOFactoryImpl(ReflectionProvider reflectionProvider,
			GenericInjector injector) {
		super(true, reflectionProvider, injector);
		SceneElementsMapProvider provider = new SceneElementsMapProvider();
		setClassMap(provider.getMap());
	}

	@Override
	public SceneElementGO get(String id) {
		for (EAdSceneElement key : cache.keySet()) {
			if (key.getId() != null && key.getId().equals(id)) {
				return cache.get(key);
			}
		}
		return null;
	}

	public void clean() {
		auxList.clear();
		if (cache != null) {
			auxList.addAll(cache.values());
			for (SceneElementGO go : auxList) {
				go.free();
			}
		}
		super.clean();
	}

}
