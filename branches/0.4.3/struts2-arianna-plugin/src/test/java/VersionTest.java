import org.softwareforge.struts2.breadcrumb.Version;


public class VersionTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String title = Version.getTitle();
		String version = Version.getVersion();
		
		System.out.printf("%s : %s", title,version);
	}

}
