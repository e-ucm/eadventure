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

package ead.engine.android.core.platform.assets.specialassetrenderers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.google.inject.Inject;

import ead.common.resources.assets.multimedia.EAdVideo;
import ead.engine.android.core.platform.AndroidAssetHandler;
import ead.engine.core.platform.assets.AssetHandler;
import ead.engine.core.platform.assets.SpecialAssetRenderer;

public class RockPlayerAndroidVideoRenderer implements SpecialAssetRenderer<EAdVideo, Object> {
	
	private AndroidAssetHandler assetHandler;
	
	private Intent i;
	
	public static boolean finished;
	
	@Inject
	public RockPlayerAndroidVideoRenderer(AssetHandler assetHandler) {
		this.assetHandler = (AndroidAssetHandler) assetHandler;
		finished = false;
	}

	@Override
	public Object getComponent(EAdVideo asset) {
		
		if (assetHandler != null){
			
			String destAddr = assetHandler.getAbsolutePath(asset.getUri().getPath());
			String pack="com.redirectin.rockplayer.android.unified.lite";
			Uri uri = Uri.parse(destAddr.toString());
			i = new Intent("android.intent.action.VIEW");		 
			i.setPackage(pack);
			i.setDataAndType(uri, "video/*");
			return i;
		}
		 
		return null;
	}

	@Override
	public boolean isFinished() {
		
		return finished;
	}

	@Override
	public boolean start() {
		finished = false;		
		((Activity)assetHandler.getContext()).startActivityForResult(i, 1);
		return true;
	}

	@Override
	public void reset() {
		finished = false;
		
	}

}
