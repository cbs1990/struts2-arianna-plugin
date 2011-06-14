package actions;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;

import comparators.TrueComparator;

@BreadCrumb(value = "OverrideComparatorAction", comparator = TrueComparator.class)
public class OverrideComparatorAction {

	public String execute() {
		return null;
	}
}
