package ead.engine;

import ead.engine.gui.StartFrame;


/**
 * Main class for launching the eAdventure game engine
 *
 */
public class EAdEngine {
	
	public static void main(String args[]) {
        try {
            javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getSystemLookAndFeelClassName( ) );
        }
        catch( Exception e ) {
            e.printStackTrace( );
        }
        
		System.setProperty(
				"com.apple.mrj.application.apple.menu.about.name",
				"eAdventure");
        
		StartFrame frame = new StartFrame();
		frame.setVisible(true);
	}
	
}
