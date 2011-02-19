package org.softwareforge.struts2.breadcrumb.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ContextBeanTag;

import com.opensymphony.xwork2.util.ValueStack;

public class BreadCrumbTag extends ContextBeanTag {

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {		
		return new BreadCrumbComponent(stack,req);
	}

    public int doEndTag() throws JspException {
        component = null;
        return EVAL_PAGE;
    }
	
    public int doAfterBody() throws JspException {
        boolean again = component.end(pageContext.getOut(), getBody());

        if (again) {
            return EVAL_BODY_AGAIN;
        } else {
            if (bodyContent != null) {
                try {
                    bodyContent.writeOut(bodyContent.getEnclosingWriter());
                } catch (Exception e) {
                    throw new JspException(e.getMessage());
                }
            }
            return SKIP_BODY;
        }
    }
    
}
