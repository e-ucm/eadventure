package es.eucm.eadventure.common.impl.reader;

import java.lang.reflect.Field;

import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.common.model.impl.EAdElementImpl;

public class ProxyElement extends EAdElementImpl {

	private Object parent;
	
	private Field field;
	
	private String value;
	
	private EAdList<Object> list;
	
	private int listPos;
	
	public ProxyElement(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Field getField() {
		return field;
	}

	public Object getParent() {
		return parent;
	}

	public void setList(EAdList<Object> list, int i) {
		this.list = list;
		this.listPos = i;
	}

	public EAdList<Object> getList() {
		return list;
	}
	
	public int getListPos() {
		return listPos;
	}

}
