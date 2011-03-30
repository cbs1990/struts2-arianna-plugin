<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="bc" uri="/struts-arianna-tags" %>
<%--
	<script type="text/javascript">
		$(document).ready(function() {
			$('.crumb').tooltip();
		});
	</script>
 --%>	
<div class='breadcrumb-trail'>
	<span>Breadcrumb : </span> 
	<bc:breadcrumbs var='c' status='s'>
		<s:if test='not #s.first'>
			<span class="separator">&raquo;</span>
		</s:if>
		<span class='crumb'>
			<s:if test='not #s.last'>
				<s:url var="surl" action="%{action}" namespace="%{namespace}" includeContext="false"/>
				
				<c:url var="url" value="${surl}">                
	                <c:forEach items="${c.params}" var="p">
	                	<c:param name="${p.key}" value="${p.value[0]}"/>
					</c:forEach>
				</c:url>
				
				<s:if test='name == "Home"'>
					<s:a href="%{#attr['url']}">${c.name}</s:a>				
				</s:if>
				<s:else>
					<sj:a href="%{#attr['url']}" targets="action_result" value="%{name}" onSuccessTopics="/arianna, /success/effect">${c.name}</sj:a>
				</s:else>
				 
			</s:if>
			<s:if test='#s.last'>
				<span class='current'>
					<s:property value="name"/>
				</span>
			</s:if>
		</span>
	</bc:breadcrumbs>
</div>