<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="bc" uri="/struts-arianna-tags"%>


<s:set name="theme" value="'xhtml'" scope="page" />
<c:set var='contextName' value='${pageContext.servletContext.contextPath}'/>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Insert title here</title>
	<link rel="stylesheet" type="text/css" href="${contextName}/res/css/layout.css" media="screen" />
	
	<script src="http://yandex.st/highlightjs/5.16/highlight.min.js"></script>
	<link rel="stylesheet" href="http://yandex.st/highlightjs/5.16/styles/school_book.min.css">
		
	<link rel="stylesheet" type="text/css" href="${contextName}/res/css/site.css"/>
	
		
	<sj:head debug="false" jqueryui="true" jquerytheme="start" loadAtOnce='true'/>
		
	<!-- include the Tools -->
	<script src="http://cdn.jquerytools.org/1.2.5/tiny/jquery.tools.min.js"></script>
	
	<script type="text/javascript" >
		$.subscribe('/success/effect', function(event,element) {			
			$(element).effect("highlight", {}, 500);
		});
	</script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$('.crumb').tooltip();
			$('.tipped').tooltip();
						
			$( "#cases" ).accordion({
				autoHeight: false
			});
			
			$( "button, input:submit").button();
			
			$("pre.code").each(function(index,elem) {
				hljs.highlightBlock(elem, '    ', false);	
			});
			
//			$( "#radio" ).buttonset();
		});
	</script>
	 
	<style>
		.tooltip {
			display:none;
			background-color: red;
			font-size:12px;
/*			
			height:70px;
			width:160px;
*/			
			padding:25px;
			color:#fff;	
		}
		
		a {
			text-decoration: underline;
		}
		
		label {
			font-family: sans-serif;
			/* color: #6EAC2C; */
		}
		
		#cases {
			font-size: 8pt;
		}
		
		#controls {
			font-size: 8pt;
		}
		
		#arianna2 {
			padding-top: 0.7em;
			padding-bottom: 0.7em;
			border-bottom: 1px solid silver;
			border-top: 1px solid silver;
		}
	</style>
	
</head>
<body>

<div id="header">	
	<h1>Struts<sup>2</sup>-arianna-plugin test cases</h1>	
	<div id='arianna2'> 									
		<sj:div href="arianna.jsp" onSuccessTopics="/success/effect" listenTopics="/arianna">arianna contents goes here</sj:div>
	</div>	
</div>

<div id="gutter">
	<!-- this is the gutter -->
