

<%@ taglib prefix="html" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>

<style type="text/css">
ul.dashboard-colors,ul.dashboard-data {
	  	margin: 0 0 10px 3px;
	}

	ul.dashboard-colors li,ul.dashboard-data li {
	  	height:30px;
	  	display:inline;
	  	width:100%;
	  	float:left;
	  	color: #424242;
	}
	
	/* COLORS */
	ul.dashboard-colors {
	  	width:48px;
	}
	
	ul.dashboard-colors li{
	  	float:left;
	  	width:50%;
	  	color:black;
	}
	li.dashboard-first  { 
		width:35px; 
		text-align:right;
		vertical-align:top;
		font-size:13px;
	} 	
	
	/* DATA */
	ul.dashboard-data {
	  	padding:0px;
	}
	
	#dashboard-column-left { 
	 	top:20;
	 	left:0;
	 	width:60px;
	 	height:240px;
	 	position:relative;
	 	display: inline-block;
	} 
	
	#dashboard-column-right { 
		position:relative;
		display: inline-block;
	 	top:20;
	 	height:240px;
	 	width:180px;
	} 
	
	.dashboard-round {
	border-radius: 4px 4px 4px 4px;
	background-color:#FFF;
	font-size:30px;
	line-height: 80px;
	text-align:center;
	color:black;
	padding: 1px 1px;
	}
</style>


<div id="dashboard-column-left">
<ul class="dashboard-colors"> 
	<li class="dashboard-first">500</li>	
  	<li style="background-color:${widgetInfo.colors[7]};">${widgetInfo.thermometer[7]}</li>

	<li class="dashboard-first">400</li>	
  	<li style="background-color:${widgetInfo.colors[6]};">${widgetInfo.thermometer[6]}</li>

	<li class="dashboard-first">300</li>	
  	<li style="background-color:${widgetInfo.colors[5]};">${widgetInfo.thermometer[5]}</li>

	<li class="dashboard-first">250</li>	
  	<li style="background-color:${widgetInfo.colors[4]};">${widgetInfo.thermometer[4]}</li>

	<li class="dashboard-first">200</li>	
  	<li style="background-color:${widgetInfo.colors[3]};">${widgetInfo.thermometer[3]}</li>

	<li class="dashboard-first">150</li>	
  	<li style="background-color:${widgetInfo.colors[2]};">${widgetInfo.thermometer[2]}</li>

	<li class="dashboard-first">100</li>	
  	<li style="background-color:${widgetInfo.colors[1]};">${widgetInfo.thermometer[1]}</li>

	<li class="dashboard-first">50</li>	
  	<li style="background-color:${widgetInfo.colors[0]};">${widgetInfo.thermometer[0]}</li>
</ul>
</div>
<div id="dashboard-column-right">
<ul class="dashboard-data">
	<li>
		<h2 style="text-align:justify;"><a href="http://www.foggybeijing.com" target="_top">Beijing Air</a></h2>
	</li>
	<li style="height:70px;">
		<span class="dashboard-round">${airDataSummary.airDataInfo.airQualityIndex}<span style="font-size:10px">AQI</span>
		</span> 
		&nbsp;
		<span class="dashboard-round"><fmt:formatNumber maxFractionDigits="0">${airDataSummary.airDataInfo.fineParticleIndex}</fmt:formatNumber>
			<span style="font-size:10px">PM2.5</span>
		</span>
	</li>
	<li style="line-height:15px;">
		<span class="label" style="${widgetInfo.labelStyle}">${airDataSummary.airDataInfo.capitalizedLabel}</span>
	</li>	
	<li>
		Last updated: <joda:format value="${airDataSummary.airDataInfo.date}" style="SS" dateTimeZone="Europe/London"/>
	</li>
	<li style="line-height:15px;margin-top:0px; margin-bottom:20px;">
		<p>
		<img src="<%=request.getContextPath()%>/images/rss.gif" style="display: block; vertical-align: top;  margin-right: 8px; float: left;"> 
		<span style="display: block; overflow: auto; width:140px;text-align: justify;"> 
		<a href="${airDataSummary.wordpressBlog.url}" target="_top">
		Latest blog: ${airDataSummary.wordpressBlog.title}</a>
		</span>
		</p>
	</li>
	<c:if test="${isWidget==true}">
	<li style="line-height:35px;margin-top:10px;text-align: right;">
		<p>
		<span style="width:30px; overflow: auto; font-style:italic;"> 
			Read more on  <a href="http://www.foggybeijing.com" target="_top"> Foggy Beijing</a>
		</span>		
		</p>
	</li>
	</c:if>
	</ul>
</div>