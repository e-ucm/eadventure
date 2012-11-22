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

package ead.editor.view.menu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import ead.editor.R;
import ead.editor.control.Controller;
import ead.editor.control.EditorConfig;
import ead.editor.control.EditorConfig.EditorConf;
import ead.editor.control.EditorConfigImpl;
import ead.utils.FileUtils;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Default file menu implementation
 */
public class FileMenu extends AbstractEditorMenu {

	private static final Logger logger = LoggerFactory.getLogger("FileMenu");

	@Inject
	public FileMenu(Controller controller) {
		super(controller, Messages.file_menu);
	}

	private static FileFilter ead2LoadFolderFilter = new EAdFileFilter(".eap",
			"data[.]xml", "EAdventure 2 project folders", false);
	private static FileFilter ead1LoadFileFilter = new EAdFileFilter(".ead",
			"descriptor[.]xml", "EAdventure 1 project files", false);
	private static EAdFileView eadFileView = new EAdFileView();

	/**
	 * Initialize the file menu
	 */
	@Override
	public void initialize() {
		AbstractEditorAction[] as = new AbstractEditorAction[] {

				new OpenAction(Messages.file_menu_open, KeyEvent.VK_O, 0),
				new ImportAction(Messages.file_menu_import, KeyEvent.VK_I,
						KeyEvent.ALT_DOWN_MASK),
				new NewAction(Messages.file_menu_new, KeyEvent.VK_N, 0),

				new SaveAction(Messages.file_menu_save, KeyEvent.VK_S, 0),
				new SaveAsAction(Messages.file_menu_save_as, KeyEvent.VK_S,
						KeyEvent.ALT_DOWN_MASK),

				new ExitAction(Messages.file_menu_exit, KeyEvent.VK_Q,
						KeyEvent.ALT_DOWN_MASK), };

		for (AbstractEditorAction a : as) {
			registerAction(a);
			controller.getProjectController().addChangeListener(a);
			a.processChange(null);
		}
	}

	/**
	 * File menu actions have access to the containing class, and are subscribed
	 * to change events from the ProjectController
	 */
	public abstract class FileMenuAction extends AbstractEditorAction {

		public FileMenuAction(String name, int gkey, int gmask) {
			super(name, gkey, gmask);
		}

		/**
		 * Ask for confirmation before losing current edits (if any).
		 * @param message to show in confirmation dialogue
		 * @return
		 */
		public boolean allowChanceToSave(String message) {
			if (controller.getModel().getEngineModel() != null
					&& controller.getCommandManager().isChanged()) {
				int rc = JOptionPane.showConfirmDialog(null, message,
						Messages.file_menu_confirm_destructive_op,
						JOptionPane.WARNING_MESSAGE,
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (rc == JOptionPane.CANCEL_OPTION) {
					return false;
				} else if (rc == JOptionPane.OK_OPTION) {
					new MenuProgressListener(controller, new Runnable() {
						@Override
						public void run() {
							controller.getProjectController().save();
						}
					}).runInEDT();
					return true;
				}
			}
			return true;
		}
	}

	public class OpenAction extends FileMenuAction {

		public OpenAction(String name, int gkey, int gmask) {
			super(name, gkey, gmask);
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!allowChanceToSave(Messages.file_menu_open_confirm_destructive)) {
				return;
			}

			final File f = chooseFile(null, Messages.file_menu_open_title,
					Messages.file_menu_open_message,
					Messages.file_menu_open_title_error,
					Messages.file_menu_open_message_error, true,
					JFileChooser.FILES_AND_DIRECTORIES, ead2LoadFolderFilter,
					EditorConf.LastLoadDirectory, EditorConf.LastLoadFile,
					controller.getConfig());
			if (f != null) {
				new MenuProgressListener(controller, new Runnable() {

					@Override
					public void run() {
						EditorConfig ec = controller.getConfig();
						ec.put(EditorConf.LastLoadDirectory, f.getParentFile()
								.getAbsolutePath());
						ec.put(EditorConf.LastLoadFile, f.getAbsolutePath());
						ec.save(null);
						controller.getProjectController().load(
								f.getAbsolutePath());
						controller.getViewController().setTitleQualifier(
								f.getName());
					}
				}).runInEDT();
			}
		}
	}

	public class ImportAction extends FileMenuAction {

		public ImportAction(String name, int gkey, int gmask) {
			super(name, gkey, gmask);
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!allowChanceToSave(Messages.file_menu_import_confirm_destructive)) {
				return;
			}

			final File f = chooseFile(null, Messages.file_menu_import_title,
					Messages.file_menu_import_message,
					Messages.file_menu_import_title_error,
					Messages.file_menu_import_message_error, true,
					JFileChooser.FILES_AND_DIRECTORIES, ead1LoadFileFilter,
					EditorConf.LastImportDirectory, EditorConf.LastImportFile,
					controller.getConfig());
			if (f == null) {
				// user cancelled request
				return;
			}
			final File d = chooseFile(null,
					Messages.file_menu_import_save_title,
					Messages.file_menu_import_save_message,
					Messages.file_menu_import_title_error,
					Messages.file_menu_import_message_error, false,
					JFileChooser.DIRECTORIES_ONLY, null,
					EditorConf.LastSaveDirectory, EditorConf.LastSaveFile,
					controller.getConfig());
			if (d != null) {
				new MenuProgressListener(controller, new Runnable() {

					@Override
					public void run() {
						EditorConfig ec = controller.getConfig();
						ec.put(EditorConf.LastImportDirectory, f
								.getParentFile().getAbsolutePath());
						ec.put(EditorConf.LastImportFile, f.getAbsolutePath());
						ec.put(EditorConf.LastSaveDirectory, d.getParentFile()
								.getAbsolutePath());
						ec.put(EditorConf.LastSaveFile, d.getAbsolutePath());
						ec.save(null);

						controller.getProjectController().doImport(
								f.getAbsolutePath(), d.getAbsolutePath());
						controller.getViewController().setTitleQualifier(
								d.getName());
					}
				}).runInEDT();
			}
		}
	}

	public class NewAction extends FileMenuAction {

		public NewAction(String name, int gkey, int gmask) {
			super(name, gkey, gmask);
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (!allowChanceToSave(Messages.file_menu_new_confirm_destructive)) {
				return;
			}

			final File d = chooseFile(null, Messages.file_menu_new_title,
					Messages.file_menu_new_message,
					Messages.file_menu_new_title_error,
					Messages.file_menu_new_message_error, false,
					JFileChooser.DIRECTORIES_ONLY, null,
					EditorConf.LastSaveDirectory, null, controller.getConfig());
			if (d != null) {
				new MenuProgressListener(controller, new Runnable() {

					@Override
					public void run() {
						EditorConfig ec = controller.getConfig();
						ec.put(EditorConf.LastSaveDirectory, d.getParentFile()
								.getAbsolutePath());
						ec.put(EditorConf.LastSaveFile, d.getAbsolutePath());
						ec.save(null);
						controller.getProjectController().newProject();
						controller.getProjectController().saveAs(
								d.getAbsolutePath());
						controller.getViewController().setTitleQualifier(
								d.getName());
					}
				}).runInEDT();
			}
		}
	}

	public class SaveAction extends FileMenuAction {

		public SaveAction(String name, int gkey, int gmask) {
			super(name, gkey, gmask);
			// not enabled until something is loaded
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			new MenuProgressListener(controller, new Runnable() {

				@Override
				public void run() {
					controller.getProjectController().save();
				}
			}).runInEDT();
		}

		@Override
		public void processChange(Object event) {
			setEnabled(controller.getModel().getEngineModel() != null);
		}
	}

	public class SaveAsAction extends FileMenuAction {

		public SaveAsAction(String name, int gkey, int gmask) {
			super(name, gkey, gmask);
			// not enabled until something is loaded
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent ae) {

			final File d = chooseFile(null, Messages.file_menu_new_title,
					Messages.file_menu_new_message,
					Messages.file_menu_new_title_error,
					Messages.file_menu_new_message_error, false,
					JFileChooser.DIRECTORIES_ONLY, null,
					EditorConf.LastSaveDirectory, null, controller.getConfig());
			if (d != null) {
				new MenuProgressListener(controller, new Runnable() {

					@Override
					public void run() {
						controller.getConfig().put(
								EditorConf.LastSaveDirectory,
								d.getParentFile().getAbsolutePath());
						controller.getConfig().put(EditorConf.LastSaveFile,
								d.getAbsolutePath());
						controller.getProjectController().saveAs(
								d.getAbsolutePath());
						controller.getViewController().setTitleQualifier(
								d.getName());
					}
				}).runInEDT();
			}
		}

		@Override
		public void processChange(Object event) {
			setEnabled(controller.getModel().getEngineModel() != null);
		}
	}

	public class ExitAction extends FileMenuAction {

		public ExitAction(String name, int gkey, int gmask) {
			super(name, gkey, gmask);
		}

		@Override
		public void actionPerformed(ActionEvent ae) {

			if (allowChanceToSave(Messages.file_menu_exit_confirm_destructive)) {
				logger.info("Exiting aplication at user request");
				System.exit(0);
			}
		}
	}

	private static class FileSelection {
		private File selectedFile = null;
	}

	/**
	 * Ask the user to provide a file or directory
	 * @param p parent component (use of null is recommended)
	 * @param selectString something like "Select"
	 * @param descriptionString something like "project file to load"
	 * @param errorString something like "Invalid"
	 * @param toOpen if true, the file must exist
	 * @param fileType type of file; generally, JFileChooser.FILES_ONLY
	 */
	public static File chooseFile(final Component p, String title,
			String description, final String errorTitle,
			final String errorDescription, final boolean toOpen,
			final int fileType, FileFilter ff, EditorConf initialDirKey,
			EditorConf initialFileKey, final EditorConfig ec) {

		final JFileChooser jfc = new JFileChooser();
		if (ff != null) {
			jfc.setFileFilter(ff);
		}
		jfc.setFileView(eadFileView);

		File currentDirectory = new File(".");
		if (initialDirKey != null && ec.containsKey(initialDirKey)) {
			currentDirectory = new File(ec.getValue(initialDirKey));
		} else if (ec.containsKey(EditorConf.LastDirectory)) {
			currentDirectory = new File(ec.getValue(EditorConf.LastDirectory));
		}
		if (currentDirectory.isDirectory()) {
			jfc.setCurrentDirectory(currentDirectory);
		}

		File currentFile = null;
		if (initialFileKey != null && ec.containsKey(initialFileKey)) {
			currentFile = new File(ec.getValue(initialFileKey));
		}
		if (currentFile != null && currentFile.exists()) {
			// FIXME            jfc.setSelectedFile(currentFile);
		}

		// description label
		JLabel titleLabel = new JLabel(description);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// more dialog config
		jfc.setFileSelectionMode(fileType);
		if (toOpen) {
			jfc.setDialogType(JFileChooser.OPEN_DIALOG);
		} else {
			jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		}

		// result goes here
		final FileSelection selection = new FileSelection();

		// build final dialog
		final JDialog jd = new JDialog((JFrame) p, title, true);
		jd.add(titleLabel, BorderLayout.NORTH);
		jd.add(jfc, BorderLayout.CENTER);

		jfc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
					selection.selectedFile = null;
					jd.dispose();
				} else if (e.getActionCommand().equals(
						JFileChooser.APPROVE_SELECTION)) {
					File f = jfc.getSelectedFile();
					if (f != null && f.getParentFile().isDirectory()) {
						File nextDirectory = f.getParentFile();
						ec.put(EditorConf.LastDirectory, nextDirectory
								.getAbsolutePath());
						ec.save(null);
					}
					if (f == null
							|| (!f.exists() && toOpen)
							|| (fileType == JFileChooser.FILES_ONLY && f
									.isDirectory())) {
						JOptionPane.showMessageDialog(p, errorDescription,
								errorTitle, JOptionPane.ERROR_MESSAGE);
						f = null;
					}
					selection.selectedFile = f;
					jd.dispose();
				}
			}
		});
		jd.pack();
		jd.setLocationRelativeTo(p);
		jd.setVisible(true);
		return selection.selectedFile;
	}

	private static class EAdFileFilter extends FileFilter {

		private String regex;
		private String name;
		private String extension;
		private boolean strict;

		public EAdFileFilter(String extension, String regex, String name,
				boolean strict) {
			this.extension = extension;
			this.regex = regex;
			this.name = name;
			this.strict = strict;
		}

		@Override
		public boolean accept(File file) {
			if (file.isDirectory() && !strict) {
				return true;
			} else if (file.isDirectory() && strict) {
				if (regex == null) {
					return true;
				} else {
					try {
						return FileUtils.folderContainsEntry(file, regex);
					} catch (IOException ioe) {
						// silently ignore
						logger.warn("Could not check {} for {} entries",
								new Object[] { file.getPath(), regex });
					}
				}
			} else if (file.getAbsolutePath().endsWith(extension)) {
				if (regex == null) {
					return true;
				} else {
					try {
						return FileUtils.zipContainsEntry(file, regex);
					} catch (IOException ioe) {
						// silently ignore
						logger.warn("Could not check {} for {} entries",
								new Object[] { file.getPath(), regex });
					}
				}
			}
			return false;
		}

		@Override
		public String getDescription() {
			return name;
		}
	}

	private static class EAdFileView extends FileView {

		private static FileFilter ead2Strict = new EAdFileFilter(".eap",
				"data[.]xml", "EAdventure 2 project folders", true);
		private static FileFilter ead1Strict = new EAdFileFilter(".ead",
				"descriptor[.]xml", "EAdventure 1 project files", true);
		private static Icon ead2Icon = new ImageIcon(EAdFileView.class
				.getClassLoader().getResource(R.Drawable.EditorIcon16x16_png));
		private static Icon ead1xIcon = new ImageIcon(EAdFileView.class
				.getClassLoader()
				.getResource(R.Drawable.EditorIcon16x16_bw_png));

		@Override
		public String getDescription(File f) {
			return super.getDescription(f);
		}

		@Override
		public Icon getIcon(File f) {
			if (ead1Strict.accept(f)) {
				return ead1xIcon;
			} else if (ead2Strict.accept(f)) {
				return ead2Icon;
			} else {
				return super.getIcon(f);
			}
		}

		@Override
		public String getName(File f) {
			return super.getName(f);
		}

		@Override
		public String getTypeDescription(File f) {
			if (ead1Strict.accept(f)) {
				return ead1Strict.getDescription();
			} else if (ead2Strict.accept(f)) {
				return ead2Strict.getDescription();
			} else {
				return super.getTypeDescription(f);
			}
		}

		@Override
		public Boolean isTraversable(File f) {
			return super.isTraversable(f);
			//            if (f.isFile()) {
			//                return false;
			//            }
			//            return (! ead1Strict.accept(f)) && (! ead2Strict.accept(f));
		}
	}

	public static void main(String args[]) {

		EditorConfig ec = new EditorConfigImpl();

		JFrame jf = new JFrame();
		jf.pack();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);

		FileMenu fm = new FileMenu(null);
		File f1 = chooseFile(jf, "Title", "Description", "ErrorTitle",
				"ErrorDescription", true, JFileChooser.FILES_ONLY,
				ead1LoadFileFilter, EditorConf.LastLoadFile,
				EditorConf.LastLoadFile, ec);
		File f2 = chooseFile(jf, "Title", "Description", "ErrorTitle",
				"ErrorDescription", false, JFileChooser.FILES_ONLY,
				ead1LoadFileFilter, EditorConf.LastLoadFile,
				EditorConf.LastLoadFile, ec);
		System.err.println(f1 + " " + f2);
		System.exit(0);
	}
}
