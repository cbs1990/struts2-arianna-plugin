package org.softwareforge.struts2.breadcrumb.showcase.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;


@Validation
public class OgnlAfterInvocationAction extends ShowcaseAction {

	private String name;
	
	@RequiredStringValidator(message = "a name is required")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@BreadCrumb(value="%{name}", afterAction=true)	
	public String execute()
	{
		// do some funny work
		reverseName();
	
		// wait 300 millis 
//		try {
//		    Thread.sleep(3000);
//		} catch (InterruptedException e) {		    
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
		
		return SUCCESS;
	}
	
	private void reverseName() {
		char[] src = name.toCharArray();
		char[] dest = new char[src.length];
		
		for (int i = 1; i <= src.length; i++) {
			dest[src.length - i] = src[i-1];
		}
		
		name = new String(dest);  
	}
}
