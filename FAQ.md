Frequently Asked Questions


## How can I reset the bread crumb trail ? ##

Just implement the `SessionAware` interface in your action then retrieve the `BreadCrumbTrail` from the session and rewind the trail to the position **-1**.
For example:
```

import org.apache.struts2.interceptor.SessionAware;
import org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;

public class RewindAction implements SessionAware {
    private Map<String, Object> session;

    public void setSession(Map session) {
        this.session = session;
    }

    public String execute() {

        // retrieve the bread crumb trail from session
        BreadCrumbTrail trail = (BreadCrumbTrail) session.get(BreadCrumbInterceptor.CRUMB_KEY);

        // rewinding at position -1 has the effect of clearing the entire trail
        trail.rewindAt(-1);

        return "success";
    }
}
```

## Can I change breadcrumb maximum crumbs at runtime ? ##

**Yes**, get the `BreadCrumTrail` from the session and call the `setMaxCrumbs` method.