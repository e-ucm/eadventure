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

package ead.importer.resources;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.ead.model.interfaces.features.ResourcedEvented;
import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.drawable.basics.animation.Frame;
import es.eucm.ead.model.assets.drawable.basics.animation.FramesAnimation;
import es.eucm.ead.model.elements.ResourcedElement;
import es.eucm.ead.model.elements.conditions.EmptyCond;
import es.eucm.ead.model.elements.effects.TriggerMacroEf;
import es.eucm.ead.model.elements.events.SceneElementEv;
import es.eucm.ead.model.elements.events.enums.SceneElementEvType;
import es.eucm.ead.model.elements.extra.EAdList;
import es.eucm.ead.model.elements.predef.effects.ChangeAppearanceEf;
import ead.importer.EAdElementImporter;
import ead.importer.GenericImporter;
import ead.importer.interfaces.ResourceImporter;
import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.common.data.animation.ImageLoaderFactory;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.loader.InputStreamCreator;
import es.eucm.eadventure.common.loader.Loader;

/**
 * Resource Importer
 *
 *
 */
@Singleton
public class ResourceImporterImpl implements ResourceImporter {

	static private Logger logger = LoggerFactory
			.getLogger(ResourceImporterImpl.class);

	/**
	 * Conditions importer
	 */
	private EAdElementImporter<Conditions, Condition> conditionsImporter;

	/**
	 * Animation importer
	 */
	private GenericImporter<Animation, FramesAnimation> animationImporter;

	private ImageLoaderFactory imageLoader;

	private InputStreamCreator inputStreamCreator;

	/**
	 * Absolute path where the new adventure must be placed
	 */
	private String newAdventurePath;

	/**
	 * Stores correspondences between Strings at old adventure project and StringS at
	 * new adventure project
	 */
	private Map<String, String> urisCorrespondences;

	/**
	 * A map matching old uris with the new assets
	 */
	private Map<String, AssetDescriptor> assets;

	@Inject
	public ResourceImporterImpl(
			EAdElementImporter<Conditions, Condition> conditionsImporter,
			GenericImporter<Animation, FramesAnimation> animationImporter,
			ImageLoaderFactory imageLoader,
			InputStreamCreator inputStreamCreator) {
		this.imageLoader = imageLoader;
		this.inputStreamCreator = inputStreamCreator;
		this.conditionsImporter = conditionsImporter;
		this.animationImporter = animationImporter;
		urisCorrespondences = new LinkedHashMap<String, String>();
		assets = new LinkedHashMap<String, AssetDescriptor>();

	}

	@Override
	public void setPath(String newAdventurePath) {
		this.newAdventurePath = newAdventurePath;
		this.urisCorrespondences.clear();
		this.assets.clear();
		createFolders();
	}

	private void createFolders() {
		File f = new File(newAdventurePath + "/drawable");
		f.mkdirs();
		f = new File(newAdventurePath + "/binary");
		f.mkdirs();

	}

	@Override
	public String getString(String oldString) {
		String newString = urisCorrespondences.get(oldString);
		if (newString == null) {

			String folder = getFolder(oldString);

			String fileName = oldString.replace("/", "_");
			newString = folder + "/" + fileName;

			if (!copyFile(oldString, newString)) {
				logger.error("Missing resource: {}", oldString);
				return null;
			}

			newString = "@" + newString;
			urisCorrespondences.put(oldString, newString);
		}
		return newString;
	}

	private String getFolder(String oldString) {
		if (oldString.endsWith(".png") || oldString.endsWith(".jpg"))
			return DRAWABLE;
		else
			return BINARY;
	}

