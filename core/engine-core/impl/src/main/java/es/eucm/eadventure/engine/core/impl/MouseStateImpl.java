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

package es.eucm.eadventure.engine.core.impl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import es.eucm.eadventure.engine.core.MouseState;
import es.eucm.eadventure.engine.core.gameobjects.GameObject;
import es.eucm.eadventure.engine.core.guiactions.MouseAction;
import es.eucm.eadventure.engine.core.platform.GUI;
import es.eucm.eadventure.engine.core.platform.PlatformConfiguration;

@Singleton
public class MouseStateImpl implements MouseState {

	public static final int OUT_VAL = -1;
	
	private int mouseX = OUT_VAL;
	
	private int mouseY = OUT_VAL;

	private boolean mousePressed = false;

	private Queue<MouseAction> mouseEvents;
	
	private GameObject<?> gameObjectUnderMouse;

	private GameObject<?> draggingGameObject;
	
	private int mouseDifX = 0;
	
	private int mouseDifY = 0;
	
	private int rawX = OUT_VAL;
	
	private int rawY = OUT_VAL;
	
	private int underMouseOffsetX = 0;
	
	private int underMouseOffsetY = 0;
	
	private boolean moved;
	
	private PlatformConfiguration platformConfiguration;
	
	@Inject
	public MouseStateImpl(PlatformConfiguration platformConfiguration) {
		mouseEvents = new ConcurrentLinkedQueue<MouseAction>();
		this.platformConfiguration = platformConfiguration;
	}
	
	public int getVirtualMouseX() {
		return mouseX;
	}

	public int getVirtualMouseY() {
		return mouseY;
	}

	public void setMousePosition(int mouseX, int mouseY) {
		this.rawX = mouseX;
		this.rawY = mouseY;
		this.mouseX = (int) ((float) mouseX / platformConfiguration.getHeight() * GUI.VIRTUAL_HEIGHT);
		this.mouseY = (int) ((float) mouseY / platformConfiguration.getHeight() * GUI.VIRTUAL_HEIGHT);
		this.moved = true;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public Queue<MouseAction> getMouseEvents() {
		return mouseEvents;
	}

	public GameObject<?> getGameObjectUnderMouse() {
		return gameObjectUnderMouse;
	}

	public void setElementGameObject(GameObject<?> gameObjectUnderMouse, int offsetX, int offsetY) {
		this.gameObjectUnderMouse = gameObjectUnderMouse;
		this.underMouseOffsetX = offsetX;
		this.underMouseOffsetY = offsetY;
	}

	public GameObject<?> getDraggingGameObject() {
		return draggingGameObject;
	}

	@Override
	public void setDraggingGameObject(GameObject<?> draggingGameObject) {
		this.draggingGameObject = draggingGameObject;
		if (draggingGameObject != null && draggingGameObject.getPosition() != null) {
			this.mouseDifX = mouseX - draggingGameObject.getPosition().getX() - underMouseOffsetX;
			this.mouseDifY = mouseY - draggingGameObject.getPosition().getY() - underMouseOffsetY;
		}
	}

	@Override
	public int getMouseDifX() {
		return mouseDifX;
	}
	
	@Override
	public int getMouseDifY() {
		return mouseDifY;
	}
	
	public boolean isInside() {
		if (mouseX == OUT_VAL && mouseY == OUT_VAL)
			return false;
		return true;
	}
	
	public boolean pullMovedStatus() {
		if (moved) {
 			moved = false;
 			return true;
		}
		return false;
	}

	@Override
	public int getRawX() {
		return rawX;
	}

	@Override
	public int getRawY() {
		return rawY;
	}
	
}
