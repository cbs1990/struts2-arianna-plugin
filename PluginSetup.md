To begin to use the arianna plugin just download the plugin ([available here](http://code.google.com/p/struts2-arianna-plugin/downloads/list)) and put the jar into your application **/WEB-INF/lib** directory.

The plugin core functionalties are provided by an interceptor which needs to be added to your interceptor stack.
Define the interceptor into your struts<sup>2</sup> package
```
<interceptor name="breadCrumbs" class="org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor">
  <param name="defaultRewindMode">NEVER</param>
  <param name="defaultComparator">org.softwareforge.struts2.breadcrumb.NameComparator</param>
</interceptor>
```

and add it to your interceptor stack, i.e.
```
<interceptor-stack name="myStack">
  <interceptor-ref name="defaultStack"/>
  <interceptor-ref name="breadCrumbs"/>
</interceptor-stack>
```

<div>
<blockquote><h3>Note</h3>
You could have very different (strange) behavior depending on where the <code>BreadCrumbInterceptor</code> is placed, however putting it after all standard interceptor is the best choice for almost all cases.<br>
</div></blockquote>

Once the interceptor is part of your interceptor's stack, you can just annotate with `@BreadCrumb` the actions and/or methods that you want to be tracked.

You can have several interceptor instance for different package each on with its own defaults, and they will be applied to all actions intercepted by that instance. However interceptor parameters are just defaults, so they can be overrided on an per-action basis using the [@BreadCrumb](Breadcrumb.md) annotation.

<div>
<blockquote><h3>Note</h3>
Avoid to set more than one <code>BreadCrumbInterceptor</code> instance on a single stack as this could lead to unpredictable behaviour (see also <a href='https://code.google.com/p/struts2-arianna-plugin/issues/detail?id=1'>Issue 1</a>).<br>
</div></blockquote>

To set the maximum number of bread crumbs the plugin should store, you have to set a global constant in **struts.xml** file
```
<constant name="arianna:maxCrumbs" value="6"></constant>
```

that value is shared across all interceptors' instances.