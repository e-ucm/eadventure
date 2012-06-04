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

package ead.engine.android.core.platform;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import ead.common.model.elements.guievents.MouseGEv;
import ead.common.model.elements.guievents.enums.MouseGEvButtonType;
import ead.common.model.elements.guievents.enums.MouseGEvType;
import ead.engine.core.input.InputHandler;
import ead.engine.core.input.actions.MouseInputAction;

public class AndroidInputListener extends SimpleOnGestureListener implements
		View.OnTouchListener {

	private InputHandler inputHandler;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_VELOCITY = 200;

	private GestureDetector gestureDetector;

	public AndroidInputListener(InputHandler inputHandler) {
		this.inputHandler = inputHandler;
		gestureDetector = new GestureDetector(this);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		inputHandler.addAction(new MouseInputAction(
				MouseGEv.MOUSE_LEFT_PRESSED, (int) e.getX(), (int) e.getY()));
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {

		inputHandler.addAction(new MouseInputAction(
				MouseGEv.MOUSE_LEFT_PRESSED, (int) e.getX(), (int) e.getY()));
		MouseInputAction action = new MouseInputAction(MouseGEvType.CLICK,
				MouseGEvButtonType.BUTTON_1, (int) e.getX(), (int) e.getY());
		inputHandler.addAction(action);

		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {

		inputHandler.addAction(new MouseInputAction(MouseGEv.MOUSE_RIGHT_CLICK,
				(int) e.getX(), (int) e.getY()));
		MouseInputAction action = new MouseInputAction(MouseGEvType.CLICK,
				MouseGEvButtonType.BUTTON_2, (int) e.getX(), (int) e.getY());
		inputHandler.addAction(action);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
				&& Math.abs(velocityX) > SWIPE_VELOCITY) {
			MouseInputAction action = new MouseInputAction(
					MouseGEvType.SWIPE_LEFT, MouseGEvButtonType.NO_BUTTON,
					(int) e2.getX(), (int) e2.getY());
			inputHandler.addAction(action);
		} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
				&& Math.abs(velocityX) > SWIPE_VELOCITY) {
			MouseInputAction action = new MouseInputAction(
					MouseGEvType.SWIPE_RIGHT, MouseGEvButtonType.NO_BUTTON,
					(int) e2.getX(), (int) e2.getY());
			inputHandler.addAction(action);
		}

		inputHandler
				.addAction(new MouseInputAction(MouseGEv.MOUSE_LEFT_RELEASED,
						(int) e2.getX(), (int) e2.getY()));
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		int x = (int) event.getRawX();
		int y = (int) event.getRawY() - 50;

		inputHandler
				.addAction(new MouseInputAction(MouseGEv.MOUSE_MOVED, x, y));

		if (event.getAction() == MotionEvent.ACTION_UP) {
			inputHandler.addAction(new MouseInputAction(
					MouseGEv.MOUSE_LEFT_RELEASED, x, y));
		}

		if (gestureDetector.onTouchEvent(event)) {
			return true;
		}

		return true;
	}

}
