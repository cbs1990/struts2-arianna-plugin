package tests;

import java.util.HashMap;

import org.apache.struts2.StrutsTestCase;
import org.apache.struts2.config.StrutsXmlConfigurationProvider;
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.config.ConfigurationProvider;

public class BasicTests extends BreadcrumbTestCase {

		
	public void testSimpleAction() throws Exception {
		
		HashMap sessionMap = new HashMap();
		
		ActionProxy proxy = getActionProxy("/c-SimpleAction.do");
		
		proxy.getInvocation().getInvocationContext().setSession(sessionMap);
		
		String rc = proxy.execute();
		
		assertTrue(sessionMap.size() > 0);
		BreadCrumbTrail breadCrumbTrail = getBreadCrumbTrail(sessionMap);
		assertTrue(breadCrumbTrail.getCrumbs().size() > 0);
		
		System.out.printf("crumbs: %s", breadCrumbTrail.getCrumbs());
	}
	
}
