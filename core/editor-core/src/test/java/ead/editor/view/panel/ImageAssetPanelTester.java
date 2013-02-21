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

package ead.editor.view.panel;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.model.assets.drawable.basics.Image;
import ead.editor.model.nodes.DependencyNode;
import ead.editor.model.nodes.EngineNode;
import ead.editor.model.nodes.asset.ImageAssetNode;
import ead.editor.view.panel.AbstractElementPanel;
import ead.editor.view.panel.asset.ImageAssetPanel;
import ead.engine.core.platform.assets.GdxAssetHandler;
import ead.utils.FileUtils;
import ead.utils.Log4jConfig;

public class ImageAssetPanelTester extends AbstractPanelTester {

	private static final Logger logger = LoggerFactory
			.getLogger("ImageAssetPanelTester");

	private static File tmpDir;

	private static ImageAssetNode target;

	private File getDir() {
		if (tmpDir == null) {
			try {
				tmpDir = FileUtils.createTempDir("ead", "asset-test");
				File src = new File(
						"../../demos/firstaidgame/src/main/resources/drawable");
				FileUtils.copy(src, new File(tmpDir, "drawable"));
			} catch (IOException ioe) {
				logger.error("Could not create or initialize tmpDir {}",
						tmpDir, ioe);
			}
		}
		return tmpDir;
	}

	public static void main(String[] args) {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Debug,
				new Object[] {});
		AbstractPanelTester apt = new ImageAssetPanelTester();
		apt.init();
		apt.setVisible(true);
	}

	@Override
	AbstractElementPanel createPanel() {
		return new ImageAssetPanel() {

			@Override
			public void cleanup() {
				super.cleanup();
				try {
					FileUtils.deleteRecursive(tmpDir);
				} catch (IOException ex) {
					System.err.println("Exception cleaning up");
				}
			}

		};
	}

	@Override
	DependencyNode getTarget() {
		if (target == null) {
			target = new ImageAssetNode(2);
			target.addChild(new EngineNode<Image>(1, new Image(
					"@drawable/assets_animation_telefono.png")));
			target.setBase(new File(getDir(),
					GdxAssetHandler.PROJECT_INTERNAL_PATH));
		}
		return target;
	}
}
