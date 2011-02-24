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
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;


/**
 *	@author Giovanni Tosto
 *  @version $Id$
 */
public class BreadCrumbInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(BreadCrumbInterceptor.class);

	private static final String TIMER_KEY = "BreadCrumbInterceptor::doIntercept";
	
	public static final String CRUMB_KEY = BreadCrumbInterceptor.class.getName() + ":CRUMBS";

	/**
	 * The maximum crumbs to keep in memory 
	 */
	private int maxCrumbs = 4;
	
	private boolean storeParams = false;
	
	/**
	 *	The <code>default</code> <em>rewind mode</em> to use for actions that not explicitly set <em>rewind mode</em>    
	 */
	private RewindMode rewind = RewindMode.NEVER;
	
	/**
	 * 	if set to true (the default) the interceptor will catch any RuntimeException raised by its internal methods.
	 * 
	 */
	private boolean	catchInternalException = true;
	
	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception 
	{
		UtilTimerStack.push(TIMER_KEY);			
		try 
		{
			beforeInvocation(invocation);
		} 
		catch (RuntimeException e) 
		{
			String msg = (new StringBuilder())
					.append("Exception in BreadCrumbInterceptor.beforeInvocation : ")
					.append(e.getMessage())
					.toString();
			LOG.error(msg, e);
			if ( !catchInternalException) 
				throw e;
		} 
		
		try {
			return invocation.invoke();			
		} finally {
			UtilTimerStack.pop(TIMER_KEY);			
		}
	}
	
	protected Stack<Crumb> getCrumbs(ActionInvocation invocation)
	{
		ActionContext context = invocation.getInvocationContext();
		// recupera le briciole dalla sessione
		Stack<Crumb> crumbs = (Stack<Crumb>) context.getSession().get(CRUMB_KEY);
		if ( crumbs == null) {
			crumbs = new Stack<Crumb>();
			context.getSession().put(CRUMB_KEY, crumbs);
		}
		
		return crumbs;
	}
	
	private	void beforeInvocation(ActionInvocation invocation) 
	{
		
		// recupera le briciole 
		Stack<Crumb> crumbs = getCrumbs(invocation);
		
		Crumb current = processAnnotation(invocation);
		
		if ( current != null ) {
			Crumb last = (crumbs.size() > 0) ? crumbs.lastElement() : null;
			
			// confronta la richiesta corrente con l'ultima briciola
			if ( !current.equals(last) ) {
				
				int	dupIdx = crumbs.indexOf(current);

				// rewind breadcrumb
				if ( rewind == RewindMode.AUTO && dupIdx != -1 ) {
					// riavvolge la breadcrumb alla prima briciola uguale a quella corrente
					for (int i=dupIdx+1, size=crumbs.size(); i<size; i++) {
						crumbs.remove(dupIdx+1);
					}
				} else {
					crumbs.push(current);					
				}
					
				if ( crumbs.size() > maxCrumbs )
					crumbs.remove(0);
					
			}			
		}
				
	}
	
	private Crumb	processAnnotation(ActionInvocation invocation)
	{
//		Map params = null;
		Class aclass = invocation.getAction().getClass();
		
		String methodName = invocation.getProxy().getMethod();
		if (methodName == null)
			methodName = "execute";
		
		
		Method method = ReflectionUtils.findMethod(aclass, methodName);
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
		
		/*
		 * returns crumb or null 		
		 */		
		return crumb == null ? null : makeCrumb(invocation,crumb.value(),storeParams);
	}
	
	private Crumb	makeCrumb(ActionInvocation invocation, String name, boolean storeParams)
	{
		ActionProxy proxy = invocation.getProxy();
		
		Crumb c = new Crumb();
		
		c.namespace = proxy.getNamespace();
		c.action = proxy.getActionName();
		c.method = proxy.getMethod();
		
		if (name == null) {
			name = c.getFullyQualifiedId();
		}
		
		// evaluate name 
		if ( name.startsWith("%{") && name.endsWith("}") ) {
			ValueStack vstack = invocation.getStack();
			Object value = vstack.findValue(name.substring(2, name.length()-1));
			name = "" + value;		// avoid NPE
		}
		c.name = name;
		
		if (storeParams) {			
			Map<String, Object> parameters = invocation.getInvocationContext().getParameters();
			c.params = parameters;
		}
		return c;
	}
	
	// Getters and Setters
	//////////////////////////////////////////////////////////////////
	
	public int getMaxCrumbs() {
		return maxCrumbs;
	}

	public void setMaxCrumbs(int maxCrumbs) {
		if (maxCrumbs < 0) {
			throw new IllegalArgumentException("maxCrumbs must be a non negative integer");
		}
//			LOG.warn("can not set maxCrumbs to "+ maxCrumbs +" it should be a positive number, using default value");
		this.maxCrumbs = maxCrumbs;
	}	
	
	public RewindMode getRewind() {
		return rewind;
	}

	public void setRewind(RewindMode rewindMode) {
		if ( rewindMode == RewindMode.DEFAULT ) {
//			throw new IllegalArgumentException("rewind mode DEFAULT can not be used as a rewind mode for the BreadCrumbInterceptor. Using default value " + rewind.name() + "instead");
			String msg = "rewind mode DEFAULT can not be used as a rewind mode for the BreadCrumbInterceptor. Using default value " + rewind.name() + "instead";
			LOG.warn(msg);
		}
		this.rewind = rewindMode;
	}

	
}
