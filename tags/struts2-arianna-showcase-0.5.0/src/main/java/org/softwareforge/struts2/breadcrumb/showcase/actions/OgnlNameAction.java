package org.softwareforge.struts2.breadcrumb.showcase.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;


public class OgnlNameAction extends ShowcaseAction {

	private String name;
	
	@RequiredStringValidator(message = "a name is required")
	@RequiredFieldValidator(message="a crumb name is required !")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@BreadCrumb("%{name}")	
	public String execute()
	{
		return "success";
	}
	
}
