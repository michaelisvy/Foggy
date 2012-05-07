<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>



<%@ taglib prefix="html" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>

<html:stylesheet url="css/bootstrap.css" />	
<html:stylesheet url="css/foggybeijing.css" />

<c:if test="${widget==false}">
	<html:script url="js/jquery-1.6.2.js"/>
	<html:script url="js/bootstrap-twipsy.js"/>
</c:if>

<style type="text/css">
	/* GENERIC*/
	
	body {
		padding-top: 0px;
	}
	
	h2 {
		line-height:24px;
	}
	
	ul {	  	
	  	color: #424242;
	}
</style>

<div style="width:250px;">
	<c:set var="isWidget" value="true" scope="request"/>
	<jsp:include page="air-dashboard.jsp"/>
</div>
