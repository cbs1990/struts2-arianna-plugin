package tests;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsTestCase;
import org.apache.struts2.config.StrutsXmlConfigurationProvider;
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.config.ConfigurationProvider;

public class MultiThreadTests extends BreadcrumbTestCase {

	static final Log	LOG = LogFactory.getLog(MultiThreadTests.class);
	
	static	AtomicInteger thread_counter = new AtomicInteger();
	
	public void testThread1Safety() throws Exception {
		
		HashMap sessionMap = new HashMap();
		
		ActionProxy proxyA = getActionProxy("/t-thread1.do");		
		proxyA.getInvocation().getInvocationContext().setSession(sessionMap);
		
		ActionProxy proxyB = getActionProxy("/t-thread1.do");		
		proxyB.getInvocation().getInvocationContext().setSession(sessionMap);

		// create two thread
		Thread t1 = createActionThread("t1",proxyA);
		Thread t2 = createActionThread("t2",proxyB);
		
		LOG.info("starting threads ...");
		// then run concurrently
		t1.start();
		t2.start();
		
		LOG.info("waiting to rejoin ...");
		while (thread_counter.get() != 2) {
			Thread.sleep(2000);
		}
		LOG.info("rejoined");
		
//		assertTrue(sessionMap.size() > 0);
//		BreadCrumbTrail breadCrumbTrail = getBreadCrumbTrail(sessionMap);
//		assertTrue(breadCrumbTrail.getCrumbs().size() > 0);
//		
//		System.out.printf("crumbs: %s", breadCrumbTrail.getCrumbs());
	}

	protected Thread createActionThread(String name, final ActionProxy proxy) {
		return new Thread(name) {
			
			@Override
			public void run() {
				LOG.info("started ...");
				try {
					String rc = proxy.execute();
					LOG.info("rc = " + rc);
				} catch (Exception e) {
					fail("caught exception " + e.getMessage());
				} finally {
					thread_counter.getAndAdd(1);					
				}
				LOG.info("... done.");
			}
		};
	}
	
}
