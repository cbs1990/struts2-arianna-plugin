package actions;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;

@BreadCrumb("simple")
public class SimpleAction {

	public String execute() throws Exception {
		System.out.printf("executing action: ", SimpleAction.class.getName());
		return null;
	}

}
