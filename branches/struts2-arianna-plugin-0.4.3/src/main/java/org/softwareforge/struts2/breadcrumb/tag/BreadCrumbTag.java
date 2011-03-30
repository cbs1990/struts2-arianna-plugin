/*
 *  Copyright 2011 - Giovanni Tosto
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */   
package org.softwareforge.struts2.breadcrumb.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;
import org.apache.struts2.components.IteratorComponent;
import org.apache.struts2.views.jsp.ComponentTagSupport;
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author Giovanni Tosto
 * @version $Id$
 */
public class BreadCrumbTag extends ComponentTagSupport {
	
	private static final long serialVersionUID = 1L;
	
	// var attribute
    private String var;
    
    public void setVar(String var) {
        this.var = var;
    }
    
    public void setId(String id) {
        setVar(id);
    }
    
	// Status attribute
	private String	status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		IteratorComponent ic = new IteratorComponent(stack);
		return ic;
	}

    @Override
	protected void populateParams() {
		super.populateParams();
		
        IteratorComponent c = (IteratorComponent) getComponent();        
        c.setId(var);
		c.setValue("#session['" + BreadCrumbInterceptor.CRUMB_KEY + "'].crumbs");
		c.setStatus(status);
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
