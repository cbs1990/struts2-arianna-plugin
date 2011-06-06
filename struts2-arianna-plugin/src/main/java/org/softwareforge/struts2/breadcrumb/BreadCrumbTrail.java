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
import java.util.ListIterator;
import java.util.Stack;

/** Represents the bread crumbs trail in its totality.
 * 
 * @author Giovanni Tosto 
 * @version $Id$
 */
public class BreadCrumbTrail implements Serializable {
	
	/** 
	 * The name of this trail. (Not actually used)
	 */
	String name;
	
	/**
	 * The maximum number of crumbs the trail must keep. 
	 */
	int	maxCrumbs;

//	/**
//	 * the rewind mode the trail will use if not specified otherwise 
//	 */
//	RewindMode	rewindMode;
//
//	/**
//	 * The comparator the trail will use if not specified otherwise.
//	 */
//	Comparator<Crumb>	comparator;
	
	/**
	 * the actual crumbs kept by the trail.
	 */
	Stack<Crumb> crumbs = new Stack<Crumb>();

	// utility methods
	///////////////////////////////////////////
	
	/**
	 * Rewinds the trail to the crumb at the specified position.
	 * 
	 * @param index 
	 */
	public void rewindAt(int index) {
		for (int i=index+1, size=crumbs.size(); i<size; i++) {
			crumbs.remove(index+1);
		}		
	}

//	public	int	indexOf(Crumb crumb)
//	{
//		return indexOf(crumb, comparator);
//	}

	/**
	 * Find the index of the first crumb that is equals to the given crumb: <var>crumb</var>.
	 * Here equals means ...
	 *   
	 * @param	crumb the crumbs to compare.
	 * @return	the index of the first crumb <em>equals</em> to <var>crumb</var> 
	 * 			or <code>-1</code> if no such crumb exists.  
	 */
	public	int	indexOf(Crumb crumb, Comparator<Crumb> comparator) 
	{		
		ListIterator<Crumb> itor = crumbs.listIterator();
		
		while (itor.hasNext()) {
			Crumb c = itor.next();
			if ( comparator.compare(c, crumb) == 0 ) {
				return itor.previousIndex();
			}
		}
		return -1;
	}
	
//	public int lastIndexOf(Crumb crumb) {
//		return lastIndexOf(crumb, comparator);
//	}
	
	public int lastIndexOf(Crumb crumb, Comparator<Crumb> comparator)
	{
		ListIterator<Crumb> itor = crumbs.listIterator(crumbs.size());
		
		while ( itor.hasPrevious() ) {
			Crumb c = itor.previous();
			if ( comparator.compare(c, crumb) == 0 ) {
				return itor.nextIndex();
			}			
		}
		
		return -1;
	}
		
	// properties setters
	///////////////////////////////////////////
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMaxCrumbs(int maxCrumbs) {
		this.maxCrumbs = maxCrumbs;
	}
		
	// properties getters
	///////////////////////////////////////////
	
	public String getName() {
		return name;
	}
	
	public int getMaxCrumbs() {
		return maxCrumbs;
	}
	

	public	Stack<Crumb>	getCrumbs() {
		if ( crumbs == null) {
			crumbs = new Stack<Crumb>();
		}
		return crumbs;
	}
}
