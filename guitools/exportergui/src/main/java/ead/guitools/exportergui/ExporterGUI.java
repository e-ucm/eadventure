package ead.guitools.exportergui;

import javax.swing.JFrame;

public class ExporterGUI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("eAdventure exporter");
		frame.setContentPane(new ExporterPanel( ));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
