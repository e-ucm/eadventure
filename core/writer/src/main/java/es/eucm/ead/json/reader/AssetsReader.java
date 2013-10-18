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

package es.eucm.ead.json.reader;

import com.google.gson.internal.StringMap;
import es.eucm.ead.model.assets.AssetDescriptor;
import es.eucm.ead.model.assets.drawable.EAdDrawable;
import es.eucm.ead.model.assets.drawable.basics.Caption;
import es.eucm.ead.model.assets.drawable.basics.EAdBasicDrawable;
import es.eucm.ead.model.assets.drawable.basics.Image;
import es.eucm.ead.model.assets.drawable.basics.animation.Frame;
import es.eucm.ead.model.assets.drawable.basics.animation.FramesAnimation;
import es.eucm.ead.model.assets.drawable.basics.enums.Alignment;
import es.eucm.ead.model.assets.drawable.basics.shapes.RectangleShape;
import es.eucm.ead.model.assets.drawable.compounds.ComposedDrawable;
import es.eucm.ead.model.assets.drawable.compounds.StateDrawable;
import es.eucm.ead.model.assets.drawable.filters.FilteredDrawable;
import es.eucm.ead.model.assets.drawable.filters.MatrixFilter;
import es.eucm.ead.model.assets.multimedia.Sound;
import es.eucm.ead.model.assets.text.BasicFont;
import es.eucm.ead.model.assets.text.EAdFont;
import es.eucm.ead.model.params.paint.EAdPaint;
import es.eucm.ead.model.params.util.Matrix;
import es.eucm.ead.reader.ObjectsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

@SuppressWarnings("unchecked")
public class AssetsReader {

	static private Logger logger = LoggerFactory.getLogger(AssetsReader.class);

	private ObjectsFactory objectsFactory;

	private TemplateReader templateReader;

	private OperationReader operationReader;

	private String resourcesRoot = "src/main/resources/";

	public AssetsReader(ObjectsFactory objectsFactory,
			TemplateReader templateReader, OperationReader operationReader) {
		this.objectsFactory = objectsFactory;
		this.templateReader = templateReader;
		this.operationReader = operationReader;
	}

	public void setResourcesRoot(String resourcesRoot) {
		this.resourcesRoot = resourcesRoot;
	}

	public boolean parseAssets(Collection<StringMap<Object>> assets) {

		for (StringMap<Object> a : assets) {
			try {
				templateReader.applyTemplates(a);
				AssetDescriptor asset = null;
				String type = a.get("type").toString();
				if (type.equals("font")) {
					asset = parseFont(a);
				} else if (type.equals("caption")) {
					asset = parseCaption(a);
				} else if (type.equals("img")) {
					asset = parseImage(a);
				} else if (type.equals("composed")) {
					asset = parseComposed(a);
				} else if (type.equals("sound")) {
					asset = parseSound(a);
				} else if (type.equals("rectangle")) {
					asset = parseRectangle(a);
				} else if (type.equals("frames")) {
					asset = parseFrames(a);
				} else if (type.equals("filter")) {
					asset = parseFilter(a);
				} else if (type.equals("states")) {
					asset = parseStates(a);
				} else if (type.equals("images")) {
					addImages(a);
					continue;
				} else if (type.equals("img_frames")) {
					asset = parseImageFrames(a);
				}

				if (asset == null) {
					logger.error("Null asset for {}", a);
				}
				String assetId = (String) a.get("id");
				if (assetId != null)
					asset.setId(assetId);
				objectsFactory.putIdentified(asset);
			} catch (Exception e) {
				logger.error("Error parsing asset {} ", a, e);
				return false;
			}
		}
		return true;
	}

	private AssetDescriptor parseStates(StringMap<Object> a) {
		StateDrawable states = new StateDrawable();
		StringMap<Object> s = (StringMap<Object>) a.get("states");
		for (Entry<String, Object> e : s.entrySet()) {
			states.addDrawable(e.getKey(), (EAdDrawable) objectsFactory
					.getObjectById(e.getValue().toString()));
		}
		return states;
	}

	private AssetDescriptor parseImageFrames(StringMap<Object> a) {
		FramesAnimation frames = new FramesAnimation();
		int frameTime = ((Number) a.get("frameTime")).intValue();
		String frameName = (String) a.get("frameName");
		int seqStart = ((Number) a.get("seqStart")).intValue();
		int seqEnd = ((Number) a.get("seqEnd")).intValue();

		int index1 = frameName.indexOf('!');
		int index2 = frameName.indexOf('!', index1 + 1);
		int diff = index2 - index1 - 1;
		String seq = "!" + frameName.substring(index1 + 1, index2) + "!";

		for (int i = seqStart; i <= seqEnd; i++) {
			String index = i + "";
			for (int j = index.length(); j < diff; j++) {
				index = "0" + index;
			}
			String name = frameName.replace(seq, index);
			frames.addFrame(new Frame(new Image(name), frameTime));
		}
		return frames;
	}

