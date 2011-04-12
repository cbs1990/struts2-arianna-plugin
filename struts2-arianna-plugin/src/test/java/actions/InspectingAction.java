package actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.InterceptorMapping;
import com.opensymphony.xwork2.interceptor.Interceptor;

@BreadCrumb("inspect")
public class InspectingAction {

    static final Log LOG = LogFactory.getLog(InspectingAction.class);
    
    public	BreadCrumbInterceptor getBreadCrumbInterceptor() {
	ActionContext ac = ActionContext.getContext();
	ActionConfig config = ac.getActionInvocation().getProxy().getConfig();
	
	for (InterceptorMapping mapping : config.getInterceptors() ) {
	    Interceptor interceptor = mapping.getInterceptor();
//	    LOG.debug("[I] " + mapping.getName());
	    if ( interceptor instanceof BreadCrumbInterceptor ) {
		return (BreadCrumbInterceptor) interceptor;
	    }
	}
	
	return null;
    }
    
    public String execute() throws Exception {
	
	LOG.info("executing action: " + InspectingAction.class.getName());
	
	BreadCrumbInterceptor interceptor = getBreadCrumbInterceptor();
	LOG.info("found interceptor " + interceptor);
	
	return null;
    }

}
