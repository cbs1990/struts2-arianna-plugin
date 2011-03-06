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

import java.util.Comparator;

/**
 * 
 *	@author Giovanni Tosto
 *	@version $Id$
 */
public class NameComparator implements Comparator<Crumb> 
{

	public int compare(Crumb c1, Crumb c2) 
	{
		if (c1 == c2 )
			return 0;
		if ( c1 == null && c2 != null)
			return +1;
		if ( c1 != null && c2 == null)
			return -1;
		if ( c1.name != null )
			return c1.name.compareTo(c2.name);			
		if ( c2.name != null )
			return c2.name.compareTo(c1.name);
		
		return c1.name.compareTo(c2.name);
	}

}
