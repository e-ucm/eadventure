package ead.plugins.engine.screenshot;

import com.google.inject.Inject;
import ead.engine.core.game.interfaces.GameState;
import ead.engine.core.gameobjects.effects.AbstractEffectGO;
import ead.tools.StringHandler;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author anserran
 *         Date: 20/05/13
 *         Time: 11:29
 */
public class ScreenShotGO extends AbstractEffectGO<ScreenShotEf> {

	private static JFileChooser fileChooser;

	private StringHandler stringHandler;

	@Inject
	public ScreenShotGO(GameState gameState, StringHandler stringHandler) {
		super(gameState);
		this.stringHandler = stringHandler;
	}

	public void initialize() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser
					.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		}

		fileChooser.setDialogTitle(stringHandler.getString(effect
				.getDialogTitle()));

		File file = new File(fileChooser.getCurrentDirectory(), stringHandler
				.getString(effect.getFileName()));
		fileChooser.setSelectedFile(file);
		if (fileChooser.showSaveDialog((Component) null) == JFileChooser.APPROVE_OPTION) {
			try {
				File f = fileChooser.getSelectedFile();
				if (!f.getCanonicalPath().endsWith(".png")) {
					f = new File(f.getParent(), f.getName() + ".png");
				}
				ScreenshotSaver.saveScreenshot(f);
			} catch (IOException e) {
			}
		}
	}

}
