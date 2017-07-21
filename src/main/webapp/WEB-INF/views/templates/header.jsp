<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>reach</title>

	<!-- Bootstrap -->
	<link href="css/bootstrap.min.css" rel="stylesheet">

	<!--  additional CSS -->
	<link href="css/custom.css" rel="stylesheet">
</head>

<html>
<body>
	<script src="js/jquery-3.2.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>

	<div class="container">
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-8 col-md-offset-2 col-lg-6 col-lg-offset-3">

				<!-- fancy header: http://www.newthinktank.com/2015/11/learn-bootstrap-one-video/ -->
				
				<nav class="navbar navbar-default">
					<div class="container-fluid">
						<div class="navbar-header">
							<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#menu" aria-expanded="false">
								<span class="sr-only"></span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
								<span class="icon-bar"></span>
							</button>

						</div>

						<div class="collapse navbar-collapse" id="menu">
							<ul class="nav navbar-nav">
								<li id="nav-home" class="dropdown">
									<a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
										<span class="glyphicon glyphicon-home" aria-hidden="true"></span> <spring:message code="home.navbar"/> <span class="caret"></span>
									</a>
									<ul class="dropdown-menu">
									<li class="dropdown-header"><spring:message code="about.title.short"/></li>
										<li><a href="<c:url value='/' />"><spring:message code="home.title.short"/></a></li>
										<li><a href="<c:url value='/contact' />"><spring:message code="contact.title.short"/></a></li>
										<li role="separator" class="divider"></li>
										<li class="dropdown-header"><spring:message code="legal.title.short"/></li>
										<li><a href="<c:url value='/privacy' />"><spring:message code="privacy.title.short"/></a></li>
										<li><a href="<c:url value='/imprint' />"><spring:message code="imprint.title.short"/></a></li>
									</ul>
								</li>

								<li id="nav-howto">
									<a href="<c:url value='/howto' />">
										<span class="glyphicon glyphicon-cog" aria-hidden="true"></span> <spring:message code="howto.title.short"/>
									</a>
								</li>

								<li id="nav-talk">
									<a href="<c:url value='/experience' />">
										<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> <spring:message code="experience.title.short"/>
									</a>
								</li>
								
								<li id="nav-account" class="dropdown">
									<a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
										<span class="glyphicon glyphicon-user" aria-hidden="true"></span> <spring:message code="account.title.short"/> <span class="caret"></span>
									</a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value='/profile' />"><spring:message code="profile.title.short"/></a></li>
										<li><a href="<c:url value='/myexperience' />"><spring:message code="myexperience.title.short"/></a></li>
										<li><a href="<c:url value='/history' />"><spring:message code="history.title.short"/></a></li>

  										<li class="dropdown-submenu"><a tabindex="-1" href="#"><spring:message code="language.title.short"/></a>
  											<ul class="dropdown-menu">
  										    	<li><a tabindex="-1" href="?lang=de">DE</a></li>
  										    	<li><a href="?lang=fr">FR</a></li>
  										    	<li><a href="?lang=it">IT</a></li>
  										    	<li><a href="?lang=en">EN</a></li>
  										    </ul>
  										</li>
										
										<li role="separator" class="divider"></li>
										<li><a href="#"><font color="red"><span class="glyphicon glyphicon-off" aria-hidden="true"></span> <spring:message code="logout.title.short"/></font></a></li>
									</ul>
								</li>
								
							</ul>
						</div>
					</div>
				</nav>

			</div>
		</div>
		
		<!-- will be closed in footer -->
		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-8 col-md-offset-2 col-lg-6 col-lg-offset-3">