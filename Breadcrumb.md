The Arianna Plugin use `@BreadCrumb` annotations to detect actions and/or methods that needs to be stored in a breadcrumb trail.

The `@BreadCrumb` annotation can be used both at class or method level, and it can have the following attributes:

| **Element**  | **Type**       | **Required** | **Description** | **Default** |
|:-------------|:---------------|:-------------|:----------------|:------------|
| **value**    | `String`       | **yes**      | crumb name      |             |
| comparator   | `Class<? extends Comparator>` | no           | comparator to use | _interceptor default_ |
| rewind       | `RewindMode`   | no           | rewind mode to use | _interceptor default_ |
| afterAction  | `boolean`      | no           | set to `true` if annotation should be processed _after_ the action invocation | false       |

To declare an action as one to track, by the plugin, you annotate the class as follows
```
@BreadCrumb("My Crumb Name")
public class MyAction {

  public String execute() {
    // action's code
    ...
  }
```

or you can annotate the method that struts will call (by default the execute method)
```
public class MyAction {

  @BreadCrumb("My Crumb Name")
  public String execute() {
    // action's code
    ...
  }
```

when struts<sup>2</sup> invokes `MyAction` a crumb with name "My Crumb Name" is created and appended to the application's breadcrumb bar.

If your actions are mapped to different methods of the same class they  can also be individually marked with different annotations
```
public class ActionClass {

  public String notTrackedMethod() {
    ...
  }

  @BreadCrumb("Action A")
  public String methodA() {
    ...
  }
        
  @BreadCrumb("Action B")
  public String methodB() {
    ...
  }
```
when the actions mapped to `methodA` or `methodB` are called a crumb with name "Action A" or "Action B" (respectively) will be added to the breadcrumb.

### Dynamic crumb names ###

Bread crumbs names can also be OGNL expressions, just use the `%{ ... }` syntax to denote expressions.
```
@BreadCrumb("%{crumbName}")
public class MyAction {

  public String getCrumbName() {
    return "Hello";
  }

  public String execute() {
    ...
  }

```
the plugin will then evaluate the OGNL expression against the action's value stack to get the actual crumb name (i.e a crumb named `Hello` will be added to the application's breadcrumb).

Sometimes you would give a name that is know only after the action execution, in this case setting the _afterAction_ atribute to `true` defer the expression evaluation to the end of the action execution (but before the view rendering).

```
@BreadCrumb(value="%{deferredName}", afterAction=true)
public class MyAction {

  private String deferredName;

  public String getDeferredName() {
    return deferredName;
  }

  public String execute() {
    // action logic
    ...
    // set/update dynamic name
    deferredName = "a name depending from the action logic"
  }
```

### Overriding Default Behaviour ###

If not specified otherwise, each tracked action will use the rewind mode (and comparator class) defined by the breadcrumb interceptor, however both `rewind mode` and comparator can be overrided by any single annotation.
To _force_ an action to have a particular behavior you can use the `rewind` and `comparator` attributes.
```
@BreadCrumb(value="Rewind to my last call", rewind=RewindMode.AUTO, comparator=ActionComparator.class)
public class MyAction {

  public String execute() {
     ...
  }

```