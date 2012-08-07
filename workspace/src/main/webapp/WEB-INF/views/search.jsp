<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<meta property="og:title" content="Foggy status of the day" />
<meta property="og:description"
	content="Current level of pollution in Beijing" />
<title>Foggy Beijing- Air Quality today</title>
<%@include file="header.jsp"%>

<style type="text/css">
ul, li { list-style-type: none; }
</style>

</head>
<body> 
	<div class="container">
		<jsp:include page="menu.jsp" />
		<div class="content">
			<c:url value="/data/search" var="searchUrl"/>
			<form:form modelAttribute="searchFormInfo" action="${searchUrl}">
			<ul>
			<li><form:input path="start"/><form:errors cssClass="error" path="start" /></li>
			<li><form:input path="end"/></li>
			<li><form:select path="range">
					<form:option label="days" value="1"/>
					<form:option label="weeks" value="2"/>
					<form:option label="months" value="3"/>
				</form:select></li>
			<li><button id="searchButton" type="submit">Submit</button></li>
			</ul>			
			</form:form>
			
		</div>
	</div>
			<footer>
				<%@include file="footer.jsp"%>
				<script type="text/javascript">
					$('.datepicker').datepicker()
				</script>		
			</footer>
</body>
</html>
