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

import java.io.Serializable;
import java.util.Comparator;

/**
 * A crumb comparator that check for equality of namespace, action and method
 * fields.
 * 
 * This comparator does not check for crumb name equality, use NameComparator for that.
 * 
 * @author Giovanni Tosto
 * @version $Id$
 */
public class ActionComparator implements Comparator<Crumb>, Serializable {
    private static final long serialVersionUID = 1L;

    public int compare(Crumb c1, Crumb c2) {
	if (c1 == c2)
	    return 0;
	if (c1 == null && c2 != null)
	    return +1;
	if (c1 != null && c2 == null)
	    return -1;

	// if (c1 == null || c2 == null)
	// throw new IllegalArgumentException("both crumbs must be not null");

	String h1 = hash(c1);
	String h2 = hash(c2);

	return h1.compareTo(h2);
    }

    final String hash(Crumb c) {
	return c.namespace + "/" + c.action + "!" + c.method;
    }
}
