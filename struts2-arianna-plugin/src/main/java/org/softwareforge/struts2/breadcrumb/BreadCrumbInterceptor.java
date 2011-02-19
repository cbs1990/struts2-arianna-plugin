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
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;


/**
 *	@author Giovanni Tosto
 */
public class BreadCrumbInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(BreadCrumbInterceptor.class);

	private static final String timerKey = "BreadCrumbInterceptor::doIntercept";
	
	public static final String CRUMB_KEY = BreadCrumbInterceptor.class.getName() + ":CRUMBS";
	
	private int maxCrumbs = 4;
	
	private boolean uniqueCrumbs = false;

	private RewindMode rewind = RewindMode.never;
	
	public RewindMode getRewind() {
		return rewind;
	}


	public void setRewind(RewindMode rewindMode) {
		this.rewind = rewindMode;
	}


	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception 
	{
		try 
		{
			UtilTimerStack.push(timerKey);
			processInvocation(invocation);
			String result = invocation.invoke();
			return result;
		} 
		catch (RuntimeException e) 
		{
			String msg = (new StringBuilder()).append("Exception in BreadCrumbInterceptor: ")
					.append(e.getMessage()).toString();
			LOG.error(msg, e);
			throw new Exception(msg, e);
		} 
		finally 
		{
			UtilTimerStack.pop(timerKey);
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
	
	private	void processInvocation(ActionInvocation invocation) 
	{
		
		// recupera le briciole 
		Stack<Crumb> crumbs = getCrumbs(invocation);
		
		Crumb current = processAnnotation(invocation);
		
		if ( current != null ) {
			Crumb last = (crumbs.size() > 0) ? crumbs.lastElement() : null;
			
			// confronta la richiesta corrente con l'ultima briciola
			if ( !current.equals(last) ) {
				
				int	dupIdx = crumbs.indexOf(current);
//				boolean isNew = uniqueCrumbs && dupIdx > 0;

				// rewind breadcrumb
				if ( rewind == RewindMode.auto && dupIdx != -1) {
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
	
	private void	rewind() {
		
	}
	
	private Crumb	processAnnotation(ActionInvocation invocation)
	{
		Class aclass = invocation.getAction().getClass();
		
		String methodName = invocation.getProxy().getMethod();
		if (methodName == null)
			methodName = "execute";
		
		
		Method method = ReflectionUtils.findMethod(aclass, methodName);
		BreadCrumb crumb = null;
		if ( method != null ) {
			crumb = method.getAnnotation(BreadCrumb.class);
		} else {
			// prova a cercare nella classe
			crumb = (BreadCrumb) aclass.getAnnotation(BreadCrumb.class);
		}
		
		if (crumb != null && !"".equals(crumb.value()) ) {
			return makeCrumb(invocation,crumb.value());
		}
		
		return null;
	}
	
	private Crumb	makeCrumb(ActionInvocation invocation, String name)
	{
		ActionProxy proxy = invocation.getProxy();
		
		Crumb c = new Crumb();
		
		c.namespace = proxy.getNamespace();
		c.action = proxy.getActionName();
		c.method = proxy.getMethod();
		
		if (name == null) {
			name = c.getFullyQualifiedId();
		}
		c.name = name;
		
		return c;
	}
	
//	private String	getActionId(ActionInvocation invocation) {
//		ActionProxy proxy = invocation.getProxy();
//		String namespace = proxy.getNamespace();
//		String action = proxy.getActionName();
//		String method = proxy.getMethod();
//		
//		return namespace + "." + action + ":" + method;
//	}
	
	// --------------------- getter && setter ------------------------
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


	public boolean isUniqueCrumbs() {
		return uniqueCrumbs;
	}


	public void setUniqueCrumbs(boolean allowDuplicates) {
		this.uniqueCrumbs = allowDuplicates;
	}

	
}
