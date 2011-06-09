package org.softwareforge.struts2.breadcrumb.showcase.actions;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.softwareforge.struts2.breadcrumb.RewindMode;

//@BreadCrumb(value="Home",rewind=RewindMode.NEVER)
public class HomeAction extends ShowcaseAction 
{
	@BreadCrumb("Home")
	public String execute() {
	    return SUCCESS;
	}
		
	public	String getPluginInfo() {
	    Package pkg = Package.getPackage("org.softwareforge.struts2.breadCrumb");
	    return String.format( "%s - %s", pkg.getSpecificationTitle(), pkg.getSpecificationVersion());
	}
	
}
