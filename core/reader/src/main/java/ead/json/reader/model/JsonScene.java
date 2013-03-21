package ead.json.reader.model;

import java.util.Collection;

public class JsonScene extends JsonIdentified {

	public String background;
	public String music;
	public Collection<JsonSceneElement> sceneElements;

	public static class JsonSceneElement extends JsonIdentified {

		public JsonSceneElement() {
			x = y = disp_x = disp_y = rotation = 0.0f;
			alpha = scale = scale_x = scale_y = 1.0f;
			visible = enable = true;
		}

		public String appearance;
		public float x, y, disp_x, disp_y, alpha, rotation, scale, scale_x,
				scale_y;
		public boolean visible, enable;
	}

}