</div>
<div id="col2">
	<div id='wrp'>
					<div id="cases">
						<h3><a href='#'>Simple Actions</a></h3>
						<div class='case'>
							<p>
								five simple actions annotated with five differents annotations like .
								<pre class='code'><code class='java'>@BreadCrumb("Simple <i>...</i>")</code></pre>								
							</p>
							<ul>
								<s:iterator status="s" var="v" value="{1,2,3,4,5}">
								<li>
									<sj:a href="simple-simple%{v}.do"
										onSuccessTopics="/arianna"
										effect="highlight"
										targets="action_result">Simple <s:property value='#s.index + 1'/></sj:a>
								</li>
								</s:iterator>
							</ul>								
						</div>
						
						<h3><a href='#'>Action with different parameter value</a></h3>
						<div class='case'>
							<p>
								Here you can invoke the same action with different parameters values.<br/> 
							</p>  
							<s:form action="pAction" theme='xhtml'>
								<s:select name='p' list="{'pippo','pluto','paperino','topolino','minnie'}" label="p parameter"/>
								<sj:submit value="execute" onSuccessTopics="/arianna" targets="action_result"/>
							</s:form>
							<p>
								If you you are using the <code>RequestComparator</code> it will compares values and recognize 
								the actions inequalities. 
							</p>
						</div>
						
						<h3><a href='#'>Different actions with equals  annotation</a></h3>
						<div class='case'>
							<p>
								Here you can invoke different actions annotated with the <em>same</em> @BreadCrumb annotation.
								If the crumb produced by such actions are considered equals or not depends by the actual comparator. 
							</p>
							<pre class='code'><code>@BreadCrumb("Crumb-A")</code></pre>
							<ul>
								<s:iterator var="a" value="{'A1','A2','A3'}" status="s">
									<li>
										invoke 
										<sj:a href="same-%{a}.do"
											onSuccessTopics="/arianna" 
											targets="action_result">
											Action-${a}
										</sj:a> 
									</li>
								</s:iterator>
							</ul>
							<pre class='code'><code>@BreadCrumb("Crumb-B")</code></pre>
							<ul>
								<s:iterator var="b" value="{'B1','B2','B3'}" status="s">
									<li>
										invoke 
										<sj:a href="same-%{b}.do"
											onSuccessTopics="/arianna" 
											targets="action_result">
											Action-${b}
										</sj:a> 
									</li>
								</s:iterator>
							</ul>
						</div>
						
						<h3><a href='#'>Breadcrumb's' name as OGNL expression</a></h3>
						<div class='case'>
							<p>
							<p>
								clicking <i>execute</i> invokes an action annotated with
								<pre class='code'><code class='java'>@Breadcrumb("%{name}")</code></pre>
								as a result the typed text will be used as the bread crumb name.  
							</p>
							<s:form action="ognl-name">
								<s:textfield name='name' label="name" required="true"></s:textfield>
								<sj:submit value="execute" onSuccessTopics="/arianna" targets="action_result"/>
							</s:form>
						</div>
						<h3><a href='#'>Actions overriding the default behaviour</a></h3>
						<div class='case'>
							<p>Whathever the breadcrumb trail configuration is, it can be overriden 
								by any <code>@BreadCrumb</code> annotation.
								<br/>
							</p>   
							<p>
								
							<ul>
								<li>
									<!-- 
									<pre class='code'><code class='java'>@Breadcrumb("Test")</code></pre>
									 -->
									<sj:a href='override-Test.do'
										onSuccessTopics="/arianna" 
										targets="action_result">test</sj:a>
								</li>
							<!-- 
								<li>
									<pre class='source'><code class='java'>@BreadCrumb(value="")</code></pre>
									override rewind mode with <sj:a href='override-RewindModeNEVER.do' 
										onSuccessTopics="/arianna" 
										targets="action_result">NEVER</sj:a>
								</li>
								<li>
									override rewind mode with <sj:a href='override-RewindModeAUTO.do' 
										onSuccessTopics="/arianna" 
										targets="action_result">AUTO</sj:a>
								</li>
							 -->
								<li>
								<%--
									<pre class='code'><code class='java'>@Breadcrumb(value="Test"
    ,comparator=NameComparator.class)</code>
									</pre>
									 --%>
									use <sj:a href='override-NameComparator.do' 
										onSuccessTopics="/arianna" 
										targets="action_result">NameComparator</sj:a>
								</li>
								<li>
									use <sj:a href='override-ActionComparator.do' 
										onSuccessTopics="/arianna" 
										targets="action_result">ActionComparator</sj:a>
								</li>
								<li>
									use <sj:a href='override-RequestComparator.do' 
										onSuccessTopics="/arianna" 
										targets="action_result">RequestComparator</sj:a>
						
								</li>
							</ul>						
						</div>
					</div>					
				</div>
	</div>
</div>

<!-- === Column 1 (center)======================================================================================-->
<div id="col1">
<%--
					<div id='arianna' class="ui-widget-content"> 									
						<sj:div href="arianna.jsp" onSuccessTopics="/success/effect" listenTopics="/arianna">arianna contents goes here</sj:div>
					</div>					
					<hr/>
 --%>
 					<sj:div id="action_result" onSuccessTopics='/arianna /success/effect'>
						<p>
						This web application was developed to test the behaviour of the plugin. 
						However it can also be used to show the plugin capabilities.
						</p>
						<p>    
							Use the test cases availables on the left to see how the 					
							<span>struts<sup>2</sup> arianna plugin</span> reacts to different action annotation. 
						</p>
						
						<p>
							You can use the panel on the right to reconfigure the bread crumb trail defaults.
							Invoke the test cases with different configurations of the plugin and see how the bread crumb trail
							reacts.
						</p>
					</sj:div>
</div>

<!-- === Column 3 (right)======================================================================================-->
<div id="col3">
	<div id='controls' class='ui-widget ui-corner-all'>
<%--		<h4 class='ui-state-default' style='margin:0 0 0 0; padding:0.3em 0.3em 0.3em 0.3em'><a>Breadcrumb trail configuration</a></h4>  --%>
		<h4 class='ui-widget-header' style='margin-top: 1px; margin-bottom: 1px;'>Breadcrumb trail configuration</h4>
		<div class='ui-widget-content'>					 				
			<c:import url="controls.jsp" />
		</div>
	</div>				
</div>
<div id="footer">
	see the <a href='http://code.google.com/p/struts2-arianna-plugin/'><span>struts<sup>2</sup> arianna plugin</span></a> project hosted at google code for more info
</div>
</body>
</html>