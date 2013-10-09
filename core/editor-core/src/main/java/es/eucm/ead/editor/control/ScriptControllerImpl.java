/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.control;

import es.eucm.ead.editor.view.components.OutputLogPanel;
import es.eucm.ead.tools.java.utils.FileUtils;
import java.io.IOException;
import java.io.Writer;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.org.mozilla.javascript.Context;
import sun.org.mozilla.javascript.ContextFactory;

/**
 *
 * @author mfreire
 */
public class ScriptControllerImpl implements ScriptController {

	private static Logger logger = LoggerFactory
			.getLogger(ScriptControllerImpl.class);

	private ScriptEngineManager mgr = new ScriptEngineManager();
	private ScriptEngine jsEngine;

	public ScriptControllerImpl() {
		jsEngine = mgr.getEngineByName("JavaScript");
		ScriptContext sc = jsEngine.getContext();
		sc.setWriter(new Writer() {
			private StringBuilder sb = new StringBuilder();
			private Logger jsLog = LoggerFactory.getLogger("js");
			@Override
			public void write(char[] cbuf, int off, int len) throws IOException {
				sb.append(cbuf, off, len);
			}

			@Override
			public void flush() throws IOException {
				jsLog.info(sb.toString());
				sb.setLength(0);
			}

			@Override
			public void close() throws IOException {}
		});
		refreshScripts();
	}

	@Override
	public void refreshScripts() {
		logger.info("Refreshing scripts...");
		
		jsEngine.setBindings(jsEngine.createBindings(),
				ScriptContext.GLOBAL_SCOPE);
		try {
			String r = FileUtils.loadResourceToString("/scripts/env.js");
			eval(r, null, "refresh");
		} catch (IOException ex) {
			logger.warn("Error reading resource", ex);
		}
	}
	
	@Override
	public Object eval(String script, OutputLogPanel out, String source) {
		try {
			long ms = System.currentTimeMillis();
			Object output = jsEngine.eval(script);
			ms = System.currentTimeMillis() - ms;
			if (out != null) {
				out.append("Evaluated " + script.length() 
						+ " chars from " + source + " in " + ms + " ms:", "");
				out.append(" -- ", (output == null) ? "(null)" : output
						.toString());
			} else {
				logger.info("Evaluated {} chars from {} in {} ms: {}", script.length(), source, ms, output);
			}
			return output;
		} catch (ScriptException ex) {
			Throwable c = ex.getCause();
			while (c != null && !(c.getClass().getName().endsWith("EcmaError"))) {
				c = c.getCause();
			}
			String error = (c == null ? ex.getMessage() : c.getMessage());
			if (out != null) {
				out.append(" E ", error);
				logger.info(error, ex);
			} else {
				logger.info(" E " + error, ex);
			}
			return null;
		}
	}
}
