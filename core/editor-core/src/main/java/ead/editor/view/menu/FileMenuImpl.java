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

import com.google.inject.Inject;
import ead.editor.control.CommandManager;
import ead.editor.control.Controller;
import ead.editor.control.EditorConfig;
import ead.editor.control.EditorConfig.EditorConf;

import ead.gui.EAdMenuItem;
import ead.editor.R;
import ead.editor.model.EditorModel.ModelProgressListener;
import ead.utils.FileUtils;
import ead.utils.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default file menu implementation
 */
public class FileMenuImpl extends MenuImpl implements FileMenu {

    private static final Logger logger = LoggerFactory.getLogger("FileMenu");
    private EAdMenuItem mOpen;
    private EAdMenuItem mImport;
    private EAdMenuItem mNew;
    private EAdMenuItem mSave;
    private EAdMenuItem mSaveAs;
    private EAdMenuItem mExit;
    private Controller controller;

    @Inject
    public FileMenuImpl(Controller controller) {
        super(Messages.file_menu);
        this.controller = controller;
        initialize();
    }
    private static FileFilter ead2LoadFolderFilter = new EAdFileFilter(
            ".eap", "data[.]xml", "EAdventure 2 project folders", false);
    private static FileFilter ead1LoadFileFilter = new EAdFileFilter(
            ".ead", "descriptor[.]xml", "EAdventure 1 project files", false);
    private static EAdFileView eadFileView = new EAdFileView();

