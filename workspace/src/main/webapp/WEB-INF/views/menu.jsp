<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="html" tagdir="/WEB-INF/tags" %>

<script>
	$('#topbar').dropdown()
</script>
<div class="topbar">
	<div class="fill">
		<div class="container">
			<ul class="tabs">
				<li class="active"><html:link url="data/today/beijing" label="Home"/></li>
				<li class="dropdown" data-dropdown="dropdown"><a href="#"
					class="dropdown-toggle">Beijing</a>
					<ul class="dropdown-menu">
						<li><html:link url="data/daily/beijing" label="Beijing these days"/></li>
						<li><html:link url="data/weekly/beijing" label="Weekly average"/></li>
						<li><html:link url="data/monthly/beijing" label="Monthly average"/></li>
						<li><html:link url="data/yearly/beijing" label="Yearly average"/></li>						
					</ul>
				</li>
				<li class="dropdown" data-dropdown="dropdown"><a href="#"
					class="dropdown-toggle">Guangzhou</a>
					<ul class="dropdown-menu">
						<li><html:link url="data/today/guangzhou" label="Guangzhou today"/></li>
						<li><html:link url="data/daily/guangzhou" label="Daily average"/></li>
						<li><html:link url="data/weekly/guangzhou" label="Weekly average"/></li>
						<li><html:link url="data/monthly/guangzhou" label="Monthly average"/></li>
					</ul>
				</li>
				<li class="active"><a href="http://www.foggybeijing.com/blog">Blog</a></li>
				<li class="dropdown" data-dropdown="dropdown"><a href="#"
					class="dropdown-toggle">About</a>
					<ul class="dropdown-menu">
						<li><html:link url="data/about" label="About Foggy Beijing"/></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</div>
