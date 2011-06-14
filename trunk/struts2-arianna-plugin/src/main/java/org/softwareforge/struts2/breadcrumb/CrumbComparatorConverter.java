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
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;
import com.opensymphony.xwork2.inject.Inject;

/**
 * An utility class used to convert a
 * <em>fully qualified class name<em> to comparator instances.
 * 
 * This converter is used mainly by the struts framework to convert <acronym>fqcn<acronym> when 
 * initializing interceptors instances.
 * 
 * @author Giovanni Tosto
 * @version $Id: CrumbComparatorConverter.java 289 2011-06-14 19:41:01Z
 *          giovanni.tosto $
 */
@SuppressWarnings("rawtypes")
public class CrumbComparatorConverter extends StrutsTypeConverter {
    private final Log LOG = LogFactory.getLog(CrumbComparatorConverter.class);

    @Inject("arianna")
    AriannaPlugin plugin;

    public Object convertFromString(Map context, String[] values, Class toClass) {
	if (values == null)
	    return null;

	if (values.length != 1) {
	    throw new TypeConversionException(
		    "Cannot convert non scalar value " + values);
	}

	Comparator<Crumb> comparator = plugin
		.lookupComparatorByClass(values[0]);
	if (comparator != null) {
	    String msg = String.format("{%s} Converted %s -> %s ", this,
		    values[0], comparator);
	    LOG.debug(msg);
	    return comparator;
	}

	throw new TypeConversionException("Cannot convert class " + values[0]
		+ " to a Comparator");

    }

    public String convertToString(Map context, Object o) {
	return o == null ? null : o.getClass().getName();
    }
}
