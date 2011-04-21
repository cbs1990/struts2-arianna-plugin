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

import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;
import org.softwareforge.struts2.breadcrumb.Crumb;

public class AriannaPlugin {
    
    private static final Log LOG = LogFactory.getLog(BreadCrumbInterceptor.class);
    
    public	int	maxCrumbs = 5;
    
    public String	getVersion() {
	return getClass().getPackage().getSpecificationVersion();
    }
	
    public String	getTitle() {
	return getClass().getPackage().getSpecificationTitle();
    }
 
    
    protected Comparator<Crumb> lookupComparatorByClass(Class clazz) {
	try {
	    Comparator instance = (Comparator) clazz.newInstance();
	    return instance;
	} catch (InstantiationException e) {
	    LOG.error("Cannot create comparator of class " + clazz, e);
	} catch (IllegalAccessException e) {
	    LOG.error("Cannot create comparator of class " + clazz, e);
	}
	
	return null;

    }

    
}
