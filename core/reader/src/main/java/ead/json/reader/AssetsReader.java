package ead.json.reader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ead.common.model.assets.AssetDescriptor;
import ead.common.model.assets.drawable.basics.Caption;
import ead.common.model.assets.drawable.basics.EAdBasicDrawable;
import ead.common.model.assets.drawable.basics.Image;
import ead.common.model.assets.drawable.compounds.ComposedDrawable;
import ead.common.model.assets.multimedia.Sound;
import ead.common.model.assets.text.BasicFont;
import ead.common.model.assets.text.EAdFont;
import ead.json.reader.model.JsonAsset;
import ead.json.reader.model.JsonAsset.AssetReference;
import ead.reader.model.ObjectsFactory;

public class AssetsReader {

	private ObjectsFactory objectsFactory;

	private Map<String, JsonAsset> currentAssets;

	public AssetsReader(ObjectsFactory objectsFactory) {
		this.objectsFactory = objectsFactory;
		this.currentAssets = new HashMap<String, JsonAsset>();
	}

	public void parseAssets(Collection<JsonAsset> assets) {
		currentAssets.clear();
		for (JsonAsset a : assets) {
			currentAssets.put(a.id, a);
		}
		updateInheritance();
		for (JsonAsset a : assets) {
			AssetDescriptor asset = null;
			if (a.type.equals("font")) {
				asset = parseFont(a);
			} else if (a.type.equals("caption")) {
				asset = parseCaption(a);
			} else if (a.type.equals("img")) {
				asset = parseImage(a);
			} else if (a.type.equals("composed")) {
				asset = parseComposed(a);
			} else if (a.type.equals("sound")) {
				asset = parseSound(a);
			}
			if (a.id != null)
				asset.setId(a.id);
			objectsFactory.putAsset(asset.getId(), asset);
		}
	}

	private AssetDescriptor parseSound(JsonAsset a) {
		return new Sound(a.uri);
	}

	private AssetDescriptor parseComposed(JsonAsset a) {
		ComposedDrawable composed = new ComposedDrawable();
		for (AssetReference ref : a.assets) {
			EAdBasicDrawable d = (EAdBasicDrawable) objectsFactory
					.getAsset(ref.id);
			composed.addDrawable(d, (int) ref.x, (int) ref.y);
		}
		return composed;
	}

	private AssetDescriptor parseImage(JsonAsset a) {
		return new Image(a.uri);
	}

	private AssetDescriptor parseCaption(JsonAsset a) {
		Caption c = new Caption(a.string);
		EAdFont font = null;
		if (a.fontRef != null) {
			font = (EAdFont) objectsFactory.getAsset(a.fontRef);
		}
		c.setFont(font);
		return c;
	}

	private AssetDescriptor parseFont(JsonAsset a) {
		return new BasicFont(a.uri);
	}

	public void updateInheritance() {
		for (JsonAsset e : currentAssets.values()) {
			e.inherite(currentAssets, e.inherits);
		}
	}

}
