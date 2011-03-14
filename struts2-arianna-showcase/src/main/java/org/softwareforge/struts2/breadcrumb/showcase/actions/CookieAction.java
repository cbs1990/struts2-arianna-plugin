package org.softwareforge.struts2.breadcrumb.showcase.actions;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

public class CookieAction  implements ServletRequestAware, ServletResponseAware {

	private HttpServletRequest request;
	private HttpServletResponse response;

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) 
	{
		this.response = response;
	}
	
	public String execute() 
	{
		Cookie c = new Cookie("my__utmv", "111872281.|2=myself_home=home=2,");
		c.setDomain(".google.com");
		c.setPath("/");
		c.setMaxAge(1*60*60*24*365*2);
		response.addCookie(c);
		return "success";
		
	}

}
