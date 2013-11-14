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

package es.eucm.ead.importer.testers;

import es.eucm.ead.model.Commands;
import es.eucm.ead.model.elements.effects.ChangeSceneEf;
import es.eucm.ead.model.elements.effects.CommandEf;
import es.eucm.ead.model.elements.effects.Effect;
import es.eucm.ead.model.elements.scenes.SceneElement;
import es.eucm.eadventure.common.data.chapter.Exit;
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.point.TPoint;

import java.util.ArrayList;
import java.util.List;

public class ExitTester {

	private ConverterTester tester;

	public ExitTester(ConverterTester tester) {
		this.tester = tester;
	}

	public void test(String currentScene, Exit e, SceneElement exit,
			List<Effect> effects, ChangeSceneEf nextScene,
			List<Effect> postEffects, List<Effect> notEffects) {
		// TEST NEXT SCENE
		tester.command(Commands.LOG, "Update current scene");
		tester.command(Commands.GO_SCENE, currentScene);
		tester.check(Commands.SCENE, currentScene);
		// Click in the scene
		int x;
		int y;
		if (e.isRectangular()) {
			x = e.getX() + e.getWidth() / 2;
			y = e.getY() + e.getHeight() / 2;
		} else {
			List<PolygonPoint> points = new ArrayList<PolygonPoint>();
			for (java.awt.Point p : e.getPoints()) {
				points.add(new PolygonPoint(p.x, p.y));
			}
			Polygon polygon = new Polygon(points);
			Poly2Tri.triangulate(polygon);
			TPoint c = polygon.getTriangles().get(0).centroid();
			x = (int) c.getX();
			y = (int) c.getY();
		}

		// seconds to milliseconds
		int transitionTime = e.getTransitionTime();
		// Test effects and post effects
		// Enable exit
		tester.setCondition(e.getConditions(), true);

		// Effects
		if (effects.size() > 0) {
			tester.command(Commands.LOG, "Testing effects");
			waitEffects(effects);
			if (postEffects.size() > 0) {
				// Post effects
				tester.command(Commands.LOG, "Testing post effects");
				waitEffects(postEffects);
				postEffects.get(postEffects.size() - 1).addNextEffect(
						new CommandEf(Commands.NOTIFY));
			} else {
				nextScene.addSimultaneousEffect(new CommandEf(Commands.NOTIFY));
			}
			tester.check(Commands.CLICK + " " + x + " " + y, exit.getId());
			tester.check(transitionTime, Commands.SCENE, e.getNextSceneId());
			tester.command(Commands.WAIT);
			tester.check(Commands.EFFECTS, "[]");
		}

		// Test no effect
		if (notEffects != null && notEffects.size() > 0) {
			tester.command(Commands.LOG, "Testing not effects");
			tester.command(Commands.GO_SCENE, currentScene);
			tester.setCondition(e.getConditions(), false);
			waitEffects(notEffects);
			tester.check(Commands.CLICK + " " + x + " " + y, exit.getId());
			tester.command(Commands.WAIT);
			tester.check(Commands.EFFECTS, "[]");
			notEffects.get(notEffects.size() - 1).addNextEffect(
					new CommandEf(Commands.NOTIFY));
		}

	}

	private void waitEffects(List<Effect> effects) {
		String[] effectsIds = new String[effects.size()];
		int i = 0;
		for (Effect ef : effects) {
			effectsIds[i++] = ef.getId();
		}
		tester.command(Commands.WAIT_EFFECTS, effectsIds);
	}
}
