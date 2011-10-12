package es.eucm.eadventure.editor.view.generics.impl;

import es.eucm.eadventure.common.model.extra.EAdList;
import es.eucm.eadventure.editor.view.generics.EAdListFieldDescriptor;
import es.eucm.eadventure.editor.view.generics.Panel;

/**
 * Generic implementation of {@link EAdListFieldDescriptor}
 * 
 * @param <S>
 */
public class EAdListFieldDescriptorImpl<S> extends FieldDescriptorImpl<EAdList<S>> implements EAdListFieldDescriptor<S> {

	private EAdList<S> list;

	/**
	 * @param element
	 *            The element where the value is stored
	 * @param fieldName
	 *            The name of the field
	 * @param list
	 */
	public EAdListFieldDescriptorImpl(Object element, String fieldName, EAdList<S> list) {
		super(element, fieldName);
		this.list = list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.eucm.eadventure.editor.view.generics.FieldDescriptor#getElement()
	 */
	@Override
	public Object getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.eucm.eadventure.editor.view.generics.FieldDescriptor#getFieldName()
	 */
	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public S getElementAt(int pos) {
		return list.get(pos);
	}

	@Override
	public Panel getPanel(int pos, boolean selected) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EAdList<S> getList() {
		return list;
	}

}
