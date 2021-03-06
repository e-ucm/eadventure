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

package es.eucm.ead.engine.desktop.platform;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import es.eucm.ead.engine.BasicModuleMap;
import es.eucm.ead.engine.assets.AssetHandler;
import es.eucm.ead.engine.assets.SpecialAssetRenderer;
import es.eucm.ead.engine.desktop.platform.assets.DesktopAssetHandler;
import es.eucm.ead.engine.desktop.platform.assets.video.vlc.VLC2VideoRenderer;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.model.assets.multimedia.EAdVideo;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DesktopModule extends AbstractModule {

	private Map<TypeLiteral<?>, Class<?>> binds;

	public DesktopModule() {
		this(new HashMap<TypeLiteral<?>, Class<?>>());
	}

	public DesktopModule(Map<TypeLiteral<?>, Class<?>> binds) {
		this.binds = binds;
	}

	@SuppressWarnings( { "unchecked", "rawtypes" })
	@Override
	protected void configure() {
		BasicModuleMap map = new BasicModuleMap();
		map.setBind(AssetHandler.class, DesktopAssetHandler.class);
		map.setBind(GUI.class, DesktopGUI.class);

		for (Entry<Class<?>, Class<?>> e : map.getBinds().entrySet()) {
			TypeLiteral typeLiteral = TypeLiteral.get(e.getKey());
			if (!binds.containsKey(typeLiteral)) {
				binds.put(typeLiteral, e.getValue());
			}
		}

		TypeLiteral<SpecialAssetRenderer<EAdVideo, ?>> t = new TypeLiteral<SpecialAssetRenderer<EAdVideo, ?>>() {
		};
		if (!binds.containsKey(t)) {
			binds.put(t, VLC2VideoRenderer.class);
			// bind(new TypeLiteral<SpecialAssetRenderer<EAdVideo, ?>>() {
			// }).to(JavaVideoRenderer.class);
		}
		for (Entry<TypeLiteral<?>, Class<?>> entry : binds.entrySet()) {
			TypeLiteral type = entry.getKey();
			Class c = entry.getValue();
			bind(type).to(c).in(Singleton.class);
		}

	}

}
