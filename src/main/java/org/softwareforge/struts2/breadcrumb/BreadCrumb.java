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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Comparator;

/**
 * @author Giovanni Tosto
 * @version $Id$
 */

@Documented
@Retention(RUNTIME)
@Target({TYPE,METHOD})
public @interface BreadCrumb 
{	
	// Tricky class to allow default <em>NULL</em> values;
	static final class NULL implements CrumbComparator 
	{
		public int compare(Crumb o1, Crumb o2) {
			throw new UnsupportedOperationException("This comparator cannot be used in this way");
		}		
	};

	public String 		value();
	
	public RewindMode	rewind() default RewindMode.DEFAULT;

	public Class<? extends Comparator>		comparator() default NULL.class;
	
//	public String		comparator() default "";
	
	
//	public String		trail()	default "main";
	
}

