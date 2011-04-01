package tests;

import java.util.HashMap;

import org.apache.struts2.StrutsTestCase;
import org.apache.struts2.config.StrutsXmlConfigurationProvider;
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;

import com.opensymphony.xwork2.config.ConfigurationProvider;

public class BreadcrumbTestCase extends StrutsTestCase
{
	protected	final BreadCrumbTrail getBreadCrumbTrail(HashMap sessionMap) {
		Object object = sessionMap.get(BreadCrumbInterceptor.CRUMB_KEY);
		assertTrue("key " + BreadCrumbInterceptor.CRUMB_KEY + " is not a BreadCrumbTrail", object instanceof BreadCrumbTrail);
		return (BreadCrumbTrail) object;
	}

//	ConfigurationProvider	p = new StrutsXmlConfigurationProvider("pippo", true, null);
	
	@Override
	protected void setupBeforeInitDispatcher() throws Exception {
//		final String DEFAULTS = "struts-default.xml,struts-plugin.xml,struts.xml";
//		dispatcherInitParams = new HashMap();
//		dispatcherInitParams.put("config", "struts-default.xml,struts-plugin.xml,struts-test.xml");
    }
	
}
