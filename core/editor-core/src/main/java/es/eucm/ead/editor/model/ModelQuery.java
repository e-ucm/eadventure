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

package es.eucm.ead.editor.model;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A query to be interpreted by the model. Mimics standard queries from
 * search engines: a series of field-values, implicitly joined together with a
 * "the more matches, the better" approach.
 *
 * Expected format is of the form
 *		Query ::= QueryPart (QueryPart )*
 *      QueryPart ::= field:value | value
 *
 * QueryParts are separated by spaces. Double-quotes ('"') can be used include
 * spaces or colons within a field or value, by quoting the whole field or value.
 * Double-quotes can be escaped with '\' to include them within the
 * field or value; and escapes can themselves be escaped via additional escapes.
 */
public class ModelQuery {

	private static final Logger logger = LoggerFactory.getLogger("ModelQuery");

	/**
	 * The parts of the query
	 */
	private ArrayList<QueryPart> queryParts = new ArrayList<QueryPart>();

	public ModelQuery(String queryString) {
		try {
			parseQuery(queryString);
		} catch (Exception e) {
			logger.warn("Bad query {}", queryString, e);
			throw new IllegalArgumentException("Illegal query string '"
					+ queryString + "'", e);
		}
	}

	private ModelQuery() {
		// do nothing.
	}

	/**
	 * Tests the validity of a queryString
	 * @param queryString
	 * @return true if valid, false if errors detected
	 */
	public static boolean isValid(String queryString) {
		ModelQuery mq = new ModelQuery();
		boolean valid = true;
		try {
			mq.parseQuery(queryString);
		} catch (Exception e) {
			valid = false;
		}
		return valid;
	}

	private void parseQuery(String queryString) {
		String q = queryString + " ";
		queryParts.clear();
		int startPos = 0; // start of next part
		int colonPos = -1; // offset, within last part, of colon
		int ignored = 0;
		boolean inQuote = false;
		boolean inEscape = false;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < q.length(); i++) {
			char c = q.charAt(i);
			switch (c) {
			case '\\': {
				if (!inEscape) {
					inEscape = true;
					ignored++;
				} else {
					sb.append(c);
					inEscape = false;
				}
				break;
			}
			case '"': {
				if (!inEscape) {
					inQuote = !inQuote;
					ignored++;
				} else {
					sb.append(c);
					inEscape = false;
				}
				break;
			}
			case ' ': {
				if (!inQuote && sb.length() > 0) {
					String f = (colonPos >= 0) ? sb.substring(0, colonPos) : "";
					String v = sb.substring(colonPos + 1);
					queryParts.add(new QueryPart(f, v));
					sb.delete(0, sb.length());
					inEscape = false;
					colonPos = -1;
					// ignore extra spaces
					while ((i + 1 < q.length()) && (q.charAt(i + 1) == ' ')) {
						i++;
					}
					startPos = i + 1;
				} else {
					sb.append(c);
					inEscape = false;
				}
				break;
			}
			case ':': {
				if (!inQuote) {
					if (colonPos != -1) {
						throw new IllegalArgumentException(
								"too many ':'s -- maximum is 1 per queryPart");
					}
					colonPos = (i - ignored - startPos);
				}
				sb.append(c);
				inEscape = false;
				break;
			}
			default: {
				sb.append(c);
				inEscape = false;
			}
			}
		}
		if (inQuote || inEscape) {
			throw new IllegalArgumentException("unclosed quote or escape");
		}
		if (queryParts.isEmpty()) {
			throw new IllegalArgumentException("expected at least 1 queryPart");
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (QueryPart p : queryParts) {
			sb.append("[").append(p.field).append("] : [").append(p.value)
					.append("]\n");
		}
		return sb.toString();
	}

	public ArrayList<QueryPart> getQueryParts() {
		return queryParts;
	}

	/**
	 * A part of a query
	 */
	public static class QueryPart {

		private String field;
		private String value;

		public QueryPart(String field, String value) {
			this.field = field;
			this.value = value;
			if (field != null && field.equals(ModelIndex.editorIdQueryField)) {
				try {
					Integer.parseInt(value);
				} catch (NumberFormatException nfe) {
					throw new IllegalArgumentException(
							"Direct ID queries can only be performed on integers",
							nfe);
				}
			}
		}

		public String getField() {
			return field;
		}

		public String getValue() {
			return value;
		}
	}
}
