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
