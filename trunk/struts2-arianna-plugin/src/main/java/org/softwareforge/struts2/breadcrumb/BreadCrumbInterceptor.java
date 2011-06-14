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

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;

/**
 * This is the interceptor that detects if we are executing an annotated action.
 * 
 * 
 * 
 * @author Giovanni Tosto
 * @version $Id$
 */
public class BreadCrumbInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    private static final Log LOG = LogFactory.getLog(BreadCrumbInterceptor.class);

    private static final String TIMER_KEY = "BreadCrumbInterceptor: ";

    public static final String CRUMB_KEY = BreadCrumbInterceptor.class.getName() + ":CRUMBS";

    static final Object LOCK = new Object();

    
    @Override
    public void init() {
	LOG.debug("Initializing " + this);
    }

    RewindMode	defaultRewindMode = RewindMode.AUTO;
    
    Comparator<Crumb>	defaultComparator = new NameComparator();
    
    public RewindMode getDefaultRewindMode() {
        return defaultRewindMode;
    }

    public void setDefaultRewindMode(RewindMode defaultMode) {
        this.defaultRewindMode = defaultMode;
    }

    public Comparator<Crumb> getDefaultComparator() {
        return defaultComparator;
    }

    public void setDefaultComparator(Comparator<Crumb> comparator) {
        this.defaultComparator = comparator;
    }

    @Inject("arianna")
    AriannaPlugin	plugin;
    
    
    @Deprecated
    public BCLegacy getTrail() {
	return new BCLegacy() {	    
	    public void setRewindMode(RewindMode mode) {
		defaultRewindMode = mode;
	    }
	    
	    public void setComparator(String classname) {
		Comparator<Crumb> comparator = plugin.lookupComparatorByClass(classname);
		defaultComparator = comparator;
	    }

	    public void setMaxCrumbs(int max) {
		LOG.warn("!!! Setting maxCrumbs via legacy parameter !!!"
			+ " You should use arianna:maxCrumbs instead."
			+ "\n maxCrumb is now a global parameter. Setting it at"
			+ " interceptors level may lead to issues.");
		plugin.setMaxCrumbs("" + max);
	    }
	};
    }

    /**
     * if set to true (the default) the interceptor will catch any
     * RuntimeException raised by its internal methods. This is primarily
     * intended for debugging purpose.
     * 
     */
    private boolean catchInternalException = true;

    public boolean isCatchInternalException() {
	return catchInternalException;
    }

    public void setCatchInternalException(boolean catchInternalException) {
	this.catchInternalException = catchInternalException;
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
	
//	UtilTimerStack.push(TIMER_KEY);
	final BreadCrumb annotation = processAnnotation(invocation);

	if (annotation != null) {
	    
	    if (!annotation.afterAction()) {
		/*
		 * Execute logic now
		 */
		doIntercept(invocation, annotation);
	    } else {
		/*
		 * Register a pre result listener
		 */
		LOG.debug("registering PreResultListener");
		invocation.addPreResultListener(new PreResultListener() {
		    public void beforeResult(ActionInvocation invocation, String resultCode) {
			doIntercept(invocation, annotation);
		    }
		});
	    }
	}

	try {
	    return invocation.invoke();
	} finally {
//	    UtilTimerStack.pop(TIMER_KEY);
	}
    }
    /**
     * Retrieve BreadCrumbTrail from session.
     * 
     * @param invocation
     * @return
     */
    @SuppressWarnings("unchecked")
    protected BreadCrumbTrail getBreadCrumbTrail(ActionInvocation invocation) {
	
	Map session = invocation.getInvocationContext().getSession();
	BreadCrumbTrail bcTrail = (BreadCrumbTrail) session.get(CRUMB_KEY);
	
	/*
	 * TODO make sure to put one breadcrumb trail only
	 */
	if (bcTrail == null) {
	    synchronized (LOCK) {
		bcTrail = new BreadCrumbTrail();
		bcTrail.setName("$arianna");
		bcTrail.setMaxCrumbs(plugin.maxCrumbs);
		// store trail in session
		session.put(CRUMB_KEY, bcTrail);
		LOG.debug("Stored new BreadCrumbTrail '" + bcTrail.name
			+ "' with key: " + CRUMB_KEY);
	    }
	}

	return bcTrail;
    }

    private void doIntercept(ActionInvocation invocation, BreadCrumb annotation) {
	UtilTimerStack.push(TIMER_KEY + "doIntercept");

	if (annotation != null) {

	    Crumb current = makeCrumb(invocation, annotation.value());

	    // get the bread crumbs trail
	    BreadCrumbTrail trail = getBreadCrumbTrail(invocation);

	    // Retrieve default configuration
	    RewindMode mode = defaultRewindMode;
	    Comparator<Crumb> comparator = defaultComparator;
	    int maxCrumbs = trail.maxCrumbs;

	    /*
	     * override defaults (if required)
	     */
	    if (annotation.rewind() != RewindMode.DEFAULT)
		mode = annotation.rewind();

	    if (annotation.comparator() != BreadCrumb.NULL.class) {
		comparator = lookupComparator(annotation.comparator());
	    }

	    /*
	     * Retrieve stored crumbs and synchronize on it.
	     * 
	     * synchronized region is needed to prevent
	     * ConcurrentModificationException(s) for requests (operating on the
	     * same session) that want modify the bread crumbs trail.
	     */

	    Stack<Crumb> crumbs = trail.getCrumbs();

	    synchronized (crumbs) {
		LOG.trace("aquired lock on crumbs " + crumbs);

		Crumb last = (crumbs.size() == 0) ? null : crumbs.lastElement();

		/*
		 * compare current and last crumbs
		 */
		if (comparator.compare(current, last) != 0) {
		    int dupIdx = trail.indexOf(current, comparator);

		    if (mode == RewindMode.AUTO && dupIdx != -1) {
			trail.rewindAt(dupIdx - 1);
		    }

		    crumbs.push(current);

		    if (crumbs.size() > maxCrumbs)
			crumbs.remove(0);

		} else {
		    if (crumbs.size() > 0) {
			crumbs.remove(crumbs.size() - 1);
			crumbs.push(current);
		    }
		}
		LOG.trace("releasing lock on crumbs");
	    } // synchronized
	}
	UtilTimerStack.pop(TIMER_KEY + "doIntercept");
    }

    private Comparator<Crumb> lookupComparator(Class clazz) {
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static BreadCrumb processAnnotation(ActionInvocation invocation) {
	UtilTimerStack.push(TIMER_KEY + "processAnnotation");

	Class aclass = invocation.getAction().getClass();

	String methodName = invocation.getProxy().getMethod();
	if (methodName == null)
	    methodName = "execute";

	Method method = Utils.findMethod(aclass, methodName);
	BreadCrumb crumb = null;

	/*
	 * Check if it is an annotated method
	 */
	if (method != null) {
	    crumb = method.getAnnotation(BreadCrumb.class);
	}

	/*
	 * Check if we have an annotated class
	 */
	if (crumb == null) {
	    crumb = (BreadCrumb) aclass.getAnnotation(BreadCrumb.class);
	}
	
	UtilTimerStack.pop(TIMER_KEY + "processAnnotation");

	return crumb;
    }

    private static Crumb makeCrumb(ActionInvocation invocation, String name) {
	ActionProxy proxy = invocation.getProxy();

	Crumb c = new Crumb();
	c.timestamp = new Date();
	c.namespace = proxy.getNamespace();
	c.action = proxy.getActionName();
	c.method = proxy.getMethod();

	// evaluate name
	if (name.startsWith("%{") && name.endsWith("}")) {
	    ValueStack vstack = invocation.getStack();
	    Object value = vstack
		    .findValue(name.substring(2, name.length() - 1));
	    name = "" + value; // avoid NPE
	}
	c.name = name;

	// store request parameters
	c.params = invocation.getInvocationContext().getParameters();

	return c;
    }

    /** Interface to allow interceptor configuration using the legacy syntax
     * 
     * @author Giovanni Tosto
     */
    public static interface BCLegacy {
	public void setRewindMode(RewindMode mode);
	public void setComparator(String classname);
	public void setMaxCrumbs(int max);
    }
}