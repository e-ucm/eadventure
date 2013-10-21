/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.ead.editor.view.generic;

import es.eucm.ead.editor.view.generic.accessors.Accessor;
import es.eucm.ead.editor.view.generic.accessors.IntrospectingAccessor;
import es.eucm.ead.editor.view.generic.table.TableSupport;

/**
 *
 * @author mfreire
 */
public class AccessorColumn<T, K> extends TableSupport.ColumnSpec<T, K> {
	private final String fieldName;
	
	public AccessorColumn(String name, String fieldName, 
			Class<?> clazz, int width) {
		super(name, clazz, false, width);
		this.fieldName = fieldName;
	}
	
	public Accessor getAccessor(T o, K i) {
		return new IntrospectingAccessor(o, fieldName);
	}

	@Override
	public Object getValue(T o, K i) {
		return getAccessor(o, i).read();
	}

	@Override
	public void setValue(T o, K i, Object value) {
		// not allowed
	}
}
