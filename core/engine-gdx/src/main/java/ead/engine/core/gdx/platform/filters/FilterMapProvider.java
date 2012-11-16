package ead.engine.core.gdx.platform.filters;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ead.common.resources.assets.drawable.filters.MatrixFilter;
import ead.engine.core.platform.rendering.filters.RuntimeFilter;
import ead.tools.MapProvider;

public class FilterMapProvider implements MapProvider<Class<?>, RuntimeFilter<?, SpriteBatch>>{

	@Override
	public Map<Class<?>, RuntimeFilter<?, SpriteBatch>> getMap() {
		Map<Class<?>, RuntimeFilter<?, SpriteBatch>> map = new HashMap<Class<?>, RuntimeFilter<?, SpriteBatch>>();
		map.put(MatrixFilter.class, new MatrixRuntimeFilter());
		return map;
	}

}
