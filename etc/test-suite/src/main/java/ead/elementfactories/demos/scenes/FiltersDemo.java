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

package ead.elementfactories.demos.scenes;

import ead.common.model.elements.scenes.SceneElementImpl;
import ead.common.resources.assets.drawable.basics.ImageImpl;
import ead.common.resources.assets.drawable.filters.FilteredDrawable;
import ead.common.resources.assets.drawable.filters.FilteredDrawableImpl;
import ead.common.resources.assets.drawable.filters.MatrixFilter;
import ead.common.util.EAdMatrixImpl;
import ead.common.util.EAdPosition.Corner;

public class FiltersDemo extends EmptyScene {

	public FiltersDemo() {
		EAdMatrixImpl m = new EAdMatrixImpl();
		m.scale(-1.0f, 1.0f, true);
		ImageImpl i = new ImageImpl("@drawable/ng_key.png");
		FilteredDrawable d = new FilteredDrawableImpl(i, new MatrixFilter(m, 1.0f, 0.0f));
		SceneElementImpl e = new SceneElementImpl(d);
		e.setScale(0.8f);
		e.setPosition(Corner.CENTER, 400, 300);
		
		SceneElementImpl e2 = new SceneElementImpl(i);
		e2.setPosition(Corner.CENTER, 400, 400);
		e2.setScale(0.8f);
		getComponents().add(e2);
		getComponents().add(e);
	}

	@Override
	public String getSceneDescription() {
		return "An scene showing filters.";
	}

	public String getDemoName() {
		return "Filters Scene";
	}

}
