package ead.converter.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.assets.AssetDescriptor;
import ead.common.model.assets.drawable.EAdDrawable;
import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.assets.drawable.basics.animation.Frame;
import ead.common.model.assets.drawable.basics.animation.FramesAnimation;
import ead.converter.OldReader;
import es.eucm.eadventure.common.data.animation.Animation;

@Singleton
public class ResourceConverter {

	private static final Logger logger = LoggerFactory
			.getLogger("ResourceConverter");

	public static final String DRAWABLE = "drawable";

	public static final String BINARY = "binary";

	private String destinyPath;

	private OldReader oldReader;

	private Map<String, String> urisCorrespondences;

	/**
	 * A map matching old uris with the new assets
	 */
	private Map<String, AssetDescriptor> assets;

	@Inject
	public ResourceConverter(OldReader oldReader) {
		this.oldReader = oldReader;
		urisCorrespondences = new LinkedHashMap<String, String>();
		assets = new LinkedHashMap<String, AssetDescriptor>();
	}

	public void setPath(String destinyPath) {
		this.destinyPath = destinyPath;
		this.urisCorrespondences.clear();
		createFolders();
	}

	private void createFolders() {
		File f = new File(destinyPath + "/" + DRAWABLE);
		f.mkdirs();
		f = new File(destinyPath + "/" + BINARY);
		f.mkdirs();

	}

	/**
	 * Returns a image in the new model with a path from the old model
	 * 
	 * @param imagePath
	 * @return
	 */
	public EAdDrawable getImage(String imagePath) {
		AssetDescriptor asset = assets.get(imagePath);
		if (asset == null) {
			String destinyImagePath = getPath(imagePath);
			asset = new Image(destinyImagePath);
			assets.put(imagePath, asset);
		}
		return (EAdDrawable) asset;
	}

	/**
	 * Returns the path on the new model for a given path in the old model
	 * 
	 * @param oldPath
	 * @return
	 */
	public String getPath(String oldPath) {
		String newString = urisCorrespondences.get(oldPath);
		if (newString == null) {

			String folder = getFolder(oldPath);

			String fileName = oldPath.replace("/", "_");
			newString = folder + "/" + fileName;

			if (!copyFile(oldPath, newString)) {
				logger.error("Missing resource: {}", oldPath);
				return null;
			}

			newString = "@" + newString;
			urisCorrespondences.put(oldPath, newString);
		}
		return newString;
	}

	private String getFolder(String oldString) {
		if (oldString.endsWith(".png") || oldString.endsWith(".jpg"))
			return DRAWABLE;
		else
			return BINARY;
	}

	/**
	 * Copy one file from old path to the new path
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public boolean copyFile(String oldPath, String newPath) {

		File toResourceFile = new File(destinyPath, newPath);
		InputStream in = null;
		OutputStream out = null;
		boolean success = false;

		try {
			in = oldReader.getInputStream(oldPath);
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
			logger.error("Error copying '{}' to '{}'", oldPath, newPath);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("Error accesing '{}'", oldPath);
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("Error accesing '{}'", newPath);
				}
			}
		}
		return success;

	}

	public Animation getAnimation(String file) {
		return oldReader.getAnimation(file);
	}

	/**
	 * Returns a frames animation for the given file
	 * 
	 * @param assetPath
	 *            it should end in *.eaa, *_01.png, *_01.jpg...
	 * @return
	 */
	public EAdDrawable getFramesAnimation(String assetPath) {
		if (assetPath == null)
			return null;

		if (assets.containsKey(assetPath)) {
			return (EAdDrawable) assets.get(assetPath);
		}

		AssetDescriptor asset = null;

		// Special case
		if (assetPath.startsWith("assets/special/EmptyAnimation")) {
			if (assetPath.endsWith("_01.png")) {
				String uri = this.getPath(assetPath);
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
				Animation a = oldReader.getAnimation(assetPath);
				// XXX animation transitions
				FramesAnimation frames = new FramesAnimation();
				for (es.eucm.eadventure.common.data.animation.Frame f : a
						.getFrames()) {
					// XXX Frame sounds not imported
					long time = f.getTime();
					String oldString = f.getUri();
					String newString = getPath(oldString);
					Frame frame = new Frame(newString, (int) time);
					frames.addFrame(frame);
					asset = frames;
				}
			} else {
				asset = importImagesAnimation(assetPath);
			}
		}

		if (asset != null)
			assets.put(assetPath, asset);
		return (EAdDrawable) asset;
	}

	public boolean fileExists(String oldString) {
		boolean exists = false;
		InputStream is = oldReader.getInputStream(oldString);
		if (is != null) {
			exists = true;
			try {
				is.close();
			} catch (IOException e) {

			}
		}
		return exists;

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
			String newPath = getPath(oldPath);
			frames.addFrame(new Frame(newPath, frameTime));
			oldPath = assetPath + "_0" + frame++ + fileExtension;
		}
		if (frames.getFrameCount() > 0)
			return frames;
		else
			return null;
	}

}