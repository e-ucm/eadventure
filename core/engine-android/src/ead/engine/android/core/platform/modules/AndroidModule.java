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

package ead.engine.android.core.platform.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import ead.engine.android.core.gameobjects.AndroidActionsHUDImpl;
import ead.engine.android.core.gameobjects.AndroidBasicHUD;
import ead.engine.android.core.platform.AndroidGUI;
import ead.engine.core.gameobjects.huds.ActionsHUD;
import ead.engine.core.gameobjects.huds.BottomBasicHUD;
import ead.engine.core.gameobjects.huds.BottomBasicHUDImpl;
import ead.engine.core.gameobjects.huds.InventoryHUD;
import ead.engine.core.gameobjects.huds.InventoryHUDImpl;
import ead.engine.core.gameobjects.huds.MenuHUD;
import ead.engine.core.gameobjects.huds.MenuHUDImpl;
import ead.engine.core.gameobjects.huds.TopBasicHUD;
import ead.engine.core.platform.GUI;

public class AndroidModule extends AbstractModule {

	public AndroidModule() {
		
	}
	
	@Override
	protected void configure() {
		bind(GUI.class).to(AndroidGUI.class);
		bind(ActionsHUD.class).to(AndroidActionsHUDImpl.class);
		bind(TopBasicHUD.class).to(AndroidBasicHUD.class);
		bind(BottomBasicHUD.class).to(BottomBasicHUDImpl.class);
		bind(InventoryHUD.class).to(InventoryHUDImpl.class);
		bind(MenuHUD.class).to(MenuHUDImpl.class);
//		bind(new TypeLiteral<FilterFactory<Canvas>>(){}).to(AndroidFilterFactory.class);
	}
	
	@Provides
	@Named("threaded")
	public boolean provideThreaded() {
		return Boolean.TRUE;
	}

}
