package ead.json.reader.model;

import java.util.Collection;

public class JsonEvent extends JsonIdentified {

	public String type;
	public Collection<String> targets;
	public Collection<Object> effects;

}
