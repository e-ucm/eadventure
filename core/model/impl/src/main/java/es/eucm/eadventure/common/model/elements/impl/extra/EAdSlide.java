package es.eucm.eadventure.common.model.elements.impl.extra;

import es.eucm.eadventure.common.Element;
import es.eucm.eadventure.common.Param;
import es.eucm.eadventure.common.model.elements.impl.EAdSceneImpl;

/**
 * <p>One of the slides of the cutscene</p>
 */
@Element(detailed = EAdSlide.class, runtime = EAdSceneImpl.class)
public class EAdSlide extends EAdSceneImpl {

	@Param("time")
	private Integer time;
	
	public EAdSlide(String id) {
		super(id);
		returnable = false;
		time = -1;
	}
	
	public Integer getTime() {
		return time;
	}
	
	public void setTime(Integer time) {
		this.time = time;
	}

}
