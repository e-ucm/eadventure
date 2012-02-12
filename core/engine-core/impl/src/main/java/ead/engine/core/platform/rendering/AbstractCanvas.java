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

package ead.engine.core.platform.rendering;

import com.google.inject.Inject;

import ead.common.params.paint.EAdPaint;
import ead.common.resources.assets.drawable.filters.DrawableFilter;
import ead.engine.core.platform.DrawableAsset;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.rendering.GenericCanvas;
import ead.engine.core.platform.rendering.filters.FilterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCanvas<T> implements GenericCanvas<T> {

	protected static final Logger logger = LoggerFactory.getLogger("EAdCanvas");

	protected T g;

	protected EAdPaint paint;

	protected FontHandler fontHandler;

	protected FilterFactory<T> filterFactory;

	@Inject
	public AbstractCanvas( FontHandler fontHandler, FilterFactory<T> filterFactory ){
		this.fontHandler = fontHandler;
		this.filterFactory = filterFactory;
	}

	public void setGraphicContext( T g ){
		this.g = g;
	}

	public void drawText( String text ){
		drawText( text, 0, 0 );
	}

	public void setPaint( EAdPaint paint ){
		this.paint = paint;
	}

	@Override
	public T getNativeGraphicContext() {
		return g;
	}

	public void setFilter(DrawableAsset<?, T> drawable, DrawableFilter filter){
		filterFactory.applyFilter(drawable, filter, this);
	}

}
