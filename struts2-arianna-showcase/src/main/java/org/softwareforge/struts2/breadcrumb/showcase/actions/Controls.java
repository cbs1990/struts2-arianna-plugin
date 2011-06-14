package org.softwareforge.struts2.breadcrumb.showcase.actions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.softwareforge.struts2.breadcrumb.ActionComparator;
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;
import org.softwareforge.struts2.breadcrumb.Crumb;
import org.softwareforge.struts2.breadcrumb.NameComparator;
import org.softwareforge.struts2.breadcrumb.RequestComparator;
import org.softwareforge.struts2.breadcrumb.RewindMode;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.InterceptorMapping;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.validator.annotations.ExpressionValidator;

public class Controls extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 1L;

    static final Log LOG = LogFactory.getLog(Controls.class);

    private Map<String, Object> session;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void setSession(Map session) {
	this.session = session;
    }
    
    private Boolean clear = true;

    String comparatorKey;
    
    RewindMode	rewindMode;
             
    Integer	maxCrumbs;
    
    //
    public String reconfigure() {

	BreadCrumbInterceptor interceptor = getBreadCrumbInterceptor();
	if ( interceptor != null ) {
            BreadCrumbTrail breadCrumbTrail = getBreadCrumbTrail();
            LOG.info("reconfiguring trail " + breadCrumbTrail.getName());
            
            LOG.info("maxcrumbs = " + breadCrumbTrail.getMaxCrumbs());
            
            // update maxcrumbs
            breadCrumbTrail.setMaxCrumbs(maxCrumbs);
            
            // update default comparator
            Comparator<Crumb> comparator = getAllComparators().get(comparatorKey);            
	    interceptor.setDefaultComparator(comparator);
	    
	    // update default rewindMode
	    interceptor.setDefaultRewindMode(rewindMode);
	    
	    if (clear == true) {
		breadCrumbTrail.rewindAt(-1);
		LOG.info("breadcrumb trial cleared, size: " + breadCrumbTrail.getCrumbs().size());		
	    }
	    
            addActionMessage("Breadcrumb has been reconfigured");
            
            /*
             * Fix some some strange replication issue when running on 
             * google GAE infrastructure.
             */
            session.put(BreadCrumbInterceptor.CRUMB_KEY, breadCrumbTrail);
            
            LOG.info("stored trail in session" + breadCrumbTrail);		            
            
            return SUCCESS;
	}
	
	return ERROR;
    }
    
    @SkipValidation
    public String clearTrail() {
	// rewinding at -1 is equivalent to a clear
	BreadCrumbTrail trail = getBreadCrumbTrail();
	trail.rewindAt(-1);
	
        /*
         * Fix some some strange replication issue when running on 
         * google GAE infrastructure.
         */	
        session.put(BreadCrumbInterceptor.CRUMB_KEY, trail);
	
	return SUCCESS;
    }

    @SkipValidation
    public String init() {
	Map<String, Comparator<Crumb>> allComparators = (Map<String, Comparator<Crumb>>) session
		.get("allComparators");
	if (allComparators == null) {
	    allComparators = new HashMap<String, Comparator<Crumb>>();
	    allComparators.put("Name Comparator", new NameComparator());
	    allComparators.put("Action Comparator", new ActionComparator());
	    allComparators.put("Request Comparator", new RequestComparator());
	    session.put("allComparators", allComparators);
	}
	
	rewindMode = getBreadCrumbInterceptor().getDefaultRewindMode();
	
	Comparator<Crumb> defaultComparator = getBreadCrumbInterceptor().getDefaultComparator();	
	for (Map.Entry<String,Comparator<Crumb>> entry : allComparators.entrySet() ) {
	    if ( defaultComparator.getClass() == entry.getValue().getClass() ) {
		comparatorKey = entry.getKey();
	    }
	}
	
	return null;
    }
    
    // Helpers and utilities
    ///////////////////////////////////////////////////////////////////////////
    
    /**
     * lookup the BreadCrumbInterceptor
     * 
     * @return
     */
    protected BreadCrumbInterceptor getBreadCrumbInterceptor() {

	ActionConfig config = ActionContext.getContext().getActionInvocation()
		.getProxy().getConfig();
	
	for (InterceptorMapping mapping : config.getInterceptors()) {
	    Interceptor interceptor = mapping.getInterceptor();
	    
	    if (interceptor instanceof BreadCrumbInterceptor && "breadCrumbs".equals(mapping.getName())) {
		return (BreadCrumbInterceptor) interceptor;
	    }
	}

	return null;
    }
    
    /**
     * Retrieve the BreadCrumbTrail
     *  
     * @return
     */
    public BreadCrumbTrail getBreadCrumbTrail() {
	if (session != null)
	    return (BreadCrumbTrail) session
		    .get(BreadCrumbInterceptor.CRUMB_KEY);

	return null;
    }
    
    public Map<String, Comparator<Crumb>> getAllComparators() {
	return (Map<String, Comparator<Crumb>>) session.get("allComparators");
    }
    
    // Others getters and setters
    ///////////////////////////////////////////////////////////////////////////
    
    public String getComparatorKey() {
        return comparatorKey;
    }

    public void setComparatorKey(String comparatorKey) {
        this.comparatorKey = comparatorKey;        
    }
        
    public RewindMode getRewindMode() {
        return rewindMode;
    }

    public void setRewindMode(RewindMode rewindMode) {
        this.rewindMode = rewindMode;
    }

    public Boolean isClear() {
	return clear;
    }

    public void setClear(Boolean clear) {
	this.clear = clear;
    }

    @ExpressionValidator(expression = "maxCrumbs > 0 and maxCrumbs < 20", message="Sorry, in this application, maxCrumbs must be between 1 and 20")
    public Integer getMaxCrumbs() {
        return maxCrumbs;
    }

    public void setMaxCrumbs(Integer maxCrumbs) {
        this.maxCrumbs = maxCrumbs;
    }
    
}
