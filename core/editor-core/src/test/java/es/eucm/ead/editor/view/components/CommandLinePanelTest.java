/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.view.components;

import es.eucm.ead.editor.util.Log4jConfig;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfreire
 */
public class CommandLinePanelTest {

	private static Logger logger = LoggerFactory
			.getLogger(CommandLinePanelTest.class);

	public CommandLinePanelTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of getText method, of class CommandLinePanel.
	 */
	@Test
	public void testGetText() {
		System.out.println("getText");
		CommandLinePanel instance = new CommandLinePanel();
		String expResult = "";
		String result = instance.getText();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setText method, of class CommandLinePanel.
	 */
	@Test
	public void testSetText() {
		System.out.println("setText");
		String text = "";
		CommandLinePanel instance = new CommandLinePanel();
		instance.setText(text);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/*
	
	 http://docs.oracle.com/javase/6/docs/technotes/guides/scripting/programmer_guide/#interfaces
	
	 var x = new JavaImporter(Packages.es.eucm.ead.editor.util.Log4jConfig);
	 x.Log4jConfig.configForConsole(x.Log4jConfig.Slf4jLevel.Warn);
	 x

	 var logConfig = new JavaImporter(
	 Packages.es.eucm.ead.editor.util.Log4jConfig).Log4jConfig.configForConsole;
	 var logLevel = new JavaImporter(
	 Packages.es.eucm.ead.editor.util.Log4jConfig).Log4jConfig.Slf4jLevel;
	 logConfig(logLevel.Warn);
	 logLevel	
	
	 var SwingGui = new JavaImporter(javax.swing,
	 javax.swing.event,
	 javax.swing.border,
	 java.awt.event);
	 with (SwingGui) {
	 var mybutton = new JButton("test");
	 var myframe = new JFrame("test");
	 myframe.add(mybutton);
	 myframe.setSize(800, 600);
	 myframe.setVisible(true);
	 }	
	
	 */

	/**
	 * main entrypoint
	 * @param args 
	 */
	public static void main(String[] args) {
		Log4jConfig.configForConsole(Log4jConfig.Slf4jLevel.Debug);

		ScriptEngineManager mgr = new ScriptEngineManager();
		final ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");

		final JFrame jf = new JFrame("Testing CMD");
		jf.setLayout(new BorderLayout());
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final CommandLinePanel cmd = new CommandLinePanel();
		final OutputLogPanel out = new OutputLogPanel();
		JSplitPane js = new JSplitPane(JSplitPane.VERTICAL_SPLIT, out, cmd);
		js.setResizeWeight(1d);
		cmd.setReferenceComponent(cmd, 50);
		out.setReferenceComponent(out, 10);
		
		final Action executeAction = new AbstractAction("execute") {
			@Override
			public void actionPerformed(ActionEvent e) {
				String toDo = cmd.getText();
				cmd.pushToHistory();
				try {
					long ms = System.currentTimeMillis();
					Object output = jsEngine.eval(toDo);
					ms = System.currentTimeMillis() - ms;
					out.append("Evaluated in " + ms + " ms:", "");
					out.append(" -- ", (output == null) ? "(null)" : output
							.toString());
				} catch (ScriptException ex) {
					Throwable c = ex.getCause();
					while (c != null
							&& !(c.getClass().getName().endsWith("EcmaError"))) {
						c = c.getCause();
					}
					String error = (c == null ? ex.getMessage() : c
							.getMessage());
					out.append(" E ", error);
				}
			}
		};
		
		cmd.setInteractive(new Runnable() {
			@Override
			public void run() {
				executeAction.actionPerformed(null);
			}			
		});

		JButton execute = new JButton("run");
		execute.addActionListener(executeAction);
		jf.add(js, BorderLayout.CENTER);
		jf.add(execute, BorderLayout.SOUTH);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				jf.setSize(800, 600);
				jf.setVisible(true);
				cmd.setText("");
				out.setText("");
			}
		});
	}
}
