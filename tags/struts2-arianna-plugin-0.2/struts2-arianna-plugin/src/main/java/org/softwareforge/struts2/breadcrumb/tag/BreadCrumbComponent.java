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
package org.softwareforge.struts2.breadcrumb.tag;

import java.io.Writer;
import java.util.Iterator;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.components.ContextBean;
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;
import org.softwareforge.struts2.breadcrumb.Crumb;

import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

public class BreadCrumbComponent extends ContextBean 
{
    private static final Logger LOG = LoggerFactory.getLogger(BreadCrumbComponent.class);
	
	Stack<Crumb> crumbs;	
	Iterator<Crumb>	crumbIterator;
	
	@SuppressWarnings("unchecked")
	public BreadCrumbComponent(ValueStack stack, HttpServletRequest request) {
		super(stack);
		
		HttpSession session = request.getSession(false);
		if (session != null) {
			crumbs = (Stack<Crumb>) session.getAttribute(BreadCrumbInterceptor.CRUMB_KEY);
		}				
	}
	
	public boolean start(Writer writer) {
		LOG.debug("BreadCrumb >>> start");
		if ( crumbIterator == null ) {
			crumbIterator = crumbs.iterator();
		}
		
        // get the first
        if ( crumbIterator.hasNext() ) {
            Crumb crumb = crumbIterator.next();
            stack.push(crumb);

            String var = getVar();
            
            if ((var != null) && (crumb != null)) {
                putInContext(crumb);
            }
            
            return true;
        } else {
            super.end(writer, "");
            return false;        	
        }
	}
	
    public boolean end(Writer writer, String body) {
		LOG.debug("BreadCrumb <<< end");
		
		ValueStack stack = getStack();
		
		if (crumbIterator != null)
			stack.pop();
		
		if ( crumbIterator != null && crumbIterator.hasNext() ) {
            Crumb crumb = crumbIterator.next();
            stack.push(crumb);
            
            putInContext(crumb);
            
            return true;
		} else {
            super.end(writer, "");
            return false;			
		}
    }
    
//    public boolean end(Writer writer, String body) {
//        ValueStack stack = getStack();
//        if (iterator != null) {
//            stack.pop();
//        }
//
//        if (iterator!=null && iterator.hasNext()) {
//            Object currentValue = iterator.next();
//            stack.push(currentValue);
//
//            putInContext(currentValue);
//
//            // Update status
//            if (status != null) {
//                statusState.next(); // Increase counter
//                statusState.setLast(!iterator.hasNext());
//            }
//
//            return true;
//        } else {
//            // Reset status object in case someone else uses the same name in another iterator tag instance
//            if (status != null) {
//                if (oldStatus == null) {
//                    stack.getContext().put(statusAttr, null);
//                } else {
//                    stack.getContext().put(statusAttr, oldStatus);
//                }
//            }
//            super.end(writer, "");
//            return false;
//        }
//    }
	
}
