<%@page import="org.softwareforge.struts2.breadcrumb.*"%>
<%@page import="java.util.*"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="bc" uri="/struts-arianna-tags" %>


 
<script type="text/javascript">
	$(document).ready(function() {
		$('.toggler').click(function(){
			var $pre = $(this).next();
			var $icon = $(this).children().first();
//			alert("icon" + $icon + " -- " + $icon.get());
			if ( $icon.hasClass("ui-icon-triangle-1-e") ) {
				$icon.toggleClass("ui-icon-triangle-1-e");
				$icon.toggleClass("ui-icon-triangle-1-s");
				$pre.show();
			} else {
				$icon.toggleClass("ui-icon-triangle-1-e");
				$icon.toggleClass("ui-icon-triangle-1-s");				
				$pre.hide();
			}
		});
	});
</script>
 
<script>hljs.highlightBlock(document.getElementById('source'), '    ', false);</script>
 

<div>
	<s:actionmessage />
	
	<s:set var='invocation' value="actionInvocation" />
	<s:set var='proxy' value="actionInvocation.proxy" />
	<div>
		You invoked the action
		<h3><span>${proxy.namespace}/${proxy.actionName}!${proxy.method}</span></h3>
		<h5 class='toggler'>
			<span class="ui-icon ui-icon-triangle-1-e">
			</span>
			<a href="#">view action code</a>
		</h5>
		<pre id="source" class='source' style='display:none'>
			<code class='java'><s:property value="source" escape="true"/></code>		
		</pre>
	</div>
	
	<h4>Current Trail Stack</h4>
	<p>
		Here you can see the details of the current bread crumb trail.
		(last crumb is on the top of the stack)
	</p>
	<ul id='stack'>
		<%
			// get crumbs 
			BreadCrumbTrail trail = (BreadCrumbTrail)session.getAttribute(BreadCrumbInterceptor.CRUMB_KEY);
			ArrayList<Crumb> crumbs = new ArrayList(trail.getCrumbs());
			Collections.reverse(crumbs);
			pageContext.setAttribute("crumbs",crumbs);
			pageContext.setAttribute("crumbsSize",crumbs.size());
		%>
		<c:forEach var="c" items="${crumbs}" varStatus="s">
			<s:push value="%{#attr['c']}">
				<s:url var="surl" action="%{action}" namespace="%{namespace}" method="%{method}" includeContext="false"/>
			</s:push>
			<c:url var="url" value="${surl}">                
				<c:forEach items="${c.params}" var="p">
					<c:param name="${p.key}" value="${p.value[0]}"/>
				</c:forEach>
			</c:url>
			<li>
										
			<div>
				<span>Crumb #${crumbsSize - s.index}</span>	
				<span style="text-align: right; float:right;">(time stamp: <fmt:formatDate value="${c.timestamp}" type="both" dateStyle="medium"/>)</span>
				<br style='clear:both;'/>
			</div>

			<pre style="background: none;">
<c:if test="${c.name == 'Home'}">crumb name  : <a href='home.do'>${c.name}</a></c:if>
<c:if test="${c.name != 'Home'}">crumb name  : <sj:a href='%{#attr.url}' onSuccessTopics="/arianna" targets="action_result" >${c.name}</sj:a></c:if>
namespace   : ${c.namespace}
action name : ${c.action}
method      : ${c.method}
</pre>				
<code style='clear:both; margin-top: 0.5em;'>struts url  : ${url}</code>
			</li>
		</c:forEach>
	</ul>
	
	
</div>