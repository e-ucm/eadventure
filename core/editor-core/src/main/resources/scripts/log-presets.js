define({
	load: [
		// object loading
		Packages.es.eucm.ead.reader2.model.readers.ObjectReader,
		Packages.es.eucm.ead.reader2.AdventureReader,
		// recursion into bits of model when importing
		Packages.es.eucm.ead.editor.model.EditorModelLoader, 
		// access to the model
		Packages.es.eucm.ead.editor.model.EditorModel, 
		Packages.es.eucm.ead.editor.model.visitor.ModelVisitorDriver
	],
})
