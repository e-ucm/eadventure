package ead.guitools.enginegui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.zip.ZipFile;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import ead.engine.core.gdx.desktop.DesktopGame;
import ead.importer.AdventureImporter;
import es.eucm.ead.tools.java.utils.FileUtils;

public class EngineGUI {

	private static Properties properties;

	public static void main(String args[]) {
		loadProperties();
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".zip")
						|| f.getName().endsWith(".ead");
			}

			@Override
			public String getDescription() {
				return "eAdventure games";
			}

		});
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setSelectedFile(new File(getProperty("file", ".")));
		final JFrame frame = new JFrame("eAdventure Engine");
		final AdventureImporter importer = new AdventureImporter();
		JButton open = new JButton("Open");
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					setProperty("file", fileChooser.getSelectedFile()
							.getAbsolutePath());
					File f = fileChooser.getSelectedFile();
					boolean valid = false;
					boolean needImport = false;
					if (f.isDirectory()) {
						// File of 2.0 games
						File data = new File(f, "data.xml");
						// File of older games
						File oldData = new File(f, "chapter1.xml");
						if (data.exists()) {
							valid = true;
						} else if (oldData.exists()) {
							valid = true;
							needImport = true;
						}
					} else {
						ZipFile zip = null;
						try {
							zip = new ZipFile(f);
							InputStream is = FileUtils.readEntryFromZip(zip,
									"data.xml");
							if (is != null) {
								valid = true;
							}
						} catch (IOException e) {

						} finally {
							if (!valid) {
								try {
									InputStream is = FileUtils
											.readEntryFromZip(zip,
													"chapter1.xml");
									if (is != null) {
										valid = true;
										needImport = true;
									}
								} catch (IOException e) {

								}
							}

							if (zip != null) {
								try {
									zip.close();
								} catch (IOException e) {

								}
							}
						}
					}

					if (valid) {
						String folder = f.getAbsolutePath();
						if (needImport)
							folder = importer.importInTemp(fileChooser
									.getSelectedFile().getAbsolutePath());
						DesktopGame game = new DesktopGame(true);
						game.setModel(folder);
						game.start();
					} else {
						JOptionPane.showMessageDialog(frame,
								"Invalid eAdventure game.");
					}
				}
			}

		});
		frame.getContentPane().add(open);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static String getProperty(String key, String defaultValue) {
		if (properties == null) {
			loadProperties();
		}
		return properties.getProperty(key, defaultValue);
	}

	public static void setProperty(String key, String value) {
		if (properties == null) {
			loadProperties();
		}
		properties.setProperty(key, value);
		saveProperties();
	}

	private static void loadProperties() {
		properties = new Properties();
		File file = new File("enginegui.properties");
		FileInputStream is = null;
		if (file.exists()) {
			try {
				is = new FileInputStream(file);
				properties.load(is);
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	private static void saveProperties() {
		File file = new File("enginegui.properties");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {

			}
		}
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
			properties.store(os, "");

		} catch (Exception e) {

		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {

				}
			}
		}

	}

}
