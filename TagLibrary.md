At this moment the library is composed by a tag only.

To use it add the following declaration to your JSP.
```
<%@ taglib prefix="bc" uri="/struts-arianna-tags" %>
```

# Tag: `<bc:breadcrumbs>` #

| **Attribute** | **Required** | **Description** |
|:--------------|:-------------|:----------------|
| `var`         | no           | the name used to reference the _crumb_ pushed into the Value Stack |
| `status`      | no           | If specified, an instanceof `IteratorStatus` will be pushed into stack upon each iteration |

This tag just acts like an `<s:iterator>` tag iterating over the breadcrumbs trail. At each iteration it will push into the Value Stack the [Crumb](Crumb.md).

## Examples ##

### simple crumbs ###
```
<%@taglib prefix="bc" uri="/struts-arianna-tags" %>
                     ...
<bc:breadcrumbs var='c' status='s'>
    <s:url id='url' action="%{action}" namespace="%{namespace}" method="%{method}" />
    <s:a href='%{url}'><s:property value='name'/></s:a>
</bc:breadcrumbs>
                     ...
```

### rendering of parameters ###
More often you need to render links together with original request parameters.

Unfortunately there is a well known problem when you need to build urls with arbitrary parameters (see [adding-parameters-from-a-map-to-a-url-tag](http://stackoverflow.com/questions/872375/struts2-adding-parameters-from-a-map-to-a-url-tag) for a discussion and possible solutions).

The following code, however, shows how to build urls using JSTL core tags.
```
<!-- add the jstl core tag library -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                     ...
<bc:breadcrumbs var='c' status='s'>
    <!-- use the usual way to build the url for a struts action -->
    <s:url var="surl" action="%{action}" namespace="%{namespace}" includeContext="false"/>
		
    <!-- use the tags: url and param to effectively build the url -->
    <c:url var="url" value="${surl}">
        <!-- iterate on parameters -->             
        <c:forEach var="p" items="${c.params}">
            <!-- iterate on parameter values -->             
            <c:forEach var="v" items="${p.value}">${v}
                <c:param name="${p.key}" value="${v}"/>
            </c:forEach>
        </c:forEach>
    </c:url>

    <!-- retrieve the url built and render an HTML anchor -->
    <s:a href="%{#attr['url']}">${c.name}</s:a>
</bc:breadcrumbs>
                     ...
```
if you feel the code above too verbose, you can also resort to a tag library available [here](http://www.objectify.be/wordpress/?p=64) specifically designed to add arbitrary parameters to a URL in struts<sup>2</sup>