    /**
     * Initialize the file menu
     */
    public void initialize() {
        mOpen = new EAdMenuItem(Messages.file_menu_open);
        addMenuItem(mOpen);
        mOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                final File f = chooseFile(null, Messages.file_menu_open_title,
                        Messages.file_menu_open_message,
                        Messages.file_menu_open_title_error,
                        Messages.file_menu_open_message_error,
                        true, JFileChooser.FILES_AND_DIRECTORIES, ead2LoadFolderFilter);
                if (f != null) {
                    new ProgressListener(controller, new Runnable() {

                        @Override
                        public void run() {
                            controller.getProjectController().load(f.getAbsolutePath());
                        }
                    }).runInEDT();
                }
            }
        });

        mImport = new EAdMenuItem(Messages.file_menu_import);
        addMenuItem(mImport);
        mImport.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                final File f = chooseFile(null, Messages.file_menu_import_title,
                        Messages.file_menu_import_message,
                        Messages.file_menu_import_title_error,
                        Messages.file_menu_import_message_error,
                        true, JFileChooser.FILES_AND_DIRECTORIES, ead1LoadFileFilter);
                if (f == null) {
                    // user cancelled request
                    return;
                }
                final File d = chooseFile(null, Messages.file_menu_import_save_title,
                        Messages.file_menu_import_save_message,
                        Messages.file_menu_import_title_error,
                        Messages.file_menu_import_message_error,
                        false, JFileChooser.DIRECTORIES_ONLY, null);
                if (f != null && d != null) {
                    new ProgressListener(controller, new Runnable() {

                        @Override
                        public void run() {
                            controller.getProjectController().doImport(
                                    f.getAbsolutePath(), d.getAbsolutePath());
                        }
                    }).runInEDT();
                }
            }
        });

        mNew = new EAdMenuItem(Messages.file_menu_new);
        addMenuItem(mNew);
        mNew.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                final File d = chooseFile(null, Messages.file_menu_new_title,
                        Messages.file_menu_new_message,
                        Messages.file_menu_new_title_error,
                        Messages.file_menu_new_message_error,
                        false, JFileChooser.DIRECTORIES_ONLY, null);
                if (d != null) {
                    new ProgressListener(controller, new Runnable() {

                        @Override
                        public void run() {
                            controller.getProjectController().newProject();
                            controller.getProjectController().saveAs(d.getAbsolutePath());
                        }
                    }).runInEDT();
                }
            }
        });

        mSave = new EAdMenuItem(Messages.file_menu_save);
        addMenuItem(mSave);
        mSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                controller.getProjectController().save();
            }
        });

        mSaveAs = new EAdMenuItem(Messages.file_menu_save_as);
        addMenuItem(mSaveAs);
        mSaveAs.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                final File d = chooseFile(null, Messages.file_menu_new_title,
                        Messages.file_menu_new_message,
                        Messages.file_menu_new_title_error,
                        Messages.file_menu_new_message_error,
                        false, JFileChooser.DIRECTORIES_ONLY, null);
                if (d != null) {
                    new ProgressListener(controller, new Runnable() {

                        @Override
                        public void run() {
                            controller.getProjectController().saveAs(d.getAbsolutePath());
                        }
                    }).runInEDT();
                }
            }
        });

        mExit = new EAdMenuItem(Messages.file_menu_exit);
        addMenuItem(mExit);
        mExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (controller.getCommandManager().isChanged()) {
                    int rc = JOptionPane.showConfirmDialog(null,
                            Messages.file_menu_confirm_exit_before_close_message,
                            Messages.file_menu_confirm_exit_before_close_title,
                            JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
                    if (rc == JOptionPane.CANCEL_OPTION) {
                        return;
                    } else if (rc == JOptionPane.OK_OPTION) {
                        new ProgressListener(controller, new Runnable() {

                            @Override
                            public void run() {
                                controller.getProjectController().save();
                            }
                        }).runInEDT();
                    }
                }
                logger.info("Exiting aplication at user request");
                System.exit(0);
            }
        });
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
    public File chooseFile(Component p,
            String title, String description,
            String errorTitle, String errorDescription,
            boolean toOpen, int fileType, FileFilter ff) {
        JFileChooser jfc = new JFileChooser();
        if (ff != null) {
            jfc.setFileFilter(ff);
        }
        jfc.setFileView(eadFileView);

        File currentDirectory = new File(".");
        EditorConfig ec = controller.getConfig();
        if (ec.containsKey(EditorConf.LastDirectory)) {
            currentDirectory = new File(ec.getValue(EditorConf.LastDirectory));
        }

        jfc.setCurrentDirectory(currentDirectory);
        jfc.setDialogTitle(title);
        jfc.add(new JLabel(description), BorderLayout.NORTH);
        jfc.setFileSelectionMode(fileType);
        File f = null;
        while (f == null) {
            int rc = (toOpen ? jfc.showOpenDialog(p) : jfc.showSaveDialog(p));
            if (rc == JFileChooser.CANCEL_OPTION) {
                f = null;
                break;
            }

            f = jfc.getSelectedFile();
            if (f != null && f.getParentFile().isDirectory()) {
                currentDirectory = f.getParentFile();
                ec.put(EditorConf.LastDirectory, currentDirectory.getAbsolutePath());
                ec.save(null);
            }
            if (f == null || (!f.exists() && toOpen)
                    || (fileType == JFileChooser.FILES_ONLY && f.isDirectory())) {
                JOptionPane.showMessageDialog(p, errorDescription,
                        errorTitle, JOptionPane.ERROR_MESSAGE);
                f = null;
                continue;
            }
        }
        return f;
    }

    private static class EAdFileFilter extends FileFilter {

        private String regex;
        private String name;
        private String extension;
        private boolean strict;

        public EAdFileFilter(String extension, String regex, String name, boolean strict) {
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
                                new Object[]{file.getPath(), regex});
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
                                new Object[]{file.getPath(), regex});
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

        private static FileFilter ead2Strict = new EAdFileFilter(
                ".eap", "data[.]xml", "EAdventure 2 project folders", true);
        private static FileFilter ead1Strict = new EAdFileFilter(
                ".ead", "descriptor[.]xml", "EAdventure 1 project files", true);
        private static Icon ead2Icon = new ImageIcon(
                EAdFileView.class.getClassLoader().getResource(R.Drawable.EditorIcon16x16_png));
        private static Icon ead1xIcon = new ImageIcon(
                EAdFileView.class.getClassLoader().getResource(R.Drawable.EditorIcon16x16_bw_png));

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

    public static class ProgressListener extends JPanel implements ModelProgressListener, Runnable {

        private JProgressBar jpb;
        private JLabel jl;
        private int oldProgress = 0;
        private Runnable r;
        private Controller controller;

        public ProgressListener(Controller controller, Runnable r) {
            this.r = r;
            this.controller = controller;
        }

        public void runInEDT() {
            SwingUtilities.doInEDTNow(this);
        }

        /**
         * Should only be run in EDT
         */
        @Override
        public void run() {
            setLayout(new BorderLayout());

            jpb = new JProgressBar(0, 100);
            jpb.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(jpb, BorderLayout.CENTER);

            jl = new JLabel("hi there", JLabel.CENTER);
            jl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(jl, BorderLayout.SOUTH);

            setPreferredSize(new Dimension(400, 100));

            controller.getViewController().addModalPanel(this);
            controller.getModel().addProgressListener(this);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // business
                    try {
                        r.run();
                    } catch (Exception e) {
                        SwingUtilities.showExceptionDialog(e);
                    }
                    // cleanup
                    SwingUtilities.doInEDT(new Runnable() {
                        @Override
                        public void run() {
                            controller.getModel().removeProgressListener(ProgressListener.this);
                            controller.getViewController().removeModalPanel(true);
                        }
                    });
                }
            }).start();
        }

        @Override
        public void update(final int progress, final String text) {
            logger.info("FileMenu update: {} {} ", new Object[]{"" + progress, text});

            SwingUtilities.doInEDTNow(new Runnable() {

                @Override
                public void run() {
                    jl.setText(text);
                    if (oldProgress > progress) {
                        jpb.setIndeterminate(true);
                    } else if (jpb.isIndeterminate()) {
                        jpb.setIndeterminate(false);
                        jpb.setValue(oldProgress);
                        jpb.setValue(progress);
                    } else {
                        jpb.setValue(progress);
                    }
                    oldProgress = progress;
                }
            });
        }
    }

    public static void main(String args[]) {
//        ProgressListener pl = new ProgressListener(null, null);
//        JFrame jf = new JFrame();
//        jf.add(pl);
//        jf.pack();
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        jf.setVisible(true);
    }
}
