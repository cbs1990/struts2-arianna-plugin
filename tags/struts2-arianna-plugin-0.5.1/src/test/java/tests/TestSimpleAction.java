package tests;

import java.util.HashMap;

import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;

public class TestSimpleAction extends BreadcrumbTestCase {

	@Override
	protected void setUp() throws Exception {
		UtilTimerStack.setActive(true);
		System.setProperty("xwork.profile.activate", "true");
		// TODO Auto-generated method stub
		super.setUp();
	}

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

	public void testConfiguration() throws Exception {

	}
}
