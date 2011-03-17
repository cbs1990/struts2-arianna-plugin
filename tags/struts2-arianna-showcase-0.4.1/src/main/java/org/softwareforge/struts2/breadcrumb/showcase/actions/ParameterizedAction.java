package org.softwareforge.struts2.breadcrumb.showcase.actions;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.softwareforge.struts2.breadcrumb.RewindMode;

import com.opensymphony.xwork2.ActionSupport;

@BreadCrumb("P-Action")
public class ParameterizedAction extends ShowcaseAction 
{
	String	p;

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}
	
}
