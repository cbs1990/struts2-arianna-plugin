package tests;

import java.util.HashMap;

import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;

import com.opensymphony.xwork2.ActionProxy;

public class BasicTests extends BreadcrumbTestCase {

	public void testSimpleAction() throws Exception {

		sessionMap = new HashMap();

		ActionProxy proxy = getActionProxy("/c-SimpleAction.do");

		String rc = proxy.execute();

		BreadCrumbTrail breadCrumbTrail = getBreadCrumbTrail(sessionMap);

		/*
		 * assert that a breadcrumb trail has been stored and that it contains
		 * at least a crumb
		 */
		assertNotNull("No breadcrumb trail in session", breadCrumbTrail);
		assertTrue(breadCrumbTrail.getCrumbs().size() > 0);

		System.out.printf("crumbs: %s", breadCrumbTrail.getCrumbs());
	}

}
