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

package es.eucm.eadventure.editor.view.impl;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.eucm.eadventure.editor.R;
import es.eucm.eadventure.editor.view.SplashScreen;
import es.eucm.eadventure.utils.swing.SwingUtilities;

/**
 * Default implementation of the eAdventure editor splash screen.
 */
public class SplashScreenImpl implements SplashScreen {

	/**
	 * Logger
	 */
	private static Logger logger = LoggerFactory.getLogger(SplashScreenImpl.class);
	
	/**
	 * The splash screen dialog
	 */
	protected SplashScreenDialog splashScreenDialog;
	
	/**
	 * The background image of the dialog
	 */
	protected BufferedImage image;
	
	/**
	 * The time at which the splash screen appeared
	 */
	private long startTime;
	
	/**
	 * The minimum time to display the splash screen
	 */
	private static final long MIN_TIME = 4000;
	
	@Override
	public void show() {
		logger.info("Showing Splash-screen");

		try {
			image = ImageIO.read(ClassLoader.getSystemResourceAsStream(R.Drawable.SplashScreenLogo_png));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
			public void run() {
				splashScreenDialog = new SplashScreenDialog();
				splashScreenDialog.setUndecorated(true);
				
		        int width = image.getWidth( null );
		        int height = image.getHeight( null );
		        splashScreenDialog.setSize( width, height );
		        splashScreenDialog.setResizable( false );
		        
		        double screenWidth = Toolkit.getDefaultToolkit( ).getScreenSize( ).getWidth( );
		        double screenHeight = Toolkit.getDefaultToolkit( ).getScreenSize( ).getHeight( );
		        int locX = Math.round( ( (int) screenWidth - width ) / 2.0f );
		        int locY = Math.round( ( (int) screenHeight - height ) / 2.0f );
		        splashScreenDialog.setLocation( locX, locY );
		        splashScreenDialog.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
		        
		        splashScreenDialog.setVisible(true);
			}
		});
		
		startTime = System.currentTimeMillis();
	}

	@Override
	public void hide() {
		logger.info("Hiding Splash-screen");

		if (System.currentTimeMillis() - startTime < MIN_TIME) {
			try {
				Thread.sleep(MIN_TIME - (System.currentTimeMillis() - startTime));
			} catch(Exception e) {
				
			}
		}
		
		SwingUtilities.doInEDTNow(new Runnable() {
			@Override
			public void run() {
				splashScreenDialog.setVisible(false);
				splashScreenDialog.dispose();
			}
		});
	}
	
	/**
	 * The actual splash screen dialog, that shows the image, the message
	 * and dots following it to show the user that the program is loading.
	 */
	private class SplashScreenDialog extends JDialog {
		
		private static final long serialVersionUID = 7388884935911211935L;

		/**
		 * The number of dots to be drawn at the end of the message
		 */
		private int status = 0;
				
		/**
		 * Timer used to show the dots at the end of the message
		 */
		private javax.swing.Timer timer = new javax.swing.Timer(400, new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            repaint();
	        }
	    });
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(image, 0, 0, null);
			String message = Messages.splash_screen_loading_message + " ";
			for (int i = 0; i < status; i++)
				message += ".";
			g.drawString(message, 100, 265);
			status = ++status % 5;
			timer.start();
		}

	}

}
