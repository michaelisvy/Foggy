<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>

<html>
<head>
<title>Foggy Beijing</title>
<%@include file="header.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
		var options = chartOptions("line","chart", "Beijing Air Pollution");		
		parseData(options, "${chartData}");						
		var chartToday = new Highcharts.Chart(options);
	});
	var highchartsOptions = Highcharts.setOptions(chartTheme("skies", "<%=request.getContextPath()%>"));

	$(function() {
		$("table#dataTable").tablesorter({});
	});
</script>
</head>
<body>
	<jsp:include page="menu.jsp" />
	<div class="container">
		<div class="content">
			<div class="page-header-small">
				<div class="alert alert-info">
					${title}
				</div>
			</div>
			<div class="span12" style="margin-left: 0px;">
				<div class="span7" style="margin-left: 0px;">
					<div id="chart" style="width: 100%; height: 300px; align: left"></div>
				</div>
				<div class="span3">
					<br/><br/><html:img url="images/${city}.jpg" />
				</div>
			</div>
			<div class="row">
		<div class="span12" style="margin-left: 0px;">
					<div class="span8">
					<table class="table table-striped table-bordered" id="dataTable">
						<thead>
							<tr>
								<th class="yellow header headerSortDown">${label}</th>
								<th class="red header">AQI</th>
								<th class="blue header">PM2.5</th>
								<th class="green header">Status</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="airData" items="${airDataList}">
								<tr>
									<td><joda:format value="${airData.date}"
											pattern="${dateFormat}" />
									</td>
									<td>${airData.airQualityIndex}</td>
									<td><fmt:formatNumber maxFractionDigits="0"
											value="${airData.fineParticleIndex}" />
									</td>
									<td>${airData.label}</td>
								</tr>
							</c:forEach>
					</table>
				</div>
				<div class="span3">
					<html:img url="images/duke-segway.png" />
				</div>
			</div>
			</div>
		</div>

		<footer>
			<%@include file="footer.jsp"%>		
		</footer>

	</div>

</body>
</html>
