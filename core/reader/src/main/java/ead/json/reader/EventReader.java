package ead.json.reader;

import java.util.Collection;

import ead.json.reader.model.JsonEvent;
import ead.reader.model.ObjectsFactory;

public class EventReader {
	private ObjectsFactory objectsFactory;

	public EventReader(ObjectsFactory objectsFactory) {
		this.objectsFactory = objectsFactory;
	}

	public void addEvents(Collection<JsonEvent> events) {

	}
}
