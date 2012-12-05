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

import ead.editor.model.nodes.asset.ImageAssetNode;
import ead.editor.view.components.FileDrop;
import ead.editor.view.components.ZoomablePanel;
import ead.editor.view.components.ZoomablePanelHolder;
import ead.utils.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.common.resources.assets.drawable.basics.Image;
import ead.common.util.EAdURI;
import ead.editor.control.CommandManager;
import ead.editor.control.change.ChangeEvent;
import ead.editor.control.change.ChangeListener;
import ead.editor.control.commands.ChangeFieldValueCommand;
import ead.editor.control.commands.ChangeFileValueCommand;
import ead.editor.control.commands.FileCache;
import ead.editor.view.generic.ConvertingFieldDescriptor;
import ead.editor.view.generic.FieldDescriptorImpl;
import ead.editor.view.generic.FileNameOption;
import ead.editor.view.generic.FileOption;
import ead.editor.view.generic.Panel;
import ead.editor.view.generic.PanelImpl;
import ead.editor.view.generic.TextOption;
import ead.utils.FileUtils;

/**
 * A panel that displays all assets, by type. A preview is available
 * on the left-hand side.
 *
 * @author mfreire
 */
public class ImageAssetPanel extends AbstractElementPanel<ImageAssetNode> {

	private static final Logger logger = LoggerFactory
			.getLogger("ImageAssetPanel");

	private static FileCache fileCache;

	private ImageAssetNode imageAsset;
	private File imageFile;
	private ZoomableImagePanel jpCanvas;

	private Panel controlPanel;

	private FileDrop.Listener dropListener = new FileDrop.Listener() {

		@Override
		public void filesDropped(File[] files) {
			if (files.length != 1 || !files[0].exists() || !files[0].isFile()) {
				// bad file: too many, too few, non-existent or is-a-dir
			} else {
				setFile(files[0]);
			}
		}
	};

	private void setFile(File f) {
		if (f != null
				&& f.getName().toLowerCase().matches(".*[.](png|jpg|jpeg)")
				&& f.exists()) {
			fileOption.updateValue(f);
			imageFile = f;
			jpCanvas.refreshImage();
		} else {
			logger.warn("Bad file: {}", f.getAbsolutePath());
		}
	}

	public ImageAssetPanel() {
		initComponents();

		jpCanvas = new ZoomableImagePanel();
		ZoomablePanelHolder holder = new ZoomablePanelHolder(jpCanvas);
		jpCanvasContainer.add(holder, BorderLayout.CENTER);
		new FileDrop(null, this, true, dropListener);
	}

	private class ZoomableImagePanel extends ZoomablePanel {
		private BufferedImage image;

		public ZoomableImagePanel() {
			super(true, true);
		}

		public void refreshImage() {
			try {
				logger.info(
						"RefreshImage: setting max size reference to {}x{}",
						new Object[] { jpCanvasContainer.getWidth(),
								jpCanvasContainer.getHeight() });
				setMaxSizeReference(jpCanvasContainer);

				image = ImageIO.read(imageFile);
				baseWidth = image.getWidth();
				baseHeight = image.getHeight();
				resetZoom();
				repaint();

				jlDescription.setText("Image '" + imageFile.getName() + "' "
						+ baseWidth + "x" + baseHeight + ", "
						+ imageFile.length() + " bytes");
			} catch (Exception e) {
				logger.error("Error refreshing image to {}", imageFile);
			}
		}

		@Override
		public void paint(Graphics g) {
			g.clearRect(0, 0, width, height);
			if (image != null) {
				// try to center
				g.drawImage(image, 0, 0, width, height, this);
			}
		}
	}

	public void openExt(Object with) {
		System.err.println("opening with " + with);
	}

	public void nameChanged(String text) {
		System.err.println("name changed to " + text);
	}

	public void setNotes(String text) {
		System.err.println("notes changed to " + text);
	}

