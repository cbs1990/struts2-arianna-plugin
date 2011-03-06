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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 * 
 *	@author Giovanni Tosto
 *	@version $Id$
 */
@SuppressWarnings("rawtypes")
public class CrumbComparatorConverter extends StrutsTypeConverter
{
	private final Log LOG = LogFactory.getLog(CrumbComparatorConverter.class);
	
	public Object convertFromString(Map context, String[] values, Class toClass) 
	{
		Object obj = new NameComparator();
		if ( LOG.isDebugEnabled() )
		{
			String msg = String.format("{%s} Converted %s -> %s ", values[0], obj,this);
			LOG.debug(msg);			
		}
		
		return obj;
	}

	
	public String convertToString(Map context, Object o) 
	{
		LOG.warn("convertToString is not supported by this Converter");
		return "" + o.toString();
	}
}
