/** 
 * Globally-defined functions, always accessible.
 * This file is loaded before r.java
 */

// access to logging
var log = Packages.org.slf4j.LoggerFactory.getLogger('js');

// dump JSONified
function dump() {
	for (var i=0; i<arguments.length; i++) {
		print(JSON.stringify(arguments[i]));
	}
}

// access to editor-package
var editor = Packages.es.eucm.ead.editor;

// programmatically set log-level
function setLogLevel(logger, level) {
	editor.util.Log4jConfig.setLevel(logger, level);
}

// load-from-js
function load(name) {
	var utils = Packages.es.eucm.ead.tools.java.utils;
	if (name[0] == '.') {
		name = '/scripts' + name.substring(1);
	}
	log.debug('loading `' + name + '`');
	// important: Java Strings are not actually JS Strings; they can be appended, though
	eval('' + utils.FileUtils.loadResourceToString(name));
}

// load (rhino-compatible, abridged) require.js
load('/scripts/r.js');

var help =
"    Help\n\
--------------\n\
 print(x)			print a variable value\n\
 dump(...)			dump JSONified args\n\
 log				direct access to a logger (use log.debug, log.debug, ...)\n\
 setLogLevel(x,y)	set log-level of package x to level y\n\
 \n\
 editor				access to the es.eucm.ead.editor package\n\\n\
 \n\
";
