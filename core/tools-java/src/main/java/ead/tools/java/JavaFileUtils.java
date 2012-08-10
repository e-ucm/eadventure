package ead.tools.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JavaFileUtils {
	
	public static String getText( String fileName ){
		StringBuilder text = new StringBuilder( );
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
			String line = null;
			while ( ( line = reader.readLine())!= null){
				text.append(line + "\n");
			}
			reader.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
		
		return text.toString();
	}

}
