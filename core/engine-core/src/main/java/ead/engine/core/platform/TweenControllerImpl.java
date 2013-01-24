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

package ead.engine.core.platform;

import java.util.List;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ead.common.model.elements.operations.BasicField;
import ead.common.model.elements.operations.EAdField;
import ead.engine.core.game.ValueMap;

@Singleton
public class TweenControllerImpl implements TweenController,
		TweenAccessor<EAdField<?>> {

	public static final int DEFAULT = 0;

	private TweenManager t;

	private ValueMap valueMap;

	@Inject
	public TweenControllerImpl(ValueMap valueMap) {
		this.valueMap = valueMap;
		t = new TweenManager();
		Tween.registerAccessor(EAdField.class, this);
		Tween.registerAccessor(BasicField.class, this);
	}

	@Override
	public TweenManager add(BaseTween<?> object) {
		return t.add(object);
	}

	@Override
	public boolean containsTarget(Object arg0, int arg1) {
		return t.containsTarget(arg0, arg1);
	}

	@Override
	public boolean containsTarget(Object arg0) {
		return t.containsTarget(arg0);
	}

	@Override
	public List<BaseTween<?>> getObjects() {
		return t.getObjects();
	}

	@Override
	public int getRunningTimelinesCount() {
		return t.getRunningTimelinesCount();
	}

	@Override
	public int getRunningTweensCount() {
		return t.getRunningTweensCount();
	}

	@Override
	public void killAll() {
		t.killAll();

	}

	@Override
	public void killTarget(Object arg0, int arg1) {
		t.killTarget(arg0, arg1);

	}

	@Override
	public void killTarget(Object arg0) {
		t.killTarget(arg0);

	}

	@Override
	public void pause() {
		t.pause();

	}

	@Override
	public void resume() {
		t.resume();

	}

	@Override
	public int size() {
		return t.size();
	}

	@Override
	public void update(float arg0) {
		t.update(arg0);

	}

	@Override
	public int getValues(EAdField<?> field, int type, float[] values) {
		switch (type) {
		default:
			Object o = valueMap.getValue(field);
			if (o instanceof Number) {
				values[0] = ((Number) o).floatValue();
				return 1;
			} else {
				return 0;
			}
		}
	}

	@Override
	public void setValues(EAdField<?> field, int type, float[] values) {
		switch (type) {
		default:
			if (field.getVarDef().getType() == Float.class) {
				valueMap.setValue(field, values[0]);
			} else if (field.getVarDef().getType() == Integer.class) {
				valueMap.setValue(field, (int) values[0]);
			}
		}

	}

	@Override
	public TweenManager getManager() {
		return t;
	}

}
