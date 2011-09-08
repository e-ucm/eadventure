package es.eucm.eadventure.common.params;

/***
 * Basic implementation for an {@link EAdParam}
 * 
 * 
 */
public abstract class EAdParamImpl implements EAdParam {

	public boolean equals(Object o) {
		return this.toString().equals(o.toString());
	}

}
