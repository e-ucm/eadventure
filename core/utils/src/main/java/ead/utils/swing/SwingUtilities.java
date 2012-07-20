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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ead.utils.Pointer;
import ead.utils.i18n.I18N;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.*;

abstract public class SwingUtilities {

    private static final Logger logger = LoggerFactory.getLogger("SwingUtilities");

    /**
     * <p> Executes {@code doRun.run()} method synchronously with the AWT Event
     * Dispatcher Thread (EDT). </p>
     *
     * <p> Unlike {@link javax.swing.SwingUtilities#invokeAndWait(Runnable)}
     * exceptions are treated. </p> <p> <ul> <li>{@code java.lang.InterruptedException}
     * marks current thread as interrupted, {@code Thread.currentThread().interrupt()}.</li> <li>{@code java.lang.reflect.InvocationTargetException}:
     * shows an error dialog including the stack trace.</li> </ul> </p>
     *
     * @param doRun Piece of code to be run in EDT
     *
     * @see javax.swing.SwingUtilities#invokeAndWait(Runnable)
     */
    static public void doInEDTNow(Runnable doRun) {
        if (javax.swing.SwingUtilities.isEventDispatchThread()) {
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

    static public void doInEDT(Runnable doRun) {
        javax.swing.SwingUtilities.invokeLater(doRun);
    }

    static public <T> T doInEDT(final ReturnnableRunnable<T> doRun) {
        final Pointer<T> pointer = new Pointer<T>();
        doInEDT(new Runnable() {

            public void run() {
                pointer.reference = doRun.run();
            }
        });
        return pointer.reference;
    }

    static public <T> T doInEDTNow(final ReturnnableRunnable<T> doRun) {
        final Pointer<T> pointer = new Pointer<T>();
        doInEDTNow(new Runnable() {

            public void run() {
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
        while (!found && i < frames.length) {
            found = frames[i].isVisible();
            i++;
        }
        if (found) {
            result = frames[i - 1];
        }
        return result;
    }

    /**
     * Shared dialog for all exceptions
     */
    static private JDialog exceptionDialog = null;
    static private JEditorPane exceptionEditorPane = null;
    static private ArrayList<String> exceptionsSoFar = new ArrayList<String>();
    
    /**
     * Display an exception in the exception dialog. Will create a new
     * exception dialog if none was currently being displayed.
     * @param exp the throwable to display
     */
    static public void showExceptionDialog(final Throwable exp) {
        
        logger.error("Exception captured by swing", exp);
    
        doInEDTNow(new Runnable() { 
            @Override
            public void run() {
                showExceptionDialogEDT(exp); 
            }
        });
    }
    
    /**
     * Display an exception in the exception dialog. 
     */    
    static private void showExceptionDialogEDT(final Throwable exp) {
        if (exceptionDialog == null) {
            exceptionDialog = new JDialog(
                    findActiveFrame(), Messages.exception_dialog_title, false);
            exceptionDialog.setLayout(new BorderLayout());
            
            exceptionEditorPane = new JEditorPane("text/html", "");
            exceptionEditorPane.setEditable(false);
            exceptionDialog.add(new JScrollPane(exceptionEditorPane), BorderLayout.CENTER);
            
            // Add close button
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            JButton closeBtn = new JButton(Messages.exception_dialog_button_close);
            closeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    exceptionDialog.dispose();
                    exceptionsSoFar.clear();
                    exceptionDialog = null;
                }
            });
            btnPanel.add(closeBtn);
            exceptionDialog.getContentPane().add(btnPanel, BorderLayout.SOUTH);

            // Setup Dialog Size, make visible
            int w = exceptionDialog.getWidth();
            int h = exceptionDialog.getHeight();
            exceptionDialog.setSize(500, 300);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            exceptionDialog.setLocation((screenSize.width / 2) - (w / 2),
                    (screenSize.height / 2) - (h / 2));

            exceptionDialog.setResizable(true);
            exceptionDialog.setVisible(true);            
        }

        // Contents
        StringBuilder sb = new StringBuilder();
        String expType = I18N.bind((exp instanceof Error) ?
                Messages.exception_dialog_type_error : 
                Messages.exception_dialog_type_exception, 
                exp.getClass().getName());        
        sb.append("<h3>" + expType
                + "<a name='a" + (exceptionsSoFar.size()+1) + "'></a>"
				+ "</h3>");
        sb.append("<div style='font-size:90%; padding-left: 10px;'><pre>");
        fillStackTrace(sb, exp);
        sb.append("</pre></div>");
        exceptionsSoFar.add(sb.toString());
        StringBuilder html = new StringBuilder("<html><head></head><body>");
        for (String s: exceptionsSoFar) {
            html.append(s);
        }
        html.append("</body></html>");
        exceptionEditorPane.setText(html.toString());
        exceptionEditorPane.scrollToReference("a"+exceptionsSoFar.size());
    }

    static private void fillStackTrace(StringBuilder buf, Throwable exp) {

        buf.append("<b>")
           .append(I18N.bind(Messages.exception_dialog_message, 
				exp.getLocalizedMessage()))
           .append("</b>\n");
        StackTraceElement traces[] = exp.getStackTrace();
        for (int i = 0; i < traces.length; i++) {
            buf.append(traces[i].toString()).append("\n");
        }
        Throwable cause = exp.getCause();
        if (cause != null) {
            String className = cause.getClass().getName();
            buf.append("<b>")
			   .append(I18N.bind(Messages.exception_dialog_caused_by, className))
               .append("</b>\n");
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
        if (insets != null) {
            result.width = d.width - (insets.left + insets.right);
            result.height = d.height - (insets.top + insets.bottom);
            result.x = insets.left;
            result.y = insets.top;
        }
        return result;
    }
}
