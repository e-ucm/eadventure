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

package es.eucm.eadventure.engine.core.test.effects;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement;
import es.eucm.eadventure.common.model.effects.impl.sceneelements.EAdMoveSceneElement.MovementSpeed;
import es.eucm.eadventure.common.model.params.EAdPosition;
import es.eucm.eadventure.common.model.variables.impl.operations.LiteralExpressionOperation;

public class MoveActorReferenceTest extends EffectTest {

	@Test
	public void testMoveActor() {
		EAdMoveSceneElement move = new EAdMoveSceneElement("id");
		move.setSceneElement(testEngine.reference1);
		move.setTargetCoordiantes(new LiteralExpressionOperation("id", "10"), new LiteralExpressionOperation("id", "10"));
		move.setSpeed(MovementSpeed.FAST);
		testEngine.addEffect(move);
		assertEquals(testEngine.gameObjectFactory.get(testEngine.reference1)
				.getPosition(), testEngine.reference1.getPosition());
		testEngine.update();
		testEngine.update();
		testEngine.update();
		testEngine.update();
		assertEquals(testEngine.gameObjectFactory.get(testEngine.reference1)
				.getPosition(), new EAdPosition( 10, 10 ));
		
	}

}
