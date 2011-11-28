package es.eucm.eadventure.common.impl.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.eucm.eadventure.common.model.extra.EAdList;

public class DepthManager {

	private List<EAdList<Object>> lists;
	
	private List<Object> stored;
	
	private Map<String, String> classAliases;

	private Map<String, String> aliasMap;
	
	private static final boolean USE_DEPTH_CONTROL = false;
	
	public DepthManager() {
		lists = new ArrayList<EAdList<Object>>();
		stored = new ArrayList<Object>();
		classAliases = new HashMap<String, String>();
		aliasMap = new HashMap<String, String>();
	}
	
	public void addList(EAdList<Object> list) {
		lists.add(list);
	}
	
	public void removeList(EAdList<Object> list) {
		lists.remove(list);
	}

	public Map<String, String> getClassAliases() {
		return classAliases;
	}
	
	public boolean inPreviousList(Object o) {
		if (USE_DEPTH_CONTROL) {
			for (int i = 0; i < lists.size() - 1; i++)
				if (lists.get(i).contains(o))
					return true;
		}
		return false;
	}
	
	public boolean isStored(Object o) {
		return stored.contains(o);
	}
	
	public void setStored(Object o) {
		stored.add(o);
	}

	public Map<String, String> getAliasMap() {
		return aliasMap;
	}
}
