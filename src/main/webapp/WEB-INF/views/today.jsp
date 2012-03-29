<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>

<html>
<head>
<meta property="og:title" content="Foggy status of the day" />
<meta property="og:description"
	content="Current level of pollution in Beijing" />
<title>Foggy Beijing- Air Quality today</title>
<%@include file="header.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		var options = chartOptions("line","chartToday", "Beijing Air Pollution");		
		parseData(options, "${chartData}");						
		var chartToday = new Highcharts.Chart(options);
	});
	var highchartsOptions = Highcharts.setOptions(chartTheme("skies", "<%=request.getContextPath()%>"));
</script>
</head>
<body>
	<div class="container">
		<jsp:include page="menu.jsp" />
		<div class="content">
			<div class="page-header-small">
				<div class="alert-message warning">${city} Foggy Status of the Day</div>
				<h3>
					<joda:format value="${airDataInfoNow.date}" style="F-" />
				</h3>
			</div>
			<div class="row">
				<jsp:include page="air-dashboard.jsp"/>
				<div id="chartToday" style="width: 280px; height: 280px; align: left; display: inline-block;"></div>
			<div id="links" style="display: inline-block;">
						<table class="invisible">
							<tr>
								<td>More links <br /> <html:link
										url="data/daily/${city}"
										label="More info about the past few days" /> <br /> <html:link
										url="data/weekly/${city}" label="Weekly average" /> <br /> <html:link
										url="data/monthly/${city}" label="Monthly average" /> <br />
									<html:link url="data/yearly/${city}" label="Yearly average" />
								</td>
							</tr>
						</table>
					</div>
			</div>			
		</div>
		</div>
			<script>
				$(function() {
					$("a[rel=twipsy]").twipsy({
						live : true
					})
				})
			</script>
			<html:script url="/js/addThis.js" />
			<footer>
				<%@include file="footer.jsp"%>		
			</footer>
</body>
</html>