	public static File chooseFile(Component p, String message, boolean toOpen,
			int fileType) {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle(message);
		jfc.setFileSelectionMode(fileType);
		File f = null;
		while (f == null) {
			int rc = (toOpen ? jfc.showOpenDialog(p) : jfc.showSaveDialog(p));
			if (rc == JFileChooser.CANCEL_OPTION) {
				f = null;
				break;
			}

			f = jfc.getSelectedFile();
			if (f == null || (!f.exists() && toOpen)
					|| (fileType == JFileChooser.FILES_ONLY && f.isDirectory())) {
				JOptionPane.showMessageDialog(null, "Error: " + message
						+ " invalid", "Error", JOptionPane.ERROR_MESSAGE);
				f = null;
				continue;
			}
		}
		return f;
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jbZoomNative = new javax.swing.JButton();
		jpView = new javax.swing.JPanel();
		jlDescription = new javax.swing.JLabel();
		jpViewControls = new javax.swing.JPanel();
		jbOpenExt = new javax.swing.JButton();
		jcbOpenWith = new javax.swing.JComboBox();
		jbResetZoom = new javax.swing.JButton();
		jbZoomPixel = new javax.swing.JButton();
		jpCanvasContainer = new javax.swing.JPanel();
		jbZoomNative.setText("1:1");

		setLayout(new java.awt.GridBagLayout());

		jpView.setLayout(new java.awt.GridBagLayout());

		jlDescription.setText("<no image loaded>");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jpView.add(jlDescription, gridBagConstraints);

		jpViewControls.setLayout(new java.awt.GridBagLayout());

		jbOpenExt.setText("Open with");
		jbOpenExt.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbOpenExtActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		jpViewControls.add(jbOpenExt, gridBagConstraints);

		jcbOpenWith.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"Item 1", "Item 2", "Item 3", "Item 4" }));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 16);
		jpViewControls.add(jcbOpenWith, gridBagConstraints);

		jbResetZoom.setText("Zoom to fit");
		jbResetZoom.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbResetZoomActionPerformed(evt);
			}
		});
		jpViewControls.add(jbResetZoom, new java.awt.GridBagConstraints());

		jbZoomPixel.setText("Zoom 1:1");
		jbZoomPixel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbZoomPixelActionPerformed(evt);
			}
		});
		jpViewControls.add(jbZoomPixel, new java.awt.GridBagConstraints());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jpView.add(jpViewControls, gridBagConstraints);

		jpCanvasContainer.setBackground(new java.awt.Color(245, 246, 249));
		jpCanvasContainer.setLayout(new java.awt.BorderLayout());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jpView.add(jpCanvasContainer, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0.2;
		gridBagConstraints.weighty = 0.2;
		add(jpView, gridBagConstraints);
	}

	private void jbOpenExtActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		openExt(jcbOpenWith.getSelectedItem());
	}

	private void jbResetZoomActionPerformed(java.awt.event.ActionEvent evt) {
		jpCanvas.resetZoom();
	}

	private void jbZoomPixelActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		jpCanvas.resetZoomNative();
	}

	// Variables declaration - do not modify
	private javax.swing.JButton jbZoomNative;
	private javax.swing.JButton jbOpenExt;
	private javax.swing.JButton jbResetZoom;
	private javax.swing.JButton jbZoomPixel;
	private javax.swing.JComboBox jcbOpenWith;
	private javax.swing.JLabel jlDescription;
	private javax.swing.JPanel jpCanvasContainer;
	private javax.swing.JPanel jpView;
	private javax.swing.JPanel jpViewControls;

	private FileOption fileOption;

	// End of variables declaration

	@Override
	protected void rebuild() {
		imageAsset = (ImageAssetNode) target;
		Image image = (Image) imageAsset.getFirst().getContent();

		if (fileCache == null) {
			try {
				fileCache = new FileCache(FileUtils.createTempDir("ead",
						"cache"));
			} catch (Exception e) {
				logger.error("Could not initialize cache");
			}
		}

		if (controlPanel == null) {
			controlPanel = new PanelImpl("Configuration",
					Panel.LayoutPolicy.VerticalBlocks, 4);
			final FileNameOption fno = new FileNameOption("Name", "Asset Name",
					new ConvertingFieldDescriptor<String, EAdURI>(
							new FieldDescriptorImpl<EAdURI>(image, "uri")) {
						@Override
						public String innerToOuter(EAdURI b) {
							return b.toStringData();
						}

						@Override
						public EAdURI outerToInner(String a) {
							return new EAdURI(a);
						}
					}, false);
			fileOption = new FileOption("Source",
					"Asset source-file (.png or .jpg only)", "Choose...",
					new FieldDescriptorImpl<File>(imageAsset, "source"),
					fileCache);
			controlPanel.addElement(fno);
			controlPanel.addElement(fileOption);
			controlPanel.addElement(new TextOption("Notes",
					"Notes on this asset", new FieldDescriptorImpl<String>(
							imageAsset, "notes"),
					TextOption.ExpectedLength.LONG));

			CommandManager manager = controller.getCommandManager();
			GridBagConstraints gbc = new GridBagConstraints(0, 1, 1, 1, .1, .1,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
					new Insets(4, 4, 4, 4), 0, 0);
			add(controlPanel.getComponent(manager), gbc);

			manager.addChangeListener(new ChangeListener<ChangeEvent>() {
				@Override
				public void processChange(ChangeEvent event) {
					if (event.hasChanged(fileOption.getFieldDescriptor())) {
						// the source file has changed: overwrite target, view target
						ChangeFileValueCommand c = (ChangeFileValueCommand) event;
						c.writeFile(imageAsset.getFile());
						setFile(imageAsset.getFile());
					} else if (event.hasChanged(fno.getFieldDescriptor())) {
						// the target has changed - remove old, switch to new
						ChangeFieldValueCommand<String> c = (ChangeFieldValueCommand<String>) event;
						String currentUri = fno.getFieldDescriptor().read();
						File prev = currentUri.equals(c.getNewValue()) ? imageAsset
								.resolveUri(c.getOldValue())
								: imageAsset.resolveUri(c.getNewValue());
						File f = imageAsset.resolveUri(currentUri);
						prev.renameTo(f);
					}
				}
			});
		}

		// FIXE: should not be necessary, but button-press needed otherwise
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.yield();
					Thread.sleep(100);
					SwingUtilities.doInEDT(new Runnable() {
						@Override
						public void run() {
							setFile(imageAsset.getFile());
						}
					});
				} catch (InterruptedException ie) {
					logger.warn("Interrupted while napping before image-set");
				}
			}
		}).start();
	}
}