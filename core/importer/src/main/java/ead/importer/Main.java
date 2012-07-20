package ead.importer;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ead.importer.EAdventure1XImporter.ImporterProgressListener;
import ead.reader.java.ReaderModule;
import ead.tools.java.JavaToolsModule;

public class Main {

	public static void main(String[] args) {
		String source = args[0];
		String destiny = args[1];
		String format = "none";
		if (args.length > 3) {
			if (args[2].equals("-f")) {
				if (args[3].equals("none") || args[3].equals("zip")
						|| args[3].equals("ead")) {
					format = args[3];
				} else {
					System.out.println("Unknown format " + args[3]);
				}
			} else {
				System.out.println("Unknown option " + args[2]);
			}
		}
		
		Injector injector = Guice.createInjector(new ImporterModule( ), new JavaToolsModule(), new ReaderModule());
		EAdventure1XImporter importer = injector.getInstance(EAdventure1XImporter.class);
	
		importer.addProgressListener(new ImporterProgressListener( ){

			@Override
			public void update(int progress, String text) {
				System.out.println(progress + "% " + text);
			}
			
		});
		
		importer.importGame(source, destiny, format);
	}

}
