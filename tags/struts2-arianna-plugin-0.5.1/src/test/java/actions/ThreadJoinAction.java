package actions;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;

public class ThreadJoinAction {

	@BreadCrumb("thread1")
	public String thread1() throws Exception {
		System.out.printf("\nexecuting action: %s\n", ThreadJoinAction.class.getName());
		return null;
	}

	@BreadCrumb(value = "thread2", afterAction = true)
	public String thread2() throws Exception {
		System.out.printf("\nexecuting action: %s\n", ThreadJoinAction.class.getName());
		return null;
	}
}
