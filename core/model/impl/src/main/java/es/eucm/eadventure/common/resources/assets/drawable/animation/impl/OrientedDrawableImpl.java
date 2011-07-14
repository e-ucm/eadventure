package es.eucm.eadventure.common.resources.assets.drawable.animation.impl;

import es.eucm.eadventure.common.interfaces.Oriented.Orientation;
import es.eucm.eadventure.common.model.EAdMap;
import es.eucm.eadventure.common.model.impl.EAdMapImpl;
import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.OrientedDrawable;

/**
 * Default implementation for {@link OrientedDrawable}
 * 
 * 
 */
public class OrientedDrawableImpl implements OrientedDrawable {

	private EAdMap<Orientation, Drawable> drawables;

	/**
	 * Constructs an empty oriented asset
	 */
	public OrientedDrawableImpl() {
		drawables = new EAdMapImpl<Orientation, Drawable>("orientedDrawable",
				Orientation.class, Drawable.class);
	}

	/**
	 * Links the given orientation with the given drawable
	 * 
	 * @param orientation
	 *            the orientation
	 * @param drawable
	 *            the drawable
	 */
	public void setDrawable(Orientation orientation, Drawable drawable) {
		drawables.put(orientation, drawable);
	}

	@Override
	public Drawable getDrawable(Orientation orientation) {
		return drawables.get(orientation);
	}

}
