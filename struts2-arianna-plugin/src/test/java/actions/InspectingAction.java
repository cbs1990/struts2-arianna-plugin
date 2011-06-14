package actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.InterceptorMapping;
import com.opensymphony.xwork2.interceptor.Interceptor;

@BreadCrumb("inspect")
public class InspectingAction {

	static final Log LOG = LogFactory.getLog(InspectingAction.class);

	public List<BreadCrumbInterceptor> getBreadCrumbInterceptor() {

		ActionConfig config = ActionContext.getContext().getActionInvocation().getProxy().getConfig();
		List<BreadCrumbInterceptor> interceptors = new ArrayList<BreadCrumbInterceptor>();

		for (InterceptorMapping mapping : config.getInterceptors()) {
			Interceptor interceptor = mapping.getInterceptor();
			// LOG.debug("[I] " + mapping.getName());
			if (interceptor instanceof BreadCrumbInterceptor) {
				// return (BreadCrumbInterceptor) interceptor;
				interceptors.add((BreadCrumbInterceptor) interceptor);
			}
		}

		return interceptors;
	}

	public String execute() throws Exception {

		ActionProxy aproxy = ActionContext.getContext().getActionInvocation().getProxy();

		LOG.info("executing action: " + aproxy.getActionName());

		List<BreadCrumbInterceptor> interceptors = getBreadCrumbInterceptor();
		LOG.info("found interceptor(s) " + interceptors);

		return null;
	}

}
