package es.eucm.eadventure.common.resources.assets.drawable.basics.impl.shapes.extra;

import com.gwtent.reflection.client.Reflectable;

@Reflectable
public enum BalloonType {
		RECTANGLE, ROUNDED_RECTANGLE, CLOUD, ELECTRIC;

		public BalloonStroke getStroke() {
			switch (this) {
			case RECTANGLE:
				return new RectangleStroke();
			case ROUNDED_RECTANGLE:
				return new RoundedRectangleStroke();
			case CLOUD:
				return new CloudStroke();
			case ELECTRIC:
				return new ElectricStroke();
			}
			return null;
		}
		
	}