package es.eucm.eadventure.engine.core.platform.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.inject.Injector;

import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.engine.core.platform.FontHandler;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

public abstract class JavaAbstractAssetHandler extends AbstractAssetHandler {

	/**
	 * An instance of the guice injector, used to load the necessary runtime
	 * assets
	 */
	private Injector injector;

	
	public JavaAbstractAssetHandler(
			Injector injector,
			Map<Class<? extends AssetDescriptor>, Class<? extends RuntimeAsset<?>>> classMap, FontHandler fontHandler ) {
		super(classMap, fontHandler);
		this.injector = injector;
	}

	public RuntimeAsset<?> getInstance(Class<? extends RuntimeAsset<?>> clazz) {
		return injector.getInstance(clazz);
	}

	/**
	 * Helper method to create a directory within the system temporary directory
	 * 
	 * @param name
	 *            The name of the directory
	 * @return The reference to the directory
	 * @throws IOException
	 *             A exception if the directory couldn't be created
	 */
	protected File createTempDirectory(String name) throws IOException {
		final File temp;

		temp = File.createTempFile(name, null);

		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: "
					+ temp.getAbsolutePath());
		}

		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: "
					+ temp.getAbsolutePath());
		}

		return (temp);
	}

}
