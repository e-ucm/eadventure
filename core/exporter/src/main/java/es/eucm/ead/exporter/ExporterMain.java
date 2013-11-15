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

package es.eucm.ead.exporter;

import es.eucm.ead.importer.AdventureConverter;
import es.eucm.ead.tools.java.utils.FileUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Properties;

import static es.eucm.ead.exporter.ExporterMain.Verbosity.*;

public class ExporterMain {

	public static void showHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("exporter [options] source destination", options);
	}

	public enum Verbosity {

		Quiet, Normal, Verbose
	}

	public static boolean checkFilesExist(CommandLine cmd, Options options,
			String... extras) {
		String[] args = cmd.getArgs();
		if (args.length != 2) {
			System.err
					.println("Expected exactly 2 extra arguments (source and destination)\n"
							+ " -- but have "
							+ args.length
							+ " extra arguments");
			showHelp(options);
			return false;
		} else if (!new File(args[0]).exists()) {
			System.err.println("Source '" + args[0] + "' not found.");
			showHelp(options);
			return false;
		} else {
			for (String s : extras) {
				if (!new File(s).exists()) {
					System.err.println("Error reading '" + s + "'");
					showHelp(options);
					return false;
				}
			}
			return true;
		}
	}

	private static Verbosity verbosity = Normal;

	@SuppressWarnings("all")
	public static void main(String args[]) {

		Options options = new Options();

		Option help = new Option("h", "help", false, "print this message");
		Option quiet = new Option("q", "quiet", false, "be extra quiet");
		Option verbose = new Option("v", "verbose", false, "be extra verbose");

		Option legacy = OptionBuilder.withArgName("s> <t").hasArgs(3)
				.withDescription(
						"source is a version 1.x game; must specify\n"
								+ "<simplify> if 'true', simplifies result\n"
								+ "<translate> if 'true', enables translation")
				.withLongOpt("import").create("i");

		Option war = OptionBuilder
				.withArgName("web-base")
				.hasArg()
				.withDescription(
						"WAR packaging (web app); "
								+ "must specify\n<web-base> the base WAR directory")
				.withLongOpt("war").create("w");

		Option jar = OptionBuilder.withDescription("JAR packaging (desktop)")
				.withLongOpt("jar").create("j");

		Option apk = OptionBuilder.withArgName("props> <adk> <d").hasArgs(3)
				.withDescription(
						"APK packaging (android); must specify \n"
								+ "<props> (a properties file) \n"
								+ "<adk> (location of the ADK to use) \n"
								+ "<deploy> ('true' to install & deploy)")
				.withLongOpt("apk").create("a");

		// EAD option
		Option ead = OptionBuilder
				.withDescription("EAD packaging (eAdventure)").withLongOpt(
						"ead").create("e");

		options.addOption(legacy);
		options.addOption(help);
		options.addOption(quiet);
		options.addOption(verbose);
		options.addOption(jar);
		options.addOption(war);
		options.addOption(apk);
		options.addOption(ead);

		CommandLineParser parser = new PosixParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException pe) {
			System.err
					.println("Error parsing command-line: " + pe.getMessage());
			showHelp(options);
			return;
		}

		// general options

		String[] extras = cmd.getArgs();

		if (cmd.hasOption(help.getOpt()) || extras.length < 2) {
			showHelp(options);
			return;
		}
		if (cmd.hasOption(verbose.getOpt())) {
			verbosity = Verbose;
		}
		if (cmd.hasOption(quiet.getOpt())) {
			verbosity = Quiet;
		}

		// import

		String source = extras[0];

		// optional import step
		if (cmd.hasOption(legacy.getOpt())) {
			String[] values = cmd.getOptionValues(legacy.getOpt());

			AdventureConverter converter = new AdventureConverter();
			if (values.length > 0 && values[0].equalsIgnoreCase("true")) {
				converter.setEnableSimplifications(true);
			}
			if (values.length > 1 && values[1].equalsIgnoreCase("true")) {
				converter.setEnableTranslations(true);
			}

			// set source for next steps to import-target
			source = converter.convert(source, null);
		}

		if (cmd.hasOption(jar.getOpt())) {
			if (checkFilesExist(cmd, options, source)) {
				JarExporter e = new JarExporter();
				e.export(source, extras[1],
						verbosity.equals(Quiet) ? new QuietStream()
								: System.err);
			}
		} else if (cmd.hasOption(apk.getOpt())) {
			String[] values = cmd.getOptionValues(apk.getOpt());
			if (checkFilesExist(cmd, options, values[0], values[1], source)) {
				AndroidExporter e = new AndroidExporter();
				Properties props = new Properties();
				File propsFile = new File(values[0]);
				try {
					props.load(new FileReader(propsFile));
					props.setProperty(AndroidExporter.SDK_HOME, props
							.getProperty(AndroidExporter.SDK_HOME, values[1]));
				} catch (IOException ioe) {
					System.err.println("Could not load properties from "
							+ propsFile.getAbsolutePath());
					return;
				}
				e.export(source, extras[1], props, values.length > 2
						&& values[2].equalsIgnoreCase("true"));
			}
		} else if (cmd.hasOption(war.getOpt())) {
			if (checkFilesExist(cmd, options, extras[0])) {
				WarExporter e = new WarExporter();
				e.setWarPath(cmd.getOptionValue(war.getOpt()));
				e.export(source, extras[1]);
			}
		} else if (cmd.hasOption(ead.getOpt())) {
			String destiny = extras[1];
			if (!destiny.endsWith(".ead")) {
				destiny += ".ead";
			}
			FileUtils.zip(new File(destiny), new File(source));
		} else {
			showHelp(options);
		}
	}

	private static class QuietStream extends PrintStream {

		public QuietStream() {
			super(new OutputStream() {
				@Override
				public void write(int b) throws IOException {
				}
			});
		}
	}
}
