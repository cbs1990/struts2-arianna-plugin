# Overview #

The **struts<sup>2</sup>-arianna-plugin** is a simple but quite flexible plugin to render and manage a simple breadcrumb navigational bar.
It keep track of the executed action (storing them into session) to allow rendering of the breadcrumb.

## Features ##
  * An `@BreadCrumb` annotation to mark actions or methods that needs to be tracked
  * a simple breadcrumb tag to iterate over stored crumbs
  * configurable breadcrumb behaviour

## Requirements ##
  * Struts 2.1.2+
  * Java 1.5+

The plugin make use of Java5 annotation to select which action should be tracked, so you need java 5;

## Installation ##
Just download the plugin from [here](http://code.google.com/p/struts2-arianna-plugin/downloads/list) and put the jar into your application's `/WEB-INF/lib` directory.

## Usage ##
The breadcrumb functionality is provided by an interceptor which needs to be added to your action's interceptor stack.

```
<interceptor name="breadCrumbs" class="org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor">
  <param name="trail.maxCrumbs">6</param>
  <param name="trail.rewindMode">AUTO</param>							
  <param name="trail.comparator">org.softwareforge.struts2.breadcrumb.NameComparator</param>  
</interceptor>
```

You can configure the interceptor with the maximum number of crumbs to track,
how it should handle duplicated crumbs (`rewind` parameter)
and the default comparator to use.
Once the breadCrumbs interceptor is part of your interceptor's stack, just annotate with `@BreadCrumb` the actions and/or methods that you need to track.

To render the bread crumbs in your page you can use the `breadcrumbs` tag
```
<%@ taglib prefix="bc" uri="/struts-arianna-tags" %>
  ...
<bc:breadcrumbs var='c' status='s'>
  <s:url id='url' action="%{action}" namespace="%{namespace}" method="%{method}" />
  <s:a href='%{url}'><s:property value='name'/></s:a>
</bc:breadcrumbs>
  ...
```

For a complete description of parameters see [parameters](parameters.md)


## Examples ##

### Annotating Actions classes ###

```
@BreadCrumb("My Action")
public class MyAction {

  public String execute() {
    // action's code
    ...
  }
  
```

when struts executes `MyAction` a _crumb_ with name "My Action" will be added to the application's breadcrumb.


### Annotating Class' methods ###

You can also annotate single methods of a class as in the following example:
```
public class ActionClass {

  public String notTrackedMethod() {
    // action's code
    ...
  }

  @BreadCrumb("Action A")
  public String aMethodA() {
    // action's code
    ...
  }
	
  @BreadCrumb("Action B")
  public String aMethodB() {
    // action's code
    ...
  }

```

when the actions mapped to `aMethod1` or `aMethod2` are called a _crumb_ with name "Action A" or "Action B" (respectively) will be added to the breadcrumb.

### Breadcrumbs name as OGNL expression ###
```
public class ActionClass {

  public String getCrumbName() {
    return "Hello";
  }

  @BreadCrumb("%{crumbName}")
  public String aMethodA() {
    // action's code
    ...
  }
```

when using the `%{ ... }` syntax the plugin will evaluate the OGNL expression against the action's value stack to get the _crumb_ name, in this case, a crumb with name _Hello_ will be added to the application's breadcrumb.
<div>
<h5>NOTE</h5>
Actually the expression evaluation take place <b>before</b> the action invokation. This behavior will probably change in future releases.<br>
</div>

### Overriding default configuration ###
some configuration parameters can be overwritten on a per action basis.
So for the following action a `RequestComparator` will always be used, without regard the actual plugin configuration is.
```
@BreadCrumb(value="My Crumb", comparator=RequestComparator)
public class ActionClass {

  public String execute() {
    // your code
  }
```

the same is true for the rewind mode
```
@BreadCrumb(value="My Crumb", rewind=NEVER)
public class MyAction {

  public String execute() {
    // your code
  }
```