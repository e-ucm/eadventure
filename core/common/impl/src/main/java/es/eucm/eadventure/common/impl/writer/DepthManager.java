package es.eucm.eadventure.common.impl.writer;

import java.util.ArrayList;
import java.util.List;

import es.eucm.eadventure.common.model.extra.EAdList;

public class DepthManager {

	private List<EAdList<Object>> lists;
	
	private List<Object> stored;
	
	public DepthManager() {
		lists = new ArrayList<EAdList<Object>>();
		stored = new ArrayList<Object>();
	}
	
	public void addList(EAdList<Object> list) {
		lists.add(list);
	}
	
	public void removeList(EAdList<Object> list) {
		lists.remove(list);
	}
	
	public boolean inPreviousList(Object o) {
		for (int i = 0; i < lists.size() - 1; i++)
			if (lists.get(i).contains(o))
				return true;
		return false;
	}
	
	public boolean isStored(Object o) {
		return stored.contains(o);
	}
	
	public void setStored(Object o) {
		stored.add(o);
	}
}
