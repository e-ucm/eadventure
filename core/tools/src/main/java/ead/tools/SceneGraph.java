package ead.tools;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ead.common.model.elements.EAdEffect;
import ead.common.model.elements.scene.EAdScene;
import ead.common.resources.assets.AssetDescriptor;

public interface SceneGraph {

	public Set<EAdScene> getScenes();

	public Map<EAdScene, List<EAdScene>> getGraph();

	public List<EAdEffect> getEffectsVisited();

	public void generateGraph(EAdScene initialScene);

	public Map<EAdScene, List<AssetDescriptor>> getSceneAssets();

}
