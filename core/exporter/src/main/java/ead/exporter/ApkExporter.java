package ead.exporter;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.maven.Maven;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;

public class ApkExporter implements Exporter {

	private Maven maven;

	public ApkExporter(Maven maven) {
		this.maven = maven;
	}

	@Override
	public void export(String gameBaseDir, String outputfolder) {
		// Load pom file
	
		File pomFile = new File("androidpom.xml");
		MavenExecutionRequest request = new DefaultMavenExecutionRequest();

		// Goals
		ArrayList<String> goals = new ArrayList<String>();
		goals.add("android:dex");
		goals.add("android:apk");
		goals.add("android:deploy");
		goals.add("android:run");
		request.setGoals(goals);

		// Properties
		Properties userProperties = new Properties();
		userProperties.setProperty("game.basedir", gameBaseDir);
		userProperties.setProperty("game.outputfolder", outputfolder);
		userProperties.setProperty("game.name", "game");
		userProperties.setProperty("eadmanifestdir", "AndroidManifest.xml");
		userProperties.setProperty("ead.assets", gameBaseDir);
		request.setUserProperties(userProperties);

		// Set files
		File basedir = new File(gameBaseDir);
		request.setBaseDirectory(basedir);
		request.setPom(pomFile);

		// Execute maven
		MavenExecutionResult result = maven.execute(request);
		for (Throwable e : result.getExceptions()) {
			e.printStackTrace();
		}

	}

}
