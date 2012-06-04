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

package ead.engine.java.core.platform.modules;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;

import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.EAdCaption;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
import ead.common.resources.assets.drawable.compounds.ComposedDrawable;
import ead.common.resources.assets.drawable.compounds.EAdComposedDrawable;
import ead.common.resources.assets.drawable.compounds.EAdStateDrawable;
import ead.common.resources.assets.drawable.compounds.StateDrawable;
import ead.common.resources.assets.drawable.filters.EAdFilteredDrawable;
import ead.common.resources.assets.drawable.filters.FilteredDrawable;
import ead.common.util.StringHandler;
import ead.engine.core.platform.StringHandlerImpl;
import ead.engine.core.platform.assets.RuntimeAsset;
import ead.engine.core.platform.assets.drawables.basics.RuntimeCaption;
import ead.engine.core.platform.assets.drawables.basics.RuntimeFramesAnimation;
import ead.engine.core.platform.assets.drawables.compunds.RuntimeComposedDrawable;
import ead.engine.core.platform.assets.drawables.compunds.RuntimeFilteredDrawable;
import ead.engine.core.platform.assets.drawables.compunds.RuntimeStateDrawable;

public abstract class AssetHandlerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(StringHandler.class).to(StringHandlerImpl.class);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>> provideMap(){
		Map map = new HashMap( );
		map.put(EAdCaption.class, RuntimeCaption.class);
		map.put(Caption.class, RuntimeCaption.class);
		map.put(EAdComposedDrawable.class, RuntimeComposedDrawable.class);
		map.put(ComposedDrawable.class, RuntimeComposedDrawable.class);
		map.put(StateDrawable.class, RuntimeStateDrawable.class);
		map.put(EAdStateDrawable.class, RuntimeStateDrawable.class);
		map.put(FramesAnimation.class, RuntimeFramesAnimation.class);
		map.put(EAdFilteredDrawable.class, RuntimeFilteredDrawable.class);
		map.put(FilteredDrawable.class, RuntimeFilteredDrawable.class);
		return (Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<? extends AssetDescriptor>>>)map;
	}

}
