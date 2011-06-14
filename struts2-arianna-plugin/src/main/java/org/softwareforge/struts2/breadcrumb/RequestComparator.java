/*
 *  Copyright 2011 - Giovanni Tosto
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.softwareforge.struts2.breadcrumb;

/**
 * A crumb comparator that behaves as an ActionComparator and besides checks for
 * equality of http request parameters (names,values) pairs.
 * 
 * @author Giovanni Tosto
 * @version $Id: RequestComparator.java 289 2011-06-14 19:41:01Z giovanni.tosto$
 */
public class RequestComparator extends ActionComparator {
	private static final long serialVersionUID = 1L;

	public int compare(Crumb c1, Crumb c2) {
		int same = super.compare(c1, c2);

		// compare request parameters
		if (same == 0) {
			same = Utils.compareParametersMap(c1.getParams(), c2.getParams()) ? 0 : -1;
		}

		return same;
	}

}
