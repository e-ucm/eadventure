/**
 * <e-Adventure> is an <e-UCM> research project.
 * <e-UCM>, Department of Software Engineering and Artificial Intelligence.
 * Faculty of Informatics, Complutense University of Madrid (Spain).
 * @author Del Blanco, A., Marchiori, E., Torrente, F.J.
 * @author Moreno-Ger, P. & Fern‡ndez-Manj—n, B. (directors)
 * @year 2009
 * Web-site: http://e-adventure.e-ucm.es
 */

/*
 Copyright (C) 2004-2009 <e-UCM> research group

 This file is part of <e-Adventure> project, an educational game & game-like 
 simulation authoring tool, availabe at http://e-adventure.e-ucm.es. 

 <e-Adventure> is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 <e-Adventure> is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with <e-Adventure>; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */
package es.eucm.eadventure.common.model.weev.common;

/**
 * Interface for retractable elements in the models
 * <p>
 * This interface is implemented by those elements in the WEEV model that in the
 * visual representation can be retracted (i.e. minimized).
 * 
 */
public interface Retractable {

	/**
	 * @param retracted
	 *            the new state of the retraction (TRUE if retracted)
	 */
	public void setRetracted(boolean retracted);

	/**
	 * @return the state of the retraction (TRUE if retracted)
	 */
	public boolean isRetracted();

}
