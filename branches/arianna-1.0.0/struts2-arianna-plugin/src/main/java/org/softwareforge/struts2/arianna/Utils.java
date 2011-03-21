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
package org.softwareforge.struts2.arianna;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 *	@author Giovanni Tosto
 *	@version $Id$
 */
public class Utils 
{
	static final Log LOG = LogFactory.getLog(Utils.class);
	
	/**	
	 * Find a method with no arguments with the name <var>name</var>.
	 *  
	 * It is just a wrapper of the <code>class.getMethod()</code>.
	 * 
	 * @param clazz the class from which are searching. 
	 * @param name	the method name we are looking for.
	 * @return the method or <code>null</null> if such a method cannot be found.
	 *  
	 */
	public static Method	findMethod(Class<?> clazz, String name) {
		
		try {
			return clazz.getMethod(name, (Class[])null);
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
	public static boolean compareParametersMap(Map m1,Map m2)
	{
		if (m1.size() != m2.size() )
			return false;
		
		for (Object key : m1.keySet() ) {
			String[] v1 = (String[]) m1.get(key);
			String[] v2 = (String[]) m2.get(key);
			
			if ( v1.length != v2.length )
				return false;
			// XXX should values be sorted first ?
			// FIXME take care of null values ?
			for (int i = 0; i < v2.length; i++) {
				if ( ! v1[i].equals(v2[i]) ) 
						return false;
			}
		}
		return true;
	}
	
}
