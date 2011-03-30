package org.softwareforge.struts2.breadcrumb.showcase.actions;

import org.softwareforge.struts2.breadcrumb.ActionComparator;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.softwareforge.struts2.breadcrumb.NameComparator;
import org.softwareforge.struts2.breadcrumb.RequestComparator;
import org.softwareforge.struts2.breadcrumb.RewindMode;

import com.opensymphony.xwork2.ActionSupport;

import static com.opensymphony.xwork2.ActionSupport.SUCCESS;

public class OverridingActions extends ShowcaseAction {
	
	@BreadCrumb("Test")
	public String useTest() {
		return SUCCESS;
	}
	
	@BreadCrumb(
		value="Test", 
		rewind=RewindMode.NEVER)
	public String useRewindModeNEVER() {
		return SUCCESS;
	}
	
	@BreadCrumb(
		value="Test", 
		rewind=RewindMode.AUTO)
	public String useRewindModeAUTO() {
		return SUCCESS;
	}
	
	@BreadCrumb(
		value="Test", 
		comparator=NameComparator.class)
	public String useNameComparator() {
		return SUCCESS;
	}
	
	@BreadCrumb(
		value="Test", 
		comparator=ActionComparator.class)
	public String useActionComparator() {
		return SUCCESS;
	}
	
	@BreadCrumb(
		value="Test",
		comparator=RequestComparator.class)
	public String useRequestComparator() {
		return SUCCESS;
	}
	
}
