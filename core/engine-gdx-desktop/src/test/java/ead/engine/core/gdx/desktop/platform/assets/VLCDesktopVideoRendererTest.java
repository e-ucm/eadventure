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

package ead.engine.core.gdx.desktop.platform.assets;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ead.common.resources.assets.multimedia.EAdVideo;
import ead.common.resources.assets.multimedia.Video;
import ead.common.util.EAdURI;
import ead.engine.core.gdx.assets.GdxAssetHandler;
import ead.engine.core.gdx.desktop.utils.assetviewer.AssetViewerModule;
import ead.engine.core.platform.assets.AssetHandler;
import ead.utils.Log4jConfig;
import java.awt.Component;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests video rendering
 * @author mfreire
 */
public class VLCDesktopVideoRendererTest {

	private EAdVideo video = new Video(
			"@binary/assets_video_CambiarPosicion.AVIXVID.webm");

	private AssetHandler ah;

	public VLCDesktopVideoRendererTest() {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Info,
				new Object[] {});

		Injector i = Guice.createInjector(new AssetViewerModule());
		ah = i.getInstance(AssetHandler.class);
		ah.setCacheEnabled(false);
		ah.setResourcesLocation(new EAdURI(new File(
				"../../demos/firstaidgame/src/main/resources/").getPath()));
	}

	/**
	 * Test of getVLCComponent method, of class VLCDesktopVideoRenderer.
	 */
	@Test
	public void testGetVLCComponent() {
		System.out.println("getVLCComponent");
		EAdVideo asset = null;
		//		VLCDesktopVideoRenderer instance = new VLCDesktopVideoRenderer(
		//				(GdxDesktopAssetHandler) ah);
		//		Component result = instance.getVLCComponent(asset);
	}

	public static void main(String[] args) {
		VLCDesktopVideoRendererTest t = new VLCDesktopVideoRendererTest();
		t.testGetVLCComponent();
	}
}
