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

package es.eucm.ead.legacyplugins.engine.desktop.screenshot;

import com.google.inject.Inject;
import es.eucm.ead.engine.game.interfaces.EngineHook;
import es.eucm.ead.engine.game.interfaces.GUI;
import es.eucm.ead.engine.game.Game;
import es.eucm.ead.engine.game.GameState;
import es.eucm.ead.engine.gameobjects.effects.AbstractEffectGO;
import es.eucm.ead.legacyplugins.model.ScreenShotEf;
import es.eucm.ead.tools.StringHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ScreenShotGO extends AbstractEffectGO<ScreenShotEf> implements
		EngineHook {

	static private Logger logger = LoggerFactory.getLogger(ScreenShotGO.class);

	private static JFileChooser fileChooser;

	private StringHandler stringHandler;

	private File file;

	private boolean finished;

	@Inject
	public ScreenShotGO(Game game, StringHandler stringHandler) {
		super(game);
		this.stringHandler = stringHandler;
	}

	public void initialize() {
		super.initialize();
		file = null;
		finished = false;
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser
					.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		}

		fileChooser.setDialogTitle(stringHandler.getString(effect
				.getDialogTitle()));

		File f = new File(fileChooser.getCurrentDirectory(), stringHandler
				.getString(effect.getFileName()));
		fileChooser.setSelectedFile(f);

		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean goOn = false;

				while (!goOn) {
					if (fileChooser.showSaveDialog((Component) null) == JFileChooser.APPROVE_OPTION) {
						try {
							File f = fileChooser.getSelectedFile();
							if (!f.getCanonicalPath().endsWith(".png")) {
								f = new File(f.getParent(), f.getName()
										+ ".png");
							}

							if (f.exists()) {
								goOn = (JOptionPane
										.showConfirmDialog(
												null,
												"El archivo ya existe. ¿Desea sobreescribir?",
												"¿Sobreescribir?",
												JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
							} else {
								goOn = true;
							}

							synchronized (ScreenShotGO.this) {
								file = f;
								finished = true;
							}

						} catch (IOException e) {
						}
					}
				}
			}

		}).start();
	}

	public boolean isFinished() {
		synchronized (this) {
			return finished;
		}
	}

	public void finish() {
		game.addHook(Game.HOOK_AFTER_RENDER, this);
		super.finish();
	}

	public boolean isQueueable() {
		return true;
	}

	@Override
	public int compareTo(EngineHook arg0) {
		return 0;
	}

	@Override
	public void execute(Game game, GameState gameState, GUI gui) {
		game.removeHook(Game.HOOK_AFTER_RENDER, this);
		try {
			ScreenshotSaver.saveScreenshot(file);
		} catch (IOException e) {
			logger.error("{}", e);
		}
	}

}
