package es.eucm.eadventure.common.params;

/***
 * Basic implementation for an {@link EAdParam}
 * 
 * 
 */
public abstract class EAdParamImpl implements EAdParam {

	public boolean equals(Object o) {
		if (o != null && o instanceof EAdParam && this.toString() != null)
			return this.toString().equals(o.toString());
		return false;
	}

}
