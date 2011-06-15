package tests;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionProxy;

public class MultiThreadTests extends BreadcrumbTestCase {

	static final Log LOG = LogFactory.getLog(MultiThreadTests.class);

	static AtomicInteger thread_counter;

	public void test_ThreadSafety_1() throws Exception {

		sessionMap = new HashMap();

		ActionProxy proxyA = getActionProxy("/t-thread1.do");
		ActionProxy proxyB = getActionProxy("/t-thread1.do");

		// creates two threads
		Thread t1 = createActionThread("t1", proxyA);
		Thread t2 = createActionThread("t2", proxyB);

		thread_counter = new AtomicInteger(2);

		// then run concurrently
		LOG.info("starting threads ...");
		t1.start();
		t2.start();

		LOG.info("waiting to rejoin ...");
		while (thread_counter.get() > 0) {
			Thread.sleep(2000);
		}
		LOG.info("rejoined");

		// assertTrue(sessionMap.size() > 0);
		// BreadCrumbTrail breadCrumbTrail = getBreadCrumbTrail(sessionMap);
		// assertTrue(breadCrumbTrail.getCrumbs().size() > 0);
		//
		// System.out.printf("crumbs: %s", breadCrumbTrail.getCrumbs());
	}

	public void test_ThreadSafety_2() throws Exception {

		sessionMap = new HashMap();

		ActionProxy proxyA = getActionProxy("/t-thread1.do");
		ActionProxy proxyB = getActionProxy("/t-thread2.do");

		// creates two threads
		Thread t1 = createActionThread("t1", proxyA);
		Thread t2 = createActionThread("t2", proxyB);

		thread_counter = new AtomicInteger(2);

		// then run concurrently
		LOG.info("starting threads ...");
		t1.start();
		t2.start();

		LOG.info("waiting to rejoin ...");
		while (thread_counter.get() > 0) {
			Thread.sleep(2000);
		}
		LOG.info("rejoined");

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
					thread_counter.getAndDecrement();
				}
				LOG.info("... done.");
			}
		};
	}

}
