package org.softwareforge.struts2.breadcrumb.showcase.actions;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.SessionAware;
import org.softwareforge.struts2.breadcrumb.ActionComparator;
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;
import org.softwareforge.struts2.breadcrumb.Crumb;
import org.softwareforge.struts2.breadcrumb.NameComparator;
import org.softwareforge.struts2.breadcrumb.RequestComparator;
import org.softwareforge.struts2.breadcrumb.RewindMode;

import com.opensymphony.xwork2.ActionSupport;


public class Controls extends ActionSupport implements SessionAware 
{
	private static final long serialVersionUID = 1L;

	static final Log LOG = LogFactory.getLog(Controls.class);
	
	private Map<String, Object> session;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setSession(Map session) {
		this.session = session;
	}
		
	public BreadCrumbTrail getBreadCrumbTrail() {
		if (session != null)
			return (BreadCrumbTrail) session.get(BreadCrumbInterceptor.CRUMB_KEY);
		
		return null;
	}
	
	
	public Map<String,Comparator<Crumb>> getAllComparators() {
		return (Map<String, Comparator<Crumb>>) session.get("allComparators");
	}
	
	public String populate()
	{
		Map<String,Comparator<?>> allComparators = (Map<String, Comparator<?>>) session.get("allComparators");
		if ( allComparators == null ) {
			allComparators = new HashMap<String, Comparator<?>>();
			allComparators.put("Name Comparator", new NameComparator());
			allComparators.put("Action Comparator", new ActionComparator());
			allComparators.put("Request Comparator", new RequestComparator());
			session.put("allComparators", allComparators);
		}
		return "";
	}
	
	public String getSelectedComparator() {
		Comparator<Crumb> comparator = getBreadCrumbTrail().getComparator();
		for ( Entry<String, Comparator<Crumb>> c : getAllComparators().entrySet() )
		{
			if ( c.getValue().getClass() == comparator.getClass() ) 
				return c.getKey();
		}
		return null;
	}

	String selectedComparator;
	
	public void setSelectedComparator(String selectedComparator) {
		this.selectedComparator = selectedComparator;
	}

	public String clearTrail() {
		// rewinding at -1 is equivalent to a clear
		getBreadCrumbTrail().rewindAt(-1);
		return SUCCESS;
	}
	
	Boolean	clear;
	
	public Boolean isClear() {
		return clear;
	}

	public void setClear(Boolean clear) {
		this.clear = clear;
	}

	// 
	public String reconfigure()
	{
		
		BreadCrumbTrail breadCrumbTrail = getBreadCrumbTrail();
		LOG.info("reconfiguring breadcrumb " + breadCrumbTrail.getName());
		
		Comparator<Crumb> comparator = getAllComparators().get(selectedComparator);
		breadCrumbTrail.setComparator(comparator);
		
		if (clear == Boolean.TRUE) {
			breadCrumbTrail.getCrumbs().clear();
		}
		
		addActionMessage("Breadcrumb has been reconfigured");
		return SUCCESS;
	}
	
}
