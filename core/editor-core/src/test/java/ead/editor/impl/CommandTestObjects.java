/**
 * eAdventure (formerly <e-Adventure> and <e-Game>) is a research project of the
 *    <e-UCM> research group.
 *
 *    Copyright 2005-2010 <e-UCM> research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    <e-UCM> is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          C Profesor Jose Garcia Santesmases sn,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure, version 2.0
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package ead.editor.impl;

/**
 * Class that represents the object which values are changed to test the
 * functionality of the changing values commands.
 */
public class CommandTestObjects {

	/**
	 * String attribute to test the command that changes values of the members of
	 * the String class.
	 */
	private String s;

	/**
	 * Integer attribute to test the command that changes values of the members
	 * of the Integer class.
	 */
	private Integer i;

	/**
	 * Boolean attribute to test the command that changes values of the members
	 * of the Boolean class.
	 */
	private Boolean b;

	/**
	 * Object attribute to test the command that changes values of the members of
	 * the Object class.
	 */
	private Object o;

	/**
	 * Default constructor for the CommandTestObjects class.
	 */
	public CommandTestObjects(String ss, Integer ii, Boolean bb, Object oo) {
		this.s = ss;
		this.i = ii;
		this.b = bb;
		this.o = oo;
	}

	/**
	 * Returns the String value of the test object.
	 */
	public String getS() {
		return s;
	}

	/**
	 * Sets the String value of the test object.
	 */
	public void setS(String ss) {
		this.s = ss;
	}

	/**
	 * Returns the Integer value of the test object.
	 */
	public Integer getI() {
		return i;
	}

	/**
	 * Sets the Integer value of the test object.
	 */
	public void setI(Integer ii) {
		this.i = ii;
	}

	/**
	 * Returns the Boolean value of the test object.
	 */
	public Boolean getB() {
		return b;
	}

	/**
	 * Sets the Boolean value of the test object.
	 */
	public void setB(Boolean bb) {
		this.b = bb;
	}

	/**
	 * Returns the Object value of the test object.
	 */
	public Object getO() {
		return o;
	}

	/**
	 * Sets the Object value of the test object.
	 */
	public void setO(Object oo) {
		this.o = oo;
	}

}
