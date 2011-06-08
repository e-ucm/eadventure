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

import es.eucm.eadventure.common.model.effects.impl.EAdCancelEffect;
import es.eucm.eadventure.common.model.effects.impl.EAdWaitEffect;
import es.eucm.eadventure.engine.core.impl.GameLoopImpl;

public class CancelEffectTest extends EffectTest {

	@Test
	public void testCancelSomeEffects() {
		EAdWaitEffect waits[] = new EAdWaitEffect[10];
		for (int i = 0; i < 10; i++) {
			waits[i] = new EAdWaitEffect( "w" + i,
					GameLoopImpl.SKIP_MILLIS_TICK * 50 );
		}
		EAdCancelEffect cancel = new EAdCancelEffect( "");
		cancel.addEffect(waits[0]);
		cancel.addEffect(waits[1]);
		cancel.addEffect(waits[5]);
		cancel.addEffect(waits[8]);

		waits[0].setTime( 10 );
		testEngine.addEffect(waits[0]);
		testEngine.addEffect(cancel);
		for (int i = 1; i < 10; i++) {
			waits[i].setBlocking(false);
			testEngine.addEffect(waits[i]);
		}
		assertEquals(testEngine.getEffects().size(), 11);
		testEngine.update();
		assertEquals(testEngine.getEffects().size(), 6);
		assertEquals( testEngine.getEffects().contains(testEngine.gameObjectFactory.get( waits[1]) ), false );
		assertEquals( testEngine.getEffects().contains(testEngine.gameObjectFactory.get( waits[5])), false );
		assertEquals( testEngine.getEffects().contains(testEngine.gameObjectFactory.get( waits[8])), false );
		assertEquals( testEngine.getEffects().contains(testEngine.gameObjectFactory.get(waits[2])), true );
		assertEquals( testEngine.getEffects().contains(testEngine.gameObjectFactory.get(waits[3])), true );
		assertEquals( testEngine.getEffects().contains(testEngine.gameObjectFactory.get(waits[4])), true );
		assertEquals( testEngine.getEffects().contains(testEngine.gameObjectFactory.get(waits[6])), true );
		assertEquals( testEngine.getEffects().contains(testEngine.gameObjectFactory.get(waits[7])), true );
		assertEquals( testEngine.getEffects().contains(testEngine.gameObjectFactory.get(waits[9])), true );
		testEngine.getEffects().clear();		
	}
	
	@Test
	public void testCancelAllEffects(){
		EAdCancelEffect cancel = new EAdCancelEffect("");
		testEngine.addEffect(cancel);
		EAdWaitEffect waits[] = new EAdWaitEffect[10];
		for (int i = 0; i < 10; i++) {
			waits[i] = new EAdWaitEffect("w" + i,
					GameLoopImpl.SKIP_MILLIS_TICK * 50 );
			testEngine.addEffect(waits[i]);
		}
		assertEquals( testEngine.getEffects().size(), 11 );
		testEngine.update();
		assertEquals( testEngine.getEffects().size(), 0 );
		
	}
}
