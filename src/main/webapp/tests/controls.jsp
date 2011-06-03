<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@taglib prefix="bc" uri="/struts-arianna-tags"%>


<s:action var='control' name="control-init" />

<s:set var='trail' value="#session['org.softwareforge.struts2.breadcrumb.BreadCrumbInterceptor:CRUMBS']" />

<s:form id='config' action="control-reconfigure">
	<s:textfield name='breadCrumbTrail.maxCrumbs' label="Max crumbs" value="%{#trail.maxCrumbs}" maxlength="3" size="3"/>
	<s:radio name='rewindMode' 
		list='{"NEVER", "AUTO"}'			
		label="Rewind mode" value="%{#control.rewindMode}"/>
		
	<s:select name="comparatorKey" value="%{#control.comparatorKey}"
		label="Crumb comparator" 
		list="#control.allComparators" 
		listKey="key" listValue="key" />
	<tr>
		<td colspan="2"><hr/></td>
	</tr>
	<sj:submit value='reconfigure' />			
		<span style='float:right; padding: 2px 2px 2px 2px;'>
			<s:a href="control-clearTrail.do">clear trail</s:a>
		</span>
</s:form>
	
