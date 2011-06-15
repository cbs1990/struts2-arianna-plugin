package tests;

import junit.framework.Assert;

import org.softwareforge.struts2.breadcrumb.AriannaPlugin;

public class AriannaPluginTest extends BreadcrumbTestCase {

	public void testPluginVersion() {
		AriannaPlugin plugin = configuration.getContainer().getInstance(AriannaPlugin.class, "arianna");

		Assert.assertNotNull("AriannaPlugin NOT FOUND", plugin);

		int maxCrumbs = plugin.getDefaultMaxCrumbs();
		// assertTrue(maxCrumbs == 6);

		String version = plugin.getVersion();
		String name = plugin.getTitle();

		// Assert.assertNotNull(version);
		// Assert.assertNotNull(name);
	}
}
