package ead.json.reader.model;

import java.util.Collection;
import java.util.Map;

public class JsonAsset extends JsonIdentified {

	public String inherits;

	public String type;

	// Font, images
	public String uri;

	// Captions
	public String fontRef;
	public String string;

	// Composed drawable
	public Collection<AssetReference> assets;

	public static class AssetReference {
		public String id;
		public float x;
		public float y;
	}

	public void inherite(Map<String, JsonAsset> assets, String parentId) {
		if (parentId == null) {
			return;
		}
		JsonAsset parentAsset = assets.get(parentId);
		if (parentAsset.inherits != null) {
			this.inherite(assets, parentAsset.inherits);
		}

		this.type = this.type == null ? parentAsset.type : this.type;
		this.uri = this.uri == null ? parentAsset.uri : this.uri;
		this.fontRef = this.fontRef == null ? parentAsset.fontRef
				: this.fontRef;
		this.string = this.string == null ? parentAsset.string : this.string;
		this.assets = this.assets == null ? parentAsset.assets : this.assets;

	}

}
