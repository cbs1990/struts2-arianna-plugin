package org.softwareforge.struts2.breadcrumb.showcase.actions;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.softwareforge.struts2.breadcrumb.RewindMode;

//@BreadCrumb(value="Home",rewind=RewindMode.NEVER)
public class HomeAction extends ShowcaseAction 
{
	public String execute() {
		return SUCCESS;
	}
	
	/*
	 *	action invoked to ensure BreadCrumbTrail initialization  
	 */
	@BreadCrumb("Home")
	public String start() {
		return SUCCESS;
	}
	
}
