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

package ead.common.resources.assets.drawable.basics;

import ead.common.interfaces.Param;
import ead.common.resources.assets.drawable.basics.BasicDrawable;
import ead.common.resources.assets.drawable.basics.SpriteImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpriteImageImpl implements BasicDrawable {

	private static final Logger logger = LoggerFactory.getLogger("SpriteImageImpl");

	@Param("totalSprites")
	private Integer totalSprites;

	@Param("sprite")
	private Integer sprite;

	@Param("image")
	private ImageImpl image;

	public SpriteImageImpl(){

	}



	public SpriteImageImpl(ImageImpl image, int totalSprites, int sprite) {
		this.image = image;
		this.totalSprites = totalSprites;
		this.sprite = sprite;
		if (sprite >= totalSprites) {
			logger.error("Sprite number {} is invalid for total of {} sprites",
                    sprite, totalSprites);
        }
	}


	/**
	 * Returns the total number of sprites in the image return by
	 * {@link SpriteImage#getImage()}
	 *
	 * @return
	 */
	public Integer getTotalSprites() {
		return totalSprites;
	}

	/**
	 * Returns the number of sprite represented by this sprite
	 *
	 * @return
	 */
	public Integer getSprite() {
		return sprite;
	}

	public ImageImpl getImage(){
		return image;
	}

	public int hashCode( ){
		return super.hashCode() + sprite + totalSprites;
	}

	public void setTotalSprites(Integer totalSprites) {
		this.totalSprites = totalSprites;
	}

	public void setSprite(Integer sprite) {
		this.sprite = sprite;
	}


}
