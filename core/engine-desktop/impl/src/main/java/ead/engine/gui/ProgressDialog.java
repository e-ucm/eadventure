package ead.engine.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import ead.common.importer.EAdventure1XImporter;

public class ProgressDialog extends JDialog {

	private static final long serialVersionUID = -2805883406261079968L;
	
	private JProgressBar progressBar;
	
	private JLabel progressText;
	
	private EAdventure1XImporter importer;
	
	private Runnable runnable = new Runnable( ){

		@Override
		public void run() {
			if ( importer.getProgress() < 100 ){
				progressBar.setValue(importer.getProgress());
				progressText.setText(importer.getProgressText());
				invalidate();
				repaint();
				new Thread( runnable ).start();
			}
			else {
				ProgressDialog.this.setVisible(false);
			}
		}
		
	};
	
	public ProgressDialog(JFrame frame, EAdventure1XImporter importer){
		super(frame, "Loading...");
		this.importer = importer;
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(importer.getProgress());
		progressText = new JLabel( "Loading..." );
		JPanel p =new JPanel(new GridBagLayout( ));
		GridBagConstraints c = new GridBagConstraints( );
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets( 5, 5, 5, 5 );
		p.add(progressText, c);
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		p.add(progressBar, c);
		this.add(p);
		this.setUndecorated(true);
		pack();
		setLocationRelativeTo(null);
	}

	
	public void setVisible(boolean v ){
		super.setVisible(v);
		if ( v ){
			progressBar.setValue(0);
			progressText.setText("Loading...");
			new Thread( runnable ).start();
		}
		
	}
}
