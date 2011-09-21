package es.eucm.eadventure.common.model.trajectories.impl;

import es.eucm.eadventure.common.interfaces.Element;
import es.eucm.eadventure.common.model.EAdElement;

@Element(detailed = Side.class, runtime = Side.class)
public class Side implements EAdElement {

	private String idStart;

	private String idEnd;

	private float length = 1;

	private float realLength = 1;

	public Side(String idStart, String idEnd) {

		this.idStart = idStart;
		this.idEnd = idEnd;
	}

	public void setRealLength(float realLength) {
		this.realLength = realLength;
	}

	public String getIDStart() {

		return idStart;
	}

	public String getIDEnd() {

		return idEnd;
	}

	public void setLenght(float length) {

		this.length = length;
	}

	@Override
	public boolean equals(Object o) {

		if (o == null)
			return false;
		if (o instanceof Side) {
			Side side = (Side) o;
			if (side.idEnd.equals(this.idEnd)
					&& side.idStart.equals(this.idStart))
				return true;
		}
		return false;
	}

	public float getLength() {

		return length;
	}

	public EAdElement copy() {

		Side s = new Side(null, null);
		s.idEnd = (idEnd != null ? new String(idEnd) : null);
		s.idStart = (idStart != null ? new String(idStart) : null);
		s.length = length;
		return s;
	}

	public float getRealLength() {
		return realLength;
	}

	@Override
	public EAdElement copy(boolean deepCopy) {
		return copy();
	}

	@Override
	public String getId() {
		return idStart + "_" + idEnd;
	}
}
