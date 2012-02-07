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

package ead.utils.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ead.utils.Pointer;
import ead.utils.i18n.I18N;

abstract public class SwingUtilities {

	/**
	 * <p>Executes {@code doRun.run()} method synchronously with the AWT
	 * Event Dispatcher Thread (EDT).</p>
	 *
	 * <p>Unlike {@link javax.swing.SwingUtilities#invokeAndWait(Runnable)}
	 * exceptions are treated.</p>
	 * <p><ul>
	 *   <li>{@code java.lang.InterruptedException} marks current thread as interrupted, {@code Thread.currentThread().interrupt()}.</li>
	 *   <li>{@code java.lang.reflect.InvocationTargetException}: shows an error dialog including the stack trace.</li>
	 * </ul></p>
	 *
	 * @param doRun Piece of code to be run in EDT
	 *
	 * @see javax.swing.SwingUtilities#invokeAndWait(Runnable)
	 */
	static public void doInEDTNow (Runnable doRun) {
		if(javax.swing.SwingUtilities.isEventDispatchThread()){
			doRun.run();
		} else {
			try {
				javax.swing.SwingUtilities.invokeAndWait(doRun);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (InvocationTargetException e) {
				showExceptionDialog(e);
				e.printStackTrace(System.out);
			}
		}
	}

	static public void doInEDT (Runnable doRun) {
		javax.swing.SwingUtilities.invokeLater(doRun);
	}

	static public <T> T doInEDT (final ReturnnableRunnable<T> doRun){
		final Pointer<T> pointer= new Pointer<T>();
		doInEDT(new Runnable(){
			public void run(){
				pointer.reference = doRun.run();
			}
		});
		return pointer.reference;
	}

	static public <T> T doInEDTNow (final ReturnnableRunnable<T> doRun){
		final Pointer<T> pointer= new Pointer<T>();
		doInEDTNow(new Runnable(){
			public void run(){
				pointer.reference = doRun.run();
			}
		});
		return pointer.reference;
	}

  /**
   * Find the current {@code Frame}
   */
    static public Frame findActiveFrame() {
	    Frame result = null;
    	Frame[] frames = JFrame.getFrames();
	    boolean found = false;
	    int i = 0;
	    while(!found && i < frames.length){
	    	found = frames[i].isVisible();
	    	i++;
	    }
	    if(found){
	    	result = frames[i-1];
	    }
	    return result;
    }

	static public void showExceptionDialog(Throwable exp) {
		final JDialog dialog = new JDialog(findActiveFrame(),Messages.exception_dialog_title, true);

		// Label
		JPanel header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
		JLabel lbl = new JLabel();
		lbl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		String expType;
		if (exp instanceof Error) {
			expType = I18N.bind(Messages.exception_dialog_type_error , exp.getClass().getName());
		} else {
			expType = I18N.bind(Messages.exception_dialog_type_exception , exp.getClass().getName());
		}
		lbl.setText(expType);
		lbl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		header.add(lbl);

		lbl = new JLabel();
		lbl.setText(I18N.bind(Messages.exception_dialog_message, exp.getMessage()));
		header.add(lbl);

		dialog.getContentPane().add(header, BorderLayout.NORTH);
		// Stack Traces
		JTextArea traceArea = new JTextArea();
		StringBuilder buf = new StringBuilder();
		fillStackTrace(buf, exp);
		traceArea.setText(buf.toString());
		traceArea.setEditable(false);
		dialog.getContentPane().add(new JScrollPane(traceArea));

		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JButton closeBtn = new JButton(Messages.exception_dialog_button_close);
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dialog.dispose();
			}
		});
		btnPanel.add(closeBtn);
		dialog.getContentPane().add(btnPanel, BorderLayout.SOUTH);

		// Setup Dialog Size
		dialog.setSize(500, 300);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((screenSize.width / 2) - (dialog.getWidth() / 2),
				(screenSize.height / 2) - (dialog.getHeight() / 2));

		dialog.setResizable(true);

		dialog.pack();
		dialog.setVisible(true);
	}

	static private void fillStackTrace(StringBuilder buf, Throwable exp) {

		StackTraceElement traces[] = exp.getStackTrace();
		for (int i = 0; i < traces.length; i++) {
			buf.append(traces[i].toString()).append("\n");
		}

		buf.append("\n");
		Throwable cause = exp.getCause();
		if (cause != null) {
			buf.append(I18N.bind(Messages.exception_dialog_caused_by, cause.getClass().getName())+"\n");
			fillStackTrace(buf, cause);
		}
	}

	public static Rectangle calculatePaintingArea(JComponent c) {
		Dimension d = c.getSize();
		Rectangle result = new Rectangle();
		result.x = 0;
		result.y = 0;
		result.width = d.width;
		result.height = d.height;
		Insets insets = c.getInsets();
		if(insets != null){
			result.width = d.width - (insets.left+insets.right);
			result.height = d.height - (insets.top+insets.bottom);
			result.x = insets.left;
			result.y = insets.top;
		}
		return result;
	}
}
