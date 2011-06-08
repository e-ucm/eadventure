package es.eucm.eadventure.common.impl.importer.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.common.Importer;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.data.animation.ImageLoaderFactory;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.impl.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.loader.InputStreamCreator;
import es.eucm.eadventure.common.loader.Loader;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.effects.impl.EAdChangeAppearance;
import es.eucm.eadventure.common.model.elements.EAdCondition;
import es.eucm.eadventure.common.model.events.EAdConditionEvent;
import es.eucm.eadventure.common.model.events.impl.EAdConditionEventImpl;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdResources;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.FramesAnimation;

/**
 * Resource Importer
 * 
 * 
 */
@Singleton
public class ResourceImporterImpl implements ResourceImporter {

	/**
	 * Conditions importer
	 */
	private Importer<Conditions, EAdCondition> conditionsImporter;

	/**
	 * Animation importer
	 */
	private Importer<Animation, FramesAnimation> animationImporter;

	private ImageLoaderFactory imageLoader;

	private InputStreamCreator inputStreamCreator;

	/**
	 * Absolute path where the old adventure is placed
	 */
	private String oldAdventurePath;

	/**
	 * Absolute path where the new adventure must be placed
	 */
	private String newAdventurePath;

	/**
	 * Stores correspondences between URIs at old adventure project and URIS at
	 * new adventure project
	 */
	private Map<String, String> urisCorrespondences;

	@Inject
	public ResourceImporterImpl(
			Importer<Conditions, EAdCondition> conditionsImporter,
			Importer<Animation, FramesAnimation> animationImporter,
			ImageLoaderFactory imageLoader,
			InputStreamCreator inputStreamCreator) {
		this.imageLoader = imageLoader;
		this.inputStreamCreator = inputStreamCreator;
		this.conditionsImporter = conditionsImporter;
		this.animationImporter = animationImporter;
		urisCorrespondences = new HashMap<String, String>();

	}

	@Override
	public void setPaths(String oldAventurePath, String newAdventurePath) {
		this.oldAdventurePath = oldAventurePath;
		this.newAdventurePath = newAdventurePath;

		createFolders();
	}

	private void createFolders() {
		File f = new File(newAdventurePath + "/drawable");
		f.mkdirs();

	}

	/**
	 * Returns the new URI for an old resource situated in oldURI
	 * 
	 * @param oldURI
	 *            the old URI
	 * @return the new URI for the resource. {@code null} it there was any
	 *         problem with the import
	 */
	public String getURI(String oldURI) {
		String newURI = urisCorrespondences.get(oldURI);
		if (newURI == null) {

			String folder = getFolder(oldURI);

			String fileName = oldURI.replace("/", "_");
			newURI = folder + "/" + fileName;

			if (!copyFile(oldURI, newURI))
				return null;

			newURI = "@" + newURI;
			urisCorrespondences.put(oldURI, newURI);
		}
		return newURI;
	}

	private String getFolder(String oldURI) {
		// FIXME Use constants for directory
		return "drawable";
	}

	private boolean copyFile(String oldURI, String newURI) {
		File resourceFile = new File(oldAdventurePath, oldURI);

		File toResourceFile = new File(newAdventurePath, newURI);

		try {
			InputStream in = new FileInputStream(resourceFile);
			OutputStream out = new FileOutputStream(toResourceFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public void importResources(EAdElement element, List<Resources> resources,
			Map<String, String> resourcesStrings,
			Map<String, Class<?>> resourcesClasses) {
		int i = 0;
		// We iterate for the resources. Each resource is associated to some
		// conditions. These
		// conditions are transformed in ConditionedEvents.
		for (Resources r : resources) {
			EAdBundleId bundleId = new EAdBundleId(element.getId() + "_bundle_"
					+ i);
			element.getResources().addBundle(bundleId);

			if (i == 0) {
				element.getResources().setInitialBundle(bundleId);
			}

			for (String resourceType : resourcesStrings.keySet()) {
				String assetPath = r.getAssetPath(resourceType);
				AssetDescriptor asset = null;
				if (assetPath.endsWith("eaa")) {
					Animation a = Loader.loadAnimation(inputStreamCreator,
							assetPath, imageLoader);
					asset = animationImporter.convert(a);
				} else {
					String newAssetPath = getURI(assetPath);

					// FIXME Check exceptions
					try {
						asset = (AssetDescriptor) resourcesClasses
								.get(resourceType).getConstructor(String.class)
								.newInstance(newAssetPath);

					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}

				String propertyName = resourcesStrings.get(resourceType);
				element.getResources().addAsset(bundleId, propertyName, asset);

			}

			EAdConditionEvent conditionEvent = new EAdConditionEventImpl(
					bundleId.getBundleId() + "_condition_" + i);

			EAdCondition condition = conditionsImporter.convert(r
					.getConditions());
			conditionEvent.setCondition(condition);

			EAdChangeAppearance changeAppereance = new EAdChangeAppearance(
					conditionEvent.getId() + "change_appearence");
			conditionEvent.addEffect(
					EAdConditionEvent.ConditionedEvent.CONDITIONS_MET,
					changeAppereance);

			i++;
		}

	}

	public boolean equals(Resources oldResources, EAdResources newResources) {
		// FIXME equals resources
		return false;
	}

}
