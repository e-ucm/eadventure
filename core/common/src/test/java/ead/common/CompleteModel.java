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

package ead.common;

import ead.common.model.elements.BasicAdventureModel;
import ead.common.model.elements.BasicChapter;
import ead.common.model.elements.scenes.BasicScene;
import ead.common.model.elements.variables.operations.BooleanOp;
import ead.common.params.fills.ColorFill;
import ead.common.params.fills.LinearGradientFill;
import ead.common.params.fills.Paint;
import ead.common.params.paint.EAdPaint;
import ead.common.params.text.EAdString;
import ead.common.resources.assets.drawable.EAdDrawable;
import ead.common.resources.assets.drawable.basics.Caption;
import ead.common.resources.assets.drawable.basics.Image;
import ead.common.resources.assets.drawable.basics.enums.Alignment;
import ead.common.resources.assets.drawable.basics.shapes.BallonShape;
import ead.common.resources.assets.drawable.basics.shapes.CircleShape;
import ead.common.resources.assets.drawable.basics.shapes.RectangleShape;
import ead.common.resources.assets.drawable.basics.shapes.extra.BalloonType;
import ead.common.resources.assets.drawable.compounds.ComposedDrawable;
import ead.common.resources.assets.drawable.compounds.StateDrawable;
import ead.common.resources.assets.drawable.filters.FilteredDrawable;
import ead.common.resources.assets.drawable.filters.MatrixFilter;
import ead.elementfactories.demos.scenes.InitScene;

/**
 * A complete model containing all possibles eAdventure elements. 
 * 
 */
public class CompleteModel extends BasicAdventureModel {
	
	public CompleteModel( ){
		// FIXME more elements
		BasicChapter chapter = new BasicChapter( );
		chapter.setTitle(EAdString.newEAdString("A chapter"));
		this.getChapters().add(chapter);
		RectangleShape background = new RectangleShape( 800, 600, ColorFill.BLACK );
		StateDrawable drawable = new StateDrawable();
		drawable.addDrawable("state1", background);
		drawable.addDrawable("state2", getDrawableWithAllBasicDrawables());
		BasicScene scene = new BasicScene( drawable );
		chapter.getScenes().add(scene);
		chapter.getScenes().add(new InitScene());
	}
	
	public EAdDrawable getDrawableWithAllBasicDrawables( ){
		ComposedDrawable composed = new ComposedDrawable( );
		LinearGradientFill gradient = new LinearGradientFill( ColorFill.BROWN, new ColorFill(500, 10, 1 ), -1, 2, 400.2f, 2.0f );
		BallonShape ballonShape = new BallonShape( 0, 10, 900, 12, BalloonType.ELECTRIC );
		ballonShape.setPaintAsVector(true);
		ballonShape.setPaint(gradient);
		
		EAdPaint p = new Paint( gradient, ColorFill.BROWN );
		CircleShape circleShape = new CircleShape( 0, 10, 900, 35, p );
		
		Image i = new Image("@drawable/someimage.png");
		
		Caption c = new Caption( );
		c.setAlignment(Alignment.LEFT);
		c.getFields().add(BooleanOp.TRUE_OP);
		c.setPadding(50);
		c.setTextPaint(gradient);
		c.setPreferredHeight(20);
		c.setBubblePaint(p);
		
		
		
		FilteredDrawable filtered = new FilteredDrawable( );
		filtered.setDrawable(c);
		filtered.setFilter(new MatrixFilter( ));
		
		composed.addDrawable(ballonShape, 20, 50);
		composed.addDrawable(circleShape);
		composed.addDrawable(i);
		composed.addDrawable(c, 1000, 100000);
		composed.addDrawable(filtered);
	
		return composed;
		
	}

}
