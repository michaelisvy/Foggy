<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Foggy Beijing - About</title>
<%@include file="header.jsp"%>
</head>
<body>
	<jsp:include page="menu.jsp" />
	<div class="container">
		<div class="content">

			<h1>About Foggy Beijing</h1>
			<a name="penguin"></a>
			<p>
				<span class="first-word">All</span> the data that we have used to
				build this website comes from the US embassy via their now famous
				twitter feed @BeijingAir. Their monitor is located on the top of the
				American Embassy in Beijing North-East Chaoyang.
			</p>

			
			<br />
			<h2>AQI levels</h2>
			<div class="row">
				<div class="span8">
					<table>
						<thead>
							<tr>
								<th>Air Quality Index (AQI) Values</th>
								<th>Levels of Health concern</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>0 to 50</td>
								<td>Good</td>
							</tr>
							<tr>
								<td>51 to 100</td>
								<td>Moderate</td>
							</tr>
							<tr>
								<td>101 to 150</td>
								<td>Unhealthy for Sensitive Groups</td>
							</tr>
							<tr>
								<td>151 to 200</td>
								<td>Unhealthy</td>
							</tr>
							<tr>
								<td>201 to 300</td>
								<td>Very Unhealthy</td>
							</tr>
							<tr>
								<td>301 to 500</td>
								<td>Hazardous</td>
							</tr>
						</tbody>
					</table>

				</div>
			</div>
			<h2>PM2.5</h2>
			<p>
				<span class="first-word">PM2.5</span> particles are air pollutants
				with a diameter of 2.5 micrometers or less, small enough to invade
				even the smallest airways. These particles generally come from
				activities that burn fossil fuels, such as traffic, smelting, and
				metal processing. Because of their small size, fine particles can
				lodge deeply into the lungs.
			</p>

			<h2>Special Thanx</h2>

			<p>This website has been built using the Java language and a set
				of open-source technologies: Tomcat, Spring, Hibernate...</p>

			<p>At last, we would like to thank the US embassy for making its
				monitoring data publicly available.</p>

			<h2>Contact us</h2>
			You can reach out to us on <a href="mailto:foggybeijing@gmail.com">foggybeijing@gmail.com</a>.
			Please use this email to report any issue or improvements that you would like
			to see in foggybeijing.

			<footer>
				<%@include file="footer.jsp"%>		
			</footer>
		</div>
	</div>
</body>
</html>
