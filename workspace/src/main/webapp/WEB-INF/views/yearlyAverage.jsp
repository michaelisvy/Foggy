<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>

<html>
<head>
<title>Beijing air quality yearly average</title>
<%@include file="header.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		var optionsYearly = chartOptions("column","chartYearly", "Beijing Air Pollution");		
		parseData(optionsYearly, "${chartDataYears}");						
		var chartYearly = new Highcharts.Chart(optionsYearly);
		
		var optionsMonthlyAQI = chartOptions("line","chartMonthlyAQI", "Air Quality Index");		
		parseData(optionsMonthlyAQI, "${chartDataMonthsAQI}");						
		var chartMonthlyAQI = new Highcharts.Chart(optionsMonthlyAQI);
		
		var optionsMonthlyPM25 = chartOptions("line","chartMonthlyPM25", "PM 2.5");		
		parseData(optionsMonthlyPM25, "${chartDataMonthsPM25}");						
		var chartMonthlyPM25 = new Highcharts.Chart(optionsMonthlyPM25);
		
		
	});
	var highchartsOptions = Highcharts.setOptions(chartTheme("skies", "<%=request.getContextPath()%>"));
</script>
</head>
<body>
	<jsp:include page="menu.jsp" />
	<div class="container">
		<div class="content">
			<div class="page-header-small">
				<h1>${city} Yearly average</h1>
			</div>
			<div class="row">
				<div class="span12">
					<div class="span6">
						<div id="chartYearly"
							style="width: 100%; height: 300px; align: left"></div>
					</div>
					<div class="span4">
							<br/><br/><br/>
							<html:img url="images/duke-many.png" />
						</div>
					<br/>
					<div class="span10">
						<br/>
						<div id="chartMonthlyAQI"
							style="width: 100%; height: 300px; align: left"></div>
					</div>
					<br/>
					<div class="span10">
						<br/>
						<div id="chartMonthlyPM25"
							style="width: 100%; height: 300px; align: left"></div>
					</div>
				</div>
				</div>
			</div>
			<footer>
			<%@include file="footer.jsp"%>		
		</footer>
		</div>
	</div>
	<!-- /container -->
</body>
</html>
