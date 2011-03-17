package org.softwareforge.struts2.breadcrumb.showcase.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;

import com.opensymphony.xwork2.ActionSupport;


public class SimpleActions extends ShowcaseAction {

		
	@BreadCrumb("Simple 1")
	public String simple1() {
		return SUCCESS;		
	}
	
	@BreadCrumb("Simple 2")
	public String simple2() {
		return SUCCESS;		
	}
	
	@BreadCrumb("Simple 3")
	public String simple3() {
		return SUCCESS;		
	}

	@BreadCrumb("Simple 4")
	public String simple4() {
		return SUCCESS;		
	}
	
	@BreadCrumb("Simple 5")
	public String simple5() {
		return SUCCESS;		
	}	
}
