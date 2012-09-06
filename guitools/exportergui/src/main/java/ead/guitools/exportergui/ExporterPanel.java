package ead.guitools.exportergui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ExporterPanel extends JPanel {

	private static final long serialVersionUID = 5314650713816468297L;

	private JFileChooser fileChooser = new JFileChooser();

	private JTextField inputField;

	private JTextField outputField;

	private JCheckBox war;

	private JCheckBox apk;

	private JCheckBox jar;

	private JCheckBox install;

	private JTextField nameField;

	private JLabel status;

	private ExporterController exporter;

	public ExporterPanel() {
		this.exporter = new ExporterController();
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		JLabel inputLabel = new JLabel("Input game");
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.LINE_START;
		add(inputLabel, c);
		inputField = new JTextField();
		inputField.setText("(No game selected)");
		inputField.setEnabled(false);
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		add(inputField, c);
		JButton button = new JButton("Select");
		c.gridx = 2;
		c.gridwidth = 1;
		add(button, c);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser
						.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if (fileChooser.showOpenDialog(ExporterPanel.this) == JFileChooser.APPROVE_OPTION) {
					inputField.setText(fileChooser.getSelectedFile()
							.getAbsolutePath());
				}
			}

		});

		// Output
		JLabel outputLabel = new JLabel("Output directory");
		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.LINE_START;
		add(outputLabel, c);
		outputField = new JTextField();
		outputField.setText("(No output directory selected)");
		outputField.setEnabled(false);
		c.gridy = 4;
		c.gridx = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		add(outputField, c);
		JButton button2 = new JButton("Select");
		c.gridx = 2;
		c.gridwidth = 1;
		add(button2, c);
		button2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fileChooser.showOpenDialog(ExporterPanel.this) == JFileChooser.APPROVE_OPTION) {
					outputField.setText(fileChooser.getSelectedFile()
							.getAbsolutePath());
				}
			}

		});

		install = new JCheckBox("Install apk in connected Android device");
		install.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (install.isSelected()) {
					apk.setSelected(true);
					apk.setEnabled(false);
				} else {
					apk.setEnabled(true);
				}

			}

		});
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 5;
		add(install, c);

		JPanel panel = new JPanel();
		war = new JCheckBox(".WAR");
		apk = new JCheckBox(".APK");
		jar = new JCheckBox(".JAR");

		panel.add(jar);
		panel.add(war);
		panel.add(apk);

		c.gridx = 0;
		c.gridy = 6;
		add(panel, c);

		c.gridy = 7;
		this.add(new JLabel("Name for the game"), c);
		c.gridy = 8;
		nameField = new JTextField("eAdventureGame");
		add(nameField, c);

		c.gridy = 9;
		JButton runButton = new JButton("Export");
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				export();
			}

		});
		add(runButton, c);

		status = new JLabel(" ");
		c.gridy = 10;
		add(status, c);
	}

	public void export() {
		status.setText("Exporting...");

		new Thread() {
			public void run() {
				String gameBase = inputField.getText();
				String outputFolder = outputField.getText();			
				exporter.export(nameField.getText(), gameBase,
						outputFolder, jar.isSelected(), war.isSelected(),
						apk.isSelected(), install.isSelected());
				status.setText("Done.");
			}

		}.start();

	}

}
