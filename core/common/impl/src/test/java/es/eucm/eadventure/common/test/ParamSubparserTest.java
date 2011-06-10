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

package es.eucm.eadventure.common.test;

import junit.framework.TestCase;

import org.junit.Test;
import org.xml.sax.helpers.AttributesImpl;

import es.eucm.eadventure.common.impl.reader.subparsers.ParamSubparser;
import es.eucm.eadventure.common.interfaces.Param;
import es.eucm.eadventure.common.model.EAdElement;
import es.eucm.eadventure.common.model.EAdElementList;
import es.eucm.eadventure.common.model.events.EAdEvent;
import es.eucm.eadventure.common.resources.EAdBundleId;
import es.eucm.eadventure.common.resources.EAdResources;
import es.eucm.eadventure.common.resources.assets.AssetDescriptor;

public class ParamSubparserTest extends TestCase {

	private class TestElement implements EAdElement {

		@Param("privateField")
		private String privateField;
		
		public String publicField;
		
		private Integer integerField;
		
		private Boolean booleanField;
		
		@Param("data")
		private int testField;
				
		public String getPrivateField() {
			return privateField;
		}
		
		public String getPublicField() {
			return publicField;
		}
		
		public Integer getIntegerField() {
			return integerField;
		}
		
		public Boolean getBooleanField() {
			return booleanField;
		}
		
		public int getTestField() {
			return testField;
		}
		
		public String getId() {
			return "";
		}
		
		public EAdElement copy() {return null;}
		public EAdElement copy(boolean deepCopy) {return null;}
		public AssetDescriptor getAsset(String id) {return null;}
		public AssetDescriptor getAsset(EAdBundleId bundleId, String id) {return null;}
		public EAdBundleId getInitialBundle() {return null;}
		public EAdResources getResources() {return null;}

		@Override
		public EAdElementList<EAdEvent> getEvents() {
			return null;
		}
	}

	private TestElement testElement;
	
	@Override
	public void setUp() {
		testElement = new TestElement();
	}

	@Test
	public void testPrivateField() {
		AttributesImpl attributes = new AttributesImpl();
		attributes.addAttribute("", "name", "name", "", "privateField");
		ParamSubparser subparser = new ParamSubparser(testElement, attributes);
		
		subparser.characters("test".toCharArray(), 0, 4);
		
		subparser.endElement();
		
		assertEquals("test", testElement.getPrivateField());
	}

	@Test
	public void testPublicField() {
		AttributesImpl attributes = new AttributesImpl();
		attributes.addAttribute("", "name", "name", "", "publicField");

		ParamSubparser subparser = new ParamSubparser(testElement, attributes);
		
		subparser.characters("test".toCharArray(), 0, 4);
		
		subparser.endElement();
		
		assertEquals("test", testElement.getPublicField());
	}

	@Test
	public void testIntegerField() {
		AttributesImpl attributes = new AttributesImpl();
		attributes.addAttribute("", "name", "name", "", "integerField");

		ParamSubparser subparser = new ParamSubparser(testElement, attributes);
		
		subparser.characters("10".toCharArray(), 0, 2);
		
		subparser.endElement();
		
		assertEquals(new Integer(10), testElement.getIntegerField());
	}

	@Test
	public void testBooleanField() {
		AttributesImpl attributes = new AttributesImpl();
		attributes.addAttribute("", "name", "name", "", "booleanField");

		ParamSubparser subparser = new ParamSubparser(testElement, attributes);
		
		subparser.characters("true".toCharArray(), 0, 4);
		
		subparser.endElement();
		
		assertEquals(Boolean.TRUE, testElement.getBooleanField());
	}

	@Test
	public void testParamAnnotationField() {
		AttributesImpl attributes = new AttributesImpl();
		attributes.addAttribute("", "name", "name", "", "data");

		ParamSubparser subparser = new ParamSubparser(testElement, attributes);
		
		subparser.characters("32523".toCharArray(), 0, 5);
		
		subparser.endElement();
		
		assertEquals(32523, testElement.getTestField());
	}

}
