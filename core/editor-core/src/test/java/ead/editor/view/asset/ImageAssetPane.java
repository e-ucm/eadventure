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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ead.editor.view.asset;

import ead.editor.view.components.ZoomablePanelHolder;
import ead.editor.view.components.FileDrop;
import ead.editor.view.components.ZoomablePanel;
import ead.editor.view.components.FileDrop.Listener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author mfreire
 */
public class ImageAssetPane extends javax.swing.JPanel {

	private File imageFile;
	private ZoomableImagePanel jpCanvas;

	private Listener dropListener = new Listener() {

		@Override
		public void filesDropped(File[] files) {
			if (files.length != 1 || !files[0].exists() || !files[0].isFile()) {
				// bad file: too many, too few, non-existent or is-a-dir
			} else {
				setFile(files[0]);
			}
		}
	};

	public static void main(String[] args) {
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(new ImageAssetPane());
		jf.setSize(800, 600);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}

	private class ZoomableImagePanel extends ZoomablePanel {
		private BufferedImage image;

		public ZoomableImagePanel() {
			super(true, true);
		}

		public void refreshImage() {
			try {
				setMaxSizeReference(jpCanvasContainer);

				image = ImageIO.read(imageFile);
				baseWidth = image.getWidth();
				baseHeight = image.getHeight();
				System.err.println("Image is " + baseWidth + "x" + baseHeight);
				resetZoom();
				repaint();

				jlDescription.setText("Image '" + imageFile.getName() + "' "
						+ baseWidth + "x" + baseHeight + ", "
						+ imageFile.length() + " bytes");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e);
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
	};

	/** Creates new form ImageAssetPane */
	public ImageAssetPane() {
		initComponents();

		jpCanvas = new ZoomableImagePanel();
		ZoomablePanelHolder holder = new ZoomablePanelHolder(jpCanvas);
		jpCanvasContainer.add(holder, BorderLayout.CENTER);
		new FileDrop(null, this, true, dropListener);
	}

	private void setFile(File f) {
		if (f != null
				&& f.getName().toLowerCase().matches(".*[.](png|jpg|jpeg)")) {
			jtfFileName.setText(f.getAbsolutePath());
			imageFile = f;
			jpCanvas.refreshImage();
		}
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

	public void openExt(Object with) {
		System.err.println("opening with " + with);
	}

	public void nameChanged(String text) {
		System.err.println("name changed to " + text);
	}

	public void setNotes(String text) {
		System.err.println("notes changed to " + text);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jButton5 = new javax.swing.JButton();
		jpView = new javax.swing.JPanel();
		jlDescription = new javax.swing.JLabel();
		jpViewControls = new javax.swing.JPanel();
		jbOpenExt = new javax.swing.JButton();
		jcbOpenWith = new javax.swing.JComboBox();
		jbResetZoom = new javax.swing.JButton();
		jbZoomPixel = new javax.swing.JButton();
		jpCanvasContainer = new javax.swing.JPanel();
		jpConfigure = new javax.swing.JPanel();
		jlName = new javax.swing.JLabel();
		jlSource = new javax.swing.JLabel();
		jlNotes = new javax.swing.JLabel();
		jtfName = new javax.swing.JTextField();
		jbOpenSource = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		jtaNotes = new javax.swing.JTextArea();
		jtfFileName = new javax.swing.JTextField();

		jButton5.setText("1:1");

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
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbResetZoomActionPerformed(evt);
			}
		});
		jpViewControls.add(jbResetZoom, new java.awt.GridBagConstraints());

		jbZoomPixel.setText("Zoom 1:1");
		jbZoomPixel.addActionListener(new java.awt.event.ActionListener() {
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

		jpConfigure.setLayout(new java.awt.GridBagLayout());

		jlName.setText("Name");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_TRAILING;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jpConfigure.add(jlName, gridBagConstraints);

		jlSource.setText("Source");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_TRAILING;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jpConfigure.add(jlSource, gridBagConstraints);

		jlNotes.setText("Notes");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jpConfigure.add(jlNotes, gridBagConstraints);

		jtfName.setText("jTextField1");
		jtfName.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jtfNameActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jpConfigure.add(jtfName, gridBagConstraints);

		jbOpenSource.setText("Choose...");
		jbOpenSource.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jbOpenSourceActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
		jpConfigure.add(jbOpenSource, gridBagConstraints);

		jtaNotes.setColumns(20);
		jtaNotes.setRows(3);
		jtaNotes.setMinimumSize(new java.awt.Dimension(232, 57));
		jtaNotes
				.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
					public void propertyChange(
							java.beans.PropertyChangeEvent evt) {
						jtaNotesPropertyChange(evt);
					}
				});
		jScrollPane1.setViewportView(jtaNotes);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 0.1;
		gridBagConstraints.weighty = 0.1;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		jpConfigure.add(jScrollPane1, gridBagConstraints);

		jtfFileName.setText("jTextField2");
		jtfFileName.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jtfFileNameActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 0.1;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 1);
		jpConfigure.add(jtfFileName, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(jpConfigure, gridBagConstraints);
	}// </editor-fold>//GEN-END:initComponents

	private void jbOpenExtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOpenExtActionPerformed
		// TODO add your handling code here:
		openExt(jcbOpenWith.getSelectedItem());
	}//GEN-LAST:event_jbOpenExtActionPerformed

	private void jtfNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNameActionPerformed
		// TODO add your handling code here:
		nameChanged(jtfName.getText());
	}//GEN-LAST:event_jtfNameActionPerformed

	private void jtfFileNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfFileNameActionPerformed
		// TODO add your handling code here:
		File f = new File(jtfFileName.getText());
		if (f.exists()) {
			setFile(f);
		}
	}//GEN-LAST:event_jtfFileNameActionPerformed

	private void jbOpenSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOpenSourceActionPerformed
		// TODO add your handling code here:
		chooseFile(this, "Choose file to open", true, JFileChooser.FILES_ONLY);
	}//GEN-LAST:event_jbOpenSourceActionPerformed

	private void jtaNotesPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jtaNotesPropertyChange
		// TODO add your handling code here:
		setNotes(jtaNotes.getText());
	}//GEN-LAST:event_jtaNotesPropertyChange

	private void jbResetZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbResetZoomActionPerformed
		jpCanvas.resetZoom();
	}//GEN-LAST:event_jbResetZoomActionPerformed

	private void jbZoomPixelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbZoomPixelActionPerformed
		// TODO add your handling code here:
		jpCanvas.resetZoomNative();
	}//GEN-LAST:event_jbZoomPixelActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton5;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JButton jbOpenExt;
	private javax.swing.JButton jbOpenSource;
	private javax.swing.JButton jbResetZoom;
	private javax.swing.JButton jbZoomPixel;
	private javax.swing.JComboBox jcbOpenWith;
	private javax.swing.JLabel jlDescription;
	private javax.swing.JLabel jlName;
	private javax.swing.JLabel jlNotes;
	private javax.swing.JLabel jlSource;
	private javax.swing.JPanel jpCanvasContainer;
	private javax.swing.JPanel jpConfigure;
	private javax.swing.JPanel jpView;
	private javax.swing.JPanel jpViewControls;
	private javax.swing.JTextArea jtaNotes;
	private javax.swing.JTextField jtfFileName;
	private javax.swing.JTextField jtfName;
	// End of variables declaration//GEN-END:variables
}
