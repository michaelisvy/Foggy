<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>

<html>
<head>
<title>Foggy Beijing</title>
<%@include file="header.jsp"%>
</head>
<body>
	<jsp:include page="menu.jsp" />
	<div class="container">
		<div class="content">
			<div class="page-header-small">
				<div class="alert-message warning">
					This page does not exist. Please click <html:link url="data/today/beijing" label="here"/> to go back to foggybeijing.
				</div>
			</div>
		</div>
		<footer>
			<%@include file="footer.jsp"%>		
		</footer>
	</div>
</body>
</html>
