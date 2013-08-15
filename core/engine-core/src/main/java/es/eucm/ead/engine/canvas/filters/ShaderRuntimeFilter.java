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

package es.eucm.ead.engine.canvas.filters;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.google.inject.Inject;

import es.eucm.ead.model.assets.drawable.filters.ShaderFilter;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.assets.drawables.RuntimeDrawable;
import es.eucm.ead.engine.canvas.GdxCanvas;

public class ShaderRuntimeFilter implements RuntimeFilter<ShaderFilter> {

	private AssetHandler assetHandler;

	private Map<ShaderFilter, ShaderProgram> shaders;

	private ShaderProgram defaultShader;

	@Inject
	public ShaderRuntimeFilter(AssetHandler assetHandler) {
		shaders = new HashMap<ShaderFilter, ShaderProgram>();
		this.assetHandler = assetHandler;
	}

	@Override
	public void setFilter(RuntimeDrawable<?> drawable, ShaderFilter filter,
			GdxCanvas c) {
		ShaderProgram s = shaders.get(filter);
		if (s == null) {
			s = new ShaderProgram(assetHandler.getTextFile(filter
					.getVertexShader()), assetHandler.getTextFile(filter
					.getFragmentShader()));
			shaders.put(filter, s);
		}

		c.setShader(s);

	}

	@Override
	public void unsetFilter(RuntimeDrawable<?> drawable, ShaderFilter filter,
			GdxCanvas c) {

		if (defaultShader == null) {
			defaultShader = SpriteBatch.createDefaultShader();
		}

		c.setShader(defaultShader);

	}

}
