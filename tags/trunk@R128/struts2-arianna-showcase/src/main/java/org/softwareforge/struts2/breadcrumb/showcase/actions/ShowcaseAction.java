package org.softwareforge.struts2.breadcrumb.showcase.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;

public abstract class ShowcaseAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	public ActionInvocation getActionInvocation() {
		 return ActionContext.getContext().getActionInvocation();
	}
	
	public String getSource() throws IOException {
		String name = getClass().getCanonicalName();
		String resource = "/" + name.replace('.', '/') +  ".java";
		InputStream stream = getClass().getResourceAsStream(resource);
		
		InputStreamReader reader = new InputStreamReader(stream);		
		StringWriter wrt = new StringWriter();
		

		char[] buffer = new char[1024];
		int len = 0;
		do {
			len = reader.read(buffer);
			wrt.write(buffer, 0, len);
		} while (len == 1024);
		
		return wrt.toString();
	}
	
	protected void addMessage()
	{
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement element = stackTrace[2];
		
		String msg = String.format("Invoked method %s.%s at line:%d", element.getClassName(), element.getMethodName(), element.getLineNumber());
		addActionMessage(msg);
	}
	
}
