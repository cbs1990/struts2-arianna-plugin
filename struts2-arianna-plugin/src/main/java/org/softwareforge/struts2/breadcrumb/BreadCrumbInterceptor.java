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

 */
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;


/**
 *	@author Giovanni Tosto
 *  @version $Id$
 */
public class BreadCrumbInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(BreadCrumbInterceptor.class);

	private static final String TIMER_KEY = "BreadCrumbInterceptor::intercept";
	
	public static final String CRUMB_KEY = BreadCrumbInterceptor.class.getName() + ":CRUMBS";

	static final Object	LOCK = new Object();
	
	/**
	 * The default breadcrumb trail
	 */
	BreadCrumbTrail	trail = new BreadCrumbTrail();
	
	
	public BreadCrumbTrail getTrail() {
		return trail;
	}
		
	/**
	 * if set to true (the default) the interceptor will catch any RuntimeException raised by its internal methods.
	 * 
	 * This is primarily intended for internal use. 
	 */
	private boolean	catchInternalException = true;
	
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception 
	{
		//UtilTimerStack.push(TIMER_KEY);
		
		/*
		 * Register a pre-result listener
		 */
		invocation.addPreResultListener(new PreResultListener() {
			
			public void beforeResult(ActionInvocation invocation, String resultCode) {
				try 
				{
					LOG.debug("processing invocation" + invocation + "(rc= " + resultCode +")");
					beforeInvocation(invocation);
				} 
				catch (RuntimeException e) 
				{
					String msg = String.format("Exception in BreadCrumbInterceptor : ", e.getMessage());
					LOG.error(msg, e);					
				}				
			}
		});
		
		return invocation.invoke();
		
//		try 
//		{
//			beforeInvocation(invocation);
//		} 
//		catch (RuntimeException e) 
//		{
//			String msg = String.format("Exception in BreadCrumbInterceptor : ", e.getMessage());
//			LOG.error(msg, e);
//			if ( !catchInternalException) 
//				throw e;
//		} finally {
//			UtilTimerStack.pop(TIMER_KEY);
//		}
//
//		return code;
	}
		
	@SuppressWarnings("unchecked")
	protected BreadCrumbTrail getBreadCrumbTrail(ActionInvocation invocation) 
	{		
		Map session = invocation.getInvocationContext().getSession();
		BreadCrumbTrail bcTrail = (BreadCrumbTrail) session.get(CRUMB_KEY);
		
		/*
		 * TODO make sure to put one breadcrumb trail only
		 */
		if ( bcTrail == null ) 
		{			
			synchronized (LOCK) 
			{
				bcTrail = new BreadCrumbTrail();
				bcTrail.setName("$default");
				bcTrail.setMaxCrumbs(trail.maxCrumbs);
				bcTrail.setRewindMode(trail.rewindMode);
				bcTrail.setComparator(trail.comparator);
				// store trail in session
				session.put(CRUMB_KEY, bcTrail);
				LOG.debug("Stored new BreadCrumbTrail '" + bcTrail.name + "' with key: " + CRUMB_KEY);
			}
		}
		
		return bcTrail;
	}
	
	private	void beforeInvocation(ActionInvocation invocation) 
	{
				
		BreadCrumb annotation = processAnnotation(invocation);
		
		/*
		 * returns crumb or null 		
		 */		
//		return crumb == null ? null 

		/*
		 * overrides rewind mode of this invocation if needed 
		 */
		
		if ( annotation != null ) {
			
			Crumb	current = makeCrumb(invocation,annotation.value());
			
			// get the bread crumbs trail
			BreadCrumbTrail	trail = getBreadCrumbTrail(invocation);

			// set default configuration			
			RewindMode mode = trail.rewindMode;
			int maxCrumbs = trail.maxCrumbs;
			Comparator<Crumb> comparator = trail.comparator;
			
			// TODO override configuration (if needed)
			if ( annotation.rewind() != RewindMode.DEFAULT )
				mode = annotation.rewind();
			
			if ( annotation.comparator() != BreadCrumb.NULL.class ) {
				comparator = createComparator(annotation.comparator());
			}
			
			// The comparator to use
			
			// then set initial condition the crumbs
			Stack<Crumb> crumbs = trail.getCrumbs();
			
			/*
			 * synchronized region is needed to prevent ConcurrentModificationException(s)
			 * for concurrent request (operating on the same session) that would modify 
			 * the bread crumbs trail.
			 */
			synchronized (crumbs) {
				LOG.debug("aquired lock on crumbs trail");
				
				Crumb last = (crumbs.size() == 0) ? null : crumbs.lastElement();
				
				/*
				 *  compare current and last crumbs				
				 */
				if ( comparator.compare(current, last) != 0 ) 
				{										
					int	dupIdx = trail.indexOf(current, comparator);
	
					if ( mode == RewindMode.AUTO && dupIdx != -1 ) 
					{
						trail.rewindAt(dupIdx-1);
					}
					
					crumbs.push(current);					
					
					if ( crumbs.size() > maxCrumbs )
						crumbs.remove(0);
						
				} else {
					if ( crumbs.size() > 0) {
						crumbs.remove(crumbs.size()-1);
						crumbs.push(current);
					}
				}
				LOG.debug("releasing lock on crumbs trail");
			} // synchronized
		}
				
	}
	
	private Comparator<Crumb>	createComparator(Class clazz) {
		try {
			Comparator instance = (Comparator) clazz.newInstance();
			return instance; 
		} catch (InstantiationException e) {
			LOG.error("Cannot create comparator of class " + clazz,e);
		} catch (IllegalAccessException e) {
			LOG.error("Cannot create comparator of class " + clazz,e);	
		}
		return null;
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static BreadCrumb	processAnnotation(ActionInvocation invocation)
	{

		Class aclass = invocation.getAction().getClass();
		
		String methodName = invocation.getProxy().getMethod();
		if (methodName == null)
			methodName = "execute";
		
		
		Method method = Utils.findMethod(aclass, methodName);
		BreadCrumb crumb = null;
		
		/*
		 * Check if it is an annotated method 
		 */
		if ( method != null ) {
			crumb = method.getAnnotation(BreadCrumb.class);
		}
		
		/*
		 * Check if we have an annotated class
		 */
		if (crumb == null) {
			crumb = (BreadCrumb) aclass.getAnnotation(BreadCrumb.class);			
		}

		return crumb;
	}
	
	private static Crumb	makeCrumb(ActionInvocation invocation, String name)
	{
		ActionProxy proxy = invocation.getProxy();
		
		Crumb c = new Crumb();
		c.timestamp = new Date();
		c.namespace = proxy.getNamespace();
		c.action = proxy.getActionName();
		c.method = proxy.getMethod();
		
		// evaluate name 
		if ( name.startsWith("%{") && name.endsWith("}") ) {
			ValueStack vstack = invocation.getStack();
			Object value = vstack.findValue(name.substring(2, name.length()-1));
			name = "" + value;		// avoid NPE
		}
		c.name = name;
		
		// store request parameters
//		c.params = new HashMap<String, Object>(invocation.getInvocationContext().getParameters());
		c.params = invocation.getInvocationContext().getParameters();
		
		return c;
	}
	

}
