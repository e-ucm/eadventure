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

package es.eucm.ead.editor.util;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Swing exception handler. <p>Usage:</p>
 * <pre>{@code
 *
 * import javax.swing.SwingUtilities;
 * import java.lang.reflect.InvocationTargetException;
 * import es.ucm.fdi.swing.SwingExceptionHandler;
 *
 * public class Foo {
 *
 *   public void createGUIAndShow(){
 *       final JFrame frame = new JFrame();
 *       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 *       JButton btn = new JButton("Push me");
 *       btn.addActionListener(new ActionListener(){
 *           public void actionPerformed(ActionEvent e){
 *               throw new RuntimeException("Ups ");
 *           }
 *       }); frame.add(btn); frame.pack(); frame.setSize(400, 300);
 * frame.setVisible(true); }
 *
 * public static void main(String[] args) { final Foo foo = new Foo(); try {
 * SwingUtilities.invokeAndWait(new Runnable(){ public void run() {
 * Thread.currentThread() .setUncaughtExceptionHandler(
 * SwingExceptionHandler.getInstance());
 * Thread.setDefaultUncaughtExceptionHandler(
 * SwingExceptionHandler.getInstance());
 *
 *                   // Insert Swing initialization code here, for example
 * foo.createGUIAndShow(); } }); }catch(InterruptedException e){ // Catch thread
 * interruption Thread.currentThread().interrupt();
 * }catch(InvocationTargetException e){ // catches exceptions thrown inside
 * invokeAndWait() e.printStackTrace(); } } } }</pre>
 *
 * @author <a href="mailto:imartinez@fdi.ucm.es">Ivan Martinez-Ortiz</a>
 *
 */
public class SwingExceptionHandler implements
		java.lang.Thread.UncaughtExceptionHandler {

	private static SwingExceptionHandler theInstance;

	private SwingExceptionHandler() {
	}

	public static UncaughtExceptionHandler getInstance() {
		synchronized (SwingExceptionHandler.class) {
			if (theInstance == null) {
				theInstance = new SwingExceptionHandler();
			}
		}
		return theInstance;
	}

	@Override
	public void uncaughtException(Thread t, final Throwable e) {
		SwingUtilities.showExceptionDialog(e);
	}
}
