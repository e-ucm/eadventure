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
import ead.engine.java.core.platform.JavaInjector;
import ead.tools.GenericInjector;
import ead.tools.StringHandler;
import ead.tools.StringHandlerImpl;

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
