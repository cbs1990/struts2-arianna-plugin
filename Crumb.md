The arianna plugin records the action invocation in beans of type `org.softwareforge.struts2.breadcrumb.Crumb`.

A Crumb has the following properties:

| **Property** | **Type**   | **Description**                         |
|:-------------|:-----------|:----------------------------------------|
| timestamp    | `Date`     | the creation time stamp.                |
| name         | `String`   | the crumb's name.                       |
| action       | `String`   | the name of the action that was invoked.      |
| method       | `String`   | the name of the method that was invoked. |
| namespace    | `String`   | the namespace to which the invoked action belongs to.   |
| params       | `Map`      | the `Map` containing the request's parameters of the original invocation.|