	@Override
	public boolean copyFile(String oldString, String newString) {

		File toResourceFile = new File(newAdventurePath, newString);
		InputStream in = null;
		OutputStream out = null;
		boolean success = false;

		try {
			in = inputStreamCreator.buildInputStream(oldString);
			if (in != null) {
				out = new FileOutputStream(toResourceFile);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				success = true;
			}

		} catch (Exception e) {
			logger.error("Error copying '{}' to '{}'", oldString, newString);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("Error accesing '{}'", oldString);
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("Error accesing '{}'", newString);
				}
			}
		}
		return success;

	}

	@Override
	public void importResources(ResourcedEvented element,
			List<Resources> resources, Map<String, String> resourcesStrings,
			Map<String, Object> resourcesObjectClasses) {
		int i = 0;
		// We iterate for the resources. Each resource is associated to some
		// conditions.
		List<String> bundles = new ArrayList<String>();
		List<Condition> conditions = new ArrayList<Condition>();
		for (Resources r : resources) {
			String bundleId;
			if (i == 0) {
				bundleId = ResourcedElement.INITIAL_BUNDLE;
			} else {
				bundleId = "bundle_" + i;
			}
			bundles.add(bundleId);
			Condition c = conditionsImporter.init(r.getConditions());
			c = conditionsImporter.convert(r.getConditions(), c);
			conditions.add(c);

			for (String resourceType : resourcesStrings.keySet()) {
				String assetPath = r.getAssetPath(resourceType);
				Object o = resourcesObjectClasses.get(resourceType);
				AssetDescriptor asset = null;
				if (o instanceof Class) {
					asset = this.getAssetDescritptor(assetPath, (Class<?>) o);
				} else if (o instanceof List) {
					asset = (AssetDescriptor) ((List<?>) o).get(i);
				} else if (o instanceof AssetDescriptor) {
					asset = (AssetDescriptor) o;
				}

				if (asset != null) {
					String propertyName = resourcesStrings.get(resourceType);
					element.addAsset(bundleId, propertyName, asset);
				}

			}
			i++;
		}

		if (resources.size() > 0) {
			setBundlesEvent(element, conditions, bundles);
		}

	}

	public static void setBundlesEvent(ResourcedEvented element,
			List<Condition> conditions, List<String> bundles) {
		if ((conditions.size() == 1 || EmptyCond.TRUE.equals(conditions.get(0)))
				&& bundles.size() >= 1) {
			return;
		} else {
			int i = 0;
			TriggerMacroEf changeBundle = new TriggerMacroEf();
			for (Condition c : conditions) {
				ChangeAppearanceEf effect = new ChangeAppearanceEf(null,
						bundles.get(i));
				changeBundle.putEffects(c, new EAdList<Effect>());
				i++;
			}
			SceneElementEv event = new SceneElementEv(
					SceneElementEvType.ALWAYS, changeBundle);
			element.addEvent(event);

		}
	}

	@Override
	public AssetDescriptor getAssetDescritptor(String assetPath, Class<?> clazz) {
		if (assetPath == null)
			return null;

		if (assets.containsKey(assetPath)) {
			return assets.get(assetPath);
		}

		AssetDescriptor asset = null;

		// Special case
		if (assetPath.startsWith("assets/special/EmptyAnimation")) {
			if (assetPath.endsWith("_01.png")) {
				String uri = this.getString(assetPath);
				return new Image(uri);
			} else if (!(assetPath.endsWith(".eaa") || assetPath
					.endsWith("_01.png")))
				if (fileExists(assetPath + ".eaa"))
					assetPath += ".eaa";
				else if (fileExists(assetPath + "_01.png")) {
					assetPath += "_01.png";
				} else if (fileExists(assetPath + "_01.jpg")) {
					assetPath += "_01.jpg";
				} else {
					logger.info("There was a problem with EmptyAnimation");
				}
		}

		if (assetPath.startsWith("assets/animation")
				|| assetPath.startsWith("assets/special/EmptyAnimation")) {
			if (assetPath.endsWith(".eaa")
					|| (getFileExtension(assetPath) == null && fileExists(assetPath
							+ ".eaa"))) {
				Animation a = Loader.loadAnimation(inputStreamCreator,
						assetPath, imageLoader);
				asset = animationImporter.init(a);
				asset = animationImporter.convert(a, asset);
			} else {
				asset = importImagesAnimation(assetPath);
			}
		} else {
			String newAssetPath = getString(assetPath);

			try {
				asset = (AssetDescriptor) clazz.getConstructor(String.class)
						.newInstance(newAssetPath);

			} catch (Exception e) {
				logger.warn("Error while playing with AssetDescriptor {}",
						newAssetPath, e);
			}
		}

		if (asset != null)
			assets.put(assetPath, asset);
		return asset;

	}

	/**
	 * Imports an animation in the form _01.png, _02.png, or _01.jpg, _02.jpg,
	 * etc.
	 *
	 * @param assetPath
	 *            the root asset path
	 * @return the asset
	 */
	private AssetDescriptor importImagesAnimation(String assetPath) {

		String fileExtension = getFileExtension(assetPath);
		if (fileExtension == null)
			return null;

		FramesAnimation frames = new FramesAnimation();
		int frame = 1;
		int frameTime = 500;
		String oldPath = assetPath + "_0" + frame++ + fileExtension;
		while (fileExists(oldPath)) {
			String newPath = getString(oldPath);
			frames.addFrame(new Frame(newPath, frameTime));
			oldPath = assetPath + "_0" + frame++ + fileExtension;
		}
		if (frames.getFrameCount() > 0)
			return frames;
		else
			return null;
	}

	private String getFileExtension(String assetPath) {
		String prefix = "_01";
		if (fileExists(assetPath + prefix + ".png")) {
			return ".png";
		} else if (fileExists(assetPath + prefix + ".jpg")) {
			return ".jpg";
		} else if (fileExists(assetPath + prefix + ".PNG")) {
			return ".PNG";
		} else if (fileExists(assetPath + prefix + ".JPG")) {
			return ".JPG";
		} else
			return null;
	}

	@Override
	public String getNewProjecFolder() {
		return newAdventurePath;
	}

	public boolean fileExists(String oldString) {
		boolean exists = false;
		InputStream is = inputStreamCreator.buildInputStream(oldString);
		if (is != null) {
			exists = true;
			try {
				is.close();
			} catch (IOException e) {

			}
		}
		return exists;

	}

	public BufferedImage loadImage(String oldUri) {
		try {
			return ImageIO.read(inputStreamCreator.buildInputStream(oldUri));
		} catch (Exception e) {
			logger.error("Error loading {}", oldUri);
		}
		return null;
	}

	@Override
	public Dimension getDimensionsForOldImage(String oldUri) {
		if (oldUri != null) {
			BufferedImage image = loadImage(oldUri);
			if (image != null) {
				Dimension d = new Dimension(image.getWidth(), image.getHeight());
				image.flush();
				return d;
			}
		}
		return new Dimension(100, 100);
	}

	@Override
	public Dimension getDimensionsForNewImage(String newString) {
		BufferedImage image = this.getNewImage(newString);
		if (image != null) {
			return new Dimension(image.getWidth(), image.getHeight());
		} else {
			return new Dimension(100, 100);
		}
	}

	@Override
	public BufferedImage getOldImage(String oldUri) {
		return loadImage(oldUri);
	}

	@Override
	public BufferedImage getNewImage(String newString) {
		File toResourceFile = new File(newAdventurePath, newString.substring(1));
		FileInputStream inputStream = null;
		BufferedImage image = null;
		try {
			inputStream = new FileInputStream(toResourceFile);
			image = ImageIO.read(inputStream);
		} catch (Exception e) {
			logger.error("Error loading {}", newString);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("Error closing input stream from {}",
							newString);
				}
			}
		}
		return image;
	}

	public InputStreamCreator getInputStreamCreator() {
		return this.inputStreamCreator;
	}
}
