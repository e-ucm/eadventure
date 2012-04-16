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

package ead.common.importer.resources;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.EAdElementImporter;
import ead.common.GenericImporter;
import ead.common.importer.interfaces.ResourceImporter;
import ead.common.interfaces.features.Evented;
import ead.common.interfaces.features.Resourced;
import ead.common.model.elements.EAdCondition;
import ead.common.model.elements.conditions.ANDCond;
import ead.common.model.elements.conditions.NOTCond;
import ead.common.model.elements.events.ConditionedEv;
import ead.common.model.elements.events.enums.ConditionedEvType;
import ead.common.model.predef.effects.ChangeAppearanceEf;
import ead.common.resources.EAdBundleId;
import ead.common.resources.assets.AssetDescriptor;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.animation.Frame;
import ead.common.resources.assets.drawable.basics.animation.FramesAnimation;
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

	private static final Logger logger = LoggerFactory
			.getLogger("ResourceImporterImpl");

	/**
	 * Conditions importer
	 */
	private EAdElementImporter<Conditions, EAdCondition> conditionsImporter;

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
	 * Stores correspondences between URIs at old adventure project and URIS at
	 * new adventure project
	 */
	private Map<String, String> urisCorrespondences;

	/**
	 * A map matching old uris with the new assets
	 */
	private Map<String, AssetDescriptor> assets;

	@Inject
	public ResourceImporterImpl(
			EAdElementImporter<Conditions, EAdCondition> conditionsImporter,
			GenericImporter<Animation, FramesAnimation> animationImporter,
			ImageLoaderFactory imageLoader,
			InputStreamCreator inputStreamCreator) {
		this.imageLoader = imageLoader;
		this.inputStreamCreator = inputStreamCreator;
		this.conditionsImporter = conditionsImporter;
		this.animationImporter = animationImporter;
		urisCorrespondences = new HashMap<String, String>();
		assets = new HashMap<String, AssetDescriptor>();

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
	public String getURI(String oldURI) {
		String newURI = urisCorrespondences.get(oldURI);
		if (newURI == null) {

			String folder = getFolder(oldURI);

			String fileName = oldURI.replace("/", "_");
			newURI = folder + "/" + fileName;

			if (!copyFile(oldURI, newURI)) {
				logger.error("Missing resource: {}", oldURI);
				return null;
			}

			newURI = "@" + newURI;
			urisCorrespondences.put(oldURI, newURI);
		}
		return newURI;
	}

	private String getFolder(String oldURI) {
		if (oldURI.endsWith(".png") || oldURI.endsWith(".jpg"))
			return DRAWABLE;
		else
			return BINARY;
	}

	@Override
	public boolean copyFile(String oldURI, String newURI) {

		File toResourceFile = new File(newAdventurePath, newURI);
		InputStream in = null;
		OutputStream out = null;
		boolean success = false;

		try {
			in = inputStreamCreator.buildInputStream(oldURI);
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
			logger.error("Error copying '{}' to '{}'", oldURI, newURI);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("Error accesing '{}'", oldURI);
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("Error accesing '{}'", newURI);
				}
			}
		}
		return success;

	}

	@Override
	public void importResources(Resourced element, List<Resources> resources,
			Map<String, String> resourcesStrings,
			Map<String, Object> resourcesObjectClasses) {
		int i = 0;
		EAdCondition previousCondition = null;

		// We iterate for the resources. Each resource is associated to some
		// conditions. These
		// conditions are transformed in ConditionedEvents.
		for (Resources r : resources) {
			EAdBundleId bundleId;
			if (i == 0) {
				bundleId = element.getInitialBundle();
			} else {
				bundleId = new EAdBundleId("bundle_" + i);
				element.getResources().addBundle(bundleId);
			}

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
					element.getResources().addAsset(bundleId, propertyName,
							asset);
				}

			}

			if (element instanceof Evented) {

				ConditionedEv conditionEvent = new ConditionedEv();
				conditionEvent
						.setId(bundleId.getBundleId() + "_condition_" + i);

				EAdCondition condition = conditionsImporter.init(r
						.getConditions());
				condition = conditionsImporter.convert(r.getConditions(),
						condition);

				if (previousCondition == null) {
					previousCondition = new NOTCond(condition);
				} else {
					EAdCondition temp = condition;
					condition = new ANDCond(condition, previousCondition);
					previousCondition = new ANDCond(previousCondition,
							new NOTCond(temp));
				}
				conditionEvent.setCondition(condition);

				ChangeAppearanceEf changeAppereance = new ChangeAppearanceEf(
						null, bundleId);
				changeAppereance.setId(conditionEvent.getId()
						+ "change_appearence");
				conditionEvent.addEffect(ConditionedEvType.CONDITIONS_MET,
						changeAppereance);

				((Evented) element).getEvents().add(conditionEvent);
			}

			i++;
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
				String uri = this.getURI(assetPath);
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
			String newAssetPath = getURI(assetPath);

			try {
				asset = (AssetDescriptor) clazz.getConstructor(String.class)
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
			String newPath = getURI(oldPath);
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

	public boolean fileExists(String oldURI) {
		boolean exists = false;
		InputStream is = inputStreamCreator.buildInputStream(oldURI);
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
	public Dimension getDimensionsForNewImage(String newURI) {
		BufferedImage image = this.getNewImage(newURI);
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
	public BufferedImage getNewImage(String newURI) {
		File toResourceFile = new File(newAdventurePath, newURI.substring(1));
		FileInputStream inputStream = null;
		BufferedImage image = null;
		try {
			inputStream = new FileInputStream(toResourceFile);
			image = ImageIO.read(inputStream);
		} catch (Exception e) {
			logger.error("Error loading {}", newURI);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("Error closing input stream from {}", newURI);
				}
			}
		}
		return image;
	}
	
	public InputStreamCreator getInputStreamCreator( ){
		return this.inputStreamCreator;
	}
}
