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

package es.eucm.eadventure.engine.core.platform.assets.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;

import es.eucm.eadventure.common.resources.assets.drawable.Drawable;
import es.eucm.eadventure.common.resources.assets.drawable.Image;
import es.eucm.eadventure.common.resources.assets.drawable.animation.impl.FramesAnimation;
import es.eucm.eadventure.engine.core.GameLoop;
import es.eucm.eadventure.engine.core.GameState;
import es.eucm.eadventure.engine.core.platform.AssetHandler;
import es.eucm.eadventure.engine.core.platform.DrawableAsset;
import es.eucm.eadventure.engine.core.platform.RuntimeAsset;

/**
 * Represents a runtime frames animation, based on a {@link FramesAnimation}
 * 
 * 
 */
public class RuntimeFramesAnimation implements DrawableAsset<FramesAnimation> {
	
	/**
	 * Logger
	 */
	private static final Logger logger = Logger
			.getLogger("RuntimeFramesAnimation");

	/**
	 * Asset handler
	 */
	private AssetHandler assetHandler;

	/**
	 * The list of images that make the animation
	 */
	private List<DrawableAsset<Image>> images;

	/**
	 * The animation frame
	 */
	private FramesAnimation framesAnimation;

	/**
	 * The elapsed time
	 */
	private int time = 0;

	/**
	 * The total time of the animation
	 */
	private int totalTime = 0;

	/**
	 * Current frame
	 */
	private DrawableAsset<Image> currentImage;

	@Inject
	public RuntimeFramesAnimation(AssetHandler assetHandler) {
		this.assetHandler = assetHandler;
		this.images = new ArrayList<DrawableAsset<Image>>();
		logger.info("New instance");
	}

	@Override
	public boolean loadAsset() {
		if (!isLoaded()) {
			for (int i = 0; i < framesAnimation.getFrameCount(); i++) {
				DrawableAsset<Image> image = (DrawableAsset<Image>) assetHandler.getRuntimeAsset((Image) framesAnimation.getFrame(i));
				images.add(image);
				totalTime += framesAnimation.getFrame(i).getTime();
			}
			if (framesAnimation.getFrameCount() > 0)
				currentImage = images.get(0);
			else
				logger.log(Level.WARNING, "Animation has no frames!!");
			return true;
		}
		return true;
	}

	@Override
	public void freeMemory() {
		for (RuntimeAsset<Image> a : images) {
			a.freeMemory();
		}
	}

	@Override
	public boolean isLoaded() {
		return images.size() == framesAnimation.getFrameCount();
	}

	@Override
	public void setDescriptor(FramesAnimation descriptor) {
		this.framesAnimation = descriptor;
	}

	@Override
	public void update(GameState state) {
		time += GameLoop.SKIP_MILLIS_TICK;
	}

	public int getHeight() {
		if (currentImage != null)
			return currentImage.getHeight();
		else
			return 1;
	}

	public int getWidth() {
		if (currentImage != null)
			return currentImage.getWidth();
		else
			return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends Drawable> DrawableAsset<S> getDrawable() {
		if (!isLoaded()) {
			loadAsset();
		}
		while (time >= totalTime)
			time -= totalTime;

		int accumulated = time;
		int i = 0;

		while (accumulated > framesAnimation.getFrame(i).getTime()) {
			accumulated -= framesAnimation.getFrame(i).getTime();
			i++;
		}

		currentImage = images.get(i);
		return (DrawableAsset<S>) currentImage;

	}

	@Override
	public FramesAnimation getAssetDescriptor() {
		return framesAnimation;
	}
}