	private AssetDescriptor parseFilter(StringMap<Object> a) {
		FilteredDrawable filteredDrawable = new FilteredDrawable();
		EAdDrawable drawable = (EAdDrawable) objectsFactory
				.getObjectById((String) a.get("drawable"));
		filteredDrawable.setDrawable(drawable);
		String filterType = (String) a.get("filter");
		if (filterType.equals("matrix")) {
			Number originX = (Number) a.get("originX");
			Number originY = (Number) a.get("originY");
			Collection<Number> matrix = (Collection<Number>) a.get("matrix");
			Matrix m = new Matrix();
			float values[] = new float[matrix.size()];
			int i = 0;
			for (Number n : matrix) {
				values[i] = n.floatValue();
				i++;
			}
			m.setValues(values);
			filteredDrawable.setFilter(new MatrixFilter(m,
					originX.floatValue(), originY.floatValue()));
		}
		return filteredDrawable;
	}

	private void addImages(StringMap<Object> a) {
		Collection<String> roots = (Collection<String>) a.get("roots");
		Boolean recursive = (Boolean) a.get("recursive");
		for (String root : roots) {
			root = resourcesRoot + root.substring(1);
			addImages(new File(root), recursive != null
					&& recursive.booleanValue());
		}
	}

	private void addImages(File folder, boolean recursive) {
		if (folder.exists() && folder.isDirectory()) {
			for (File f : folder.listFiles()) {
				if (f.isDirectory() && recursive) {
					addImages(f, true);
				} else if (f.getName().endsWith(".png")
						|| f.getName().endsWith(".jpg")) {
					String uri = "@"
							+ folder.getPath()
									.substring(resourcesRoot.length()) + "/"
							+ f.getName();
					Image i = new Image(uri);
					i.setId("img_"
							+ f.getName()
									.substring(0, f.getName().length() - 4));
					objectsFactory.putIdentified(i);
				}
			}
		}
	}

	private AssetDescriptor parseFrames(StringMap<Object> a) {
		Collection<String> frames = (Collection<String>) a.get("frames");
		Collection<Number> times = (Collection<Number>) a.get("times");
		FramesAnimation animation = new FramesAnimation();
		Iterator<Number> it = times.iterator();
		for (String f : frames) {
			animation.addFrame(new Frame((EAdBasicDrawable) objectsFactory
					.getObjectById(f), it.next().intValue()));
		}
		return animation;
	}

	private AssetDescriptor parseRectangle(StringMap<Object> a) {
		Number width = (Number) a.get("width");
		Number height = (Number) a.get("height");
		String p = a.get("paint").toString();
		EAdPaint paint = objectsFactory.getPaint(p);

		return new RectangleShape(width.intValue(), height.intValue(), paint);
	}

	private AssetDescriptor parseSound(StringMap<Object> a) {
		return new Sound(a.get("uri").toString());
	}

	private AssetDescriptor parseComposed(StringMap<Object> a) {
		ComposedDrawable composed = new ComposedDrawable();
		return composed;
	}

	private AssetDescriptor parseImage(StringMap<Object> a) {
		return new Image(a.get("uri").toString());
	}

	private AssetDescriptor parseCaption(StringMap<Object> a) {
		Caption c = new Caption(a.get("string").toString());
		EAdFont font = null;
		if (a.get("fontRef") != null) {
			font = (EAdFont) objectsFactory.getObjectById(a.get("fontRef")
					.toString());
		}
		String p = (String) a.get("textPaint");
		if (p != null) {
			c.setTextPaint(objectsFactory.getPaint(p));
		}

		String b = (String) a.get("bubblePaint");
		if (b != null) {
			c.setBubblePaint(objectsFactory.getPaint(b));
		}

		Number pad = (Number) a.get("padding");
		if (pad != null) {
			c.setPadding(pad.intValue());
		}

		Number prefWidth = (Number) a.get("prefWidth");
		if (prefWidth != null) {
			c.setPreferredWidth(prefWidth.intValue());
		}

		Number prefHeight = (Number) a.get("prefHeight");
		if (prefHeight != null) {
			c.setPreferredWidth(prefHeight.intValue());
		}

		String align = (String) a.get("align");
		if (align != null) {
			if (align.equals("left")) {
				c.setAlignment(Alignment.LEFT);
			} else if (align.equals("center")) {
				c.setAlignment(Alignment.CENTER);
			} else if (align.equals("right")) {
				c.setAlignment(Alignment.RIGHT);
			}
		}

		Collection<StringMap<Object>> operations = (Collection<StringMap<Object>>) a
				.get("operations");
		if (operations != null)
			for (StringMap<Object> op : operations) {
				c.getOperations().add(operationReader.read(op));
			}

		c.setFont(font);
		return c;
	}

	private AssetDescriptor parseFont(StringMap<Object> a) {
		return new BasicFont(a.get("uri").toString());
	}

}
