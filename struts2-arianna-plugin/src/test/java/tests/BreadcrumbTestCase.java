package tests;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.StrutsTestCase;
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;

import com.opensymphony.xwork2.ActionProxy;

public abstract class BreadcrumbTestCase extends StrutsTestCase {

	protected Map<String, Object> sessionMap;

	protected BreadCrumbTrail getBreadCrumbTrail(Map sessionMap) {
		Object object = sessionMap.get(BreadCrumbInterceptor.CRUMB_KEY);
		assertTrue("key " + BreadCrumbInterceptor.CRUMB_KEY + " is not a BreadCrumbTrail", object instanceof BreadCrumbTrail);
		return (BreadCrumbTrail) object;
	}

	// ConfigurationProvider p = new StrutsXmlConfigurationProvider("pippo",
	// true, null);

	@Override
	protected void setupBeforeInitDispatcher() throws Exception {
		final String CONFIG_PATHS = "struts-default.xml,struts-plugin.xml,test-struts.xml";
		dispatcherInitParams = new HashMap();
		dispatcherInitParams.put("config", CONFIG_PATHS);
	}

	/**
	 * Override getActionProxy so that we can initialize a session map.
	 * 
	 */
	@Override
	protected ActionProxy getActionProxy(String uri) {
		// TODO Auto-generated method stub
		ActionProxy proxy = super.getActionProxy(uri);
		proxy.getInvocation().getInvocationContext().setSession(sessionMap);

		return proxy;
	}

}
