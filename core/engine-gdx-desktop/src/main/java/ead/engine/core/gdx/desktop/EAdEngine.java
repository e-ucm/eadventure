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

package ead.engine.core.gdx.desktop;

import ead.engine.core.gdx.desktop.gui.StartFrame;


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
        
//		EAdMainDebugger.addDebugger(FieldsDebugger.class);
		StartFrame frame = new StartFrame();
		frame.setVisible(true);
		
		
//		final Injector injector = Guice.createInjector(new GdxDesktopModule(), new JavaToolsModule() );
//		GUI gui = injector.getInstance(GUI.class);
//		final Game game = injector.getInstance(Game.class);
//		gui.setGame(game);
//		gui.initialize();
//		
//		Gdx.app.postRunnable(new Runnable( ){
//
//			@Override
//			public void run() {
//				GameLoader gameLoader = injector.getInstance(GameLoader.class);
//				String file = "C:/Users/anserran/Desktop/eAdventure/juegos/test/imported/PrimerosAuxilios/";
//				String data = JavaFileUtils.getText(file + "data.xml");
//				String strings = JavaFileUtils.getText(file + "strings.xml");
//				String properties = JavaFileUtils.getText(file + "ead.properties");
//				AssetHandler assetHandler = injector.getInstance(AssetHandler.class);
//				assetHandler.setResourcesLocation(new EAdURI(file));
//				gameLoader.loadGame(data, strings, properties);				
//			}
//			
//		});

		
		
		
	}
	
}
