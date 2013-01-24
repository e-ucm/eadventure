package ead.writer.model.writers;

import ead.writer.model.ModelVisitor;

public abstract class AbstractWriter<T> implements Writer<T> {

	protected ModelVisitor modelVisitor;

	public AbstractWriter(ModelVisitor modelVisitor) {
		this.modelVisitor = modelVisitor;
	}

	protected String translateClass(Class<? extends Object> clazz) {
		return modelVisitor.translateClass(clazz);
	}

}
