package tests;

import java.util.HashMap;

import org.apache.log4j.MDC;

import com.opensymphony.xwork2.ActionProxy;

public class TestInterceptorRedefintion extends BreadcrumbTestCase {

	public void testSimpleAction() throws Exception {
		MDC.put("TEST", "testSimpleAction");
		sessionMap = new HashMap();

		ActionProxy ap1 = getActionProxy("/R/inspect1.do");
		String rc1 = ap1.execute();

		ActionProxy ap2 = getActionProxy("/R/inspect1.do");
		String rc2 = ap2.execute();
	}

	public void testActionRedefinition() throws Exception {
		MDC.put("TEST", "testActionRedefinition");
		sessionMap = new HashMap();

		ActionProxy ap1 = getActionProxy("/R/inspect1.do");
		String rc1 = ap1.execute();

		ActionProxy ap2 = getActionProxy("/R/inspect2.do");
		String rc2 = ap2.execute();
	}

}
