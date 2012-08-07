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

package ead.engine.desktop.utils.assetviewer;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import ead.common.model.elements.extra.EAdList;
import ead.common.model.elements.variables.EAdOperation;
import ead.engine.core.assets.GdxAssetHandler;
import ead.engine.core.game.Game;
import ead.engine.core.game.VariableMap;
import ead.engine.core.gameobjects.go.DrawableGO;
import ead.engine.core.platform.FontHandler;
import ead.engine.core.platform.FontHandlerImpl;
import ead.engine.core.platform.GUI;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.util.EAdTransformation;
import ead.tools.GenericInjector;
import ead.tools.StringHandler;
import ead.tools.StringHandlerImpl;
import ead.tools.java.JavaInjector;

public class AssetViewerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(FontHandler.class).to(FontHandlerImpl.class).in(Singleton.class);
		bind(AssetHandler.class).to(GdxAssetHandler.class).in(
				Singleton.class);
		bind(StringHandler.class).to(StringHandlerImpl.class).in(
				Singleton.class);
		bind(GUI.class).to(AssetViewerGUI.class).in(Singleton.class);
		bind(VariableMap.class).to(AssetVariableMap.class).in(Singleton.class);
		bind(GenericInjector.class).to(JavaInjector.class).in(Singleton.class);

	}

	public static class AssetViewerGUI implements GUI {

		@Override
		public void setGame(Game game) {

		}

		@Override
		public void addElement(DrawableGO<?> go,
				EAdTransformation parentTransformation) {

		}

		@Override
		public void showSpecialResource(Object object, int x, int y,
				boolean fullscreen) {

		}

		@Override
		public void prepareGUI() {

		}

		@Override
		public void commit(float interpolation) {

		}

		@Override
		public void initialize() {

		}

		@Override
		public int getWidth() {
			return 0;
		}

		@Override
		public int getHeight() {
			return 0;
		}

		@Override
		public EAdTransformation addTransformation(EAdTransformation t1,
				EAdTransformation t2) {
			return null;
		}

		@Override
		public void finish() {

		}

		@Override
		public void setInitialTransformation(
				EAdTransformation initialTransformation) {

		}

		@Override
		public int getSkippedMilliseconds() {
			return Math.round(1000.0f / 60.0f);
		}

		@Override
		public int getTicksPerSecond() {
			return 0;
		}

	}

	public static class AssetVariableMap extends VariableMap {

		public AssetVariableMap() {
			super(null, null, null);
		}

		@Override
		public String processTextVars(String text,
				EAdList<EAdOperation> operations) {
			return text;
		}

		@Override
		public String evaluateExpression(String expression,
				EAdList<EAdOperation> operations) {
			return expression;
		}

	}

}
