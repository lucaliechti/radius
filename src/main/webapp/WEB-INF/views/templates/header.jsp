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
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<title>Bootstrap test</title>
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
</head>

<html>
<body>
	<script src="js/jquery-3.2.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>

	<div class="container">

		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-8 col-md-offset-2 col-lg-6 col-lg-offset-3">

				<!-- .navbar-fixed-top, or .navbar-fixed-bottom can be added to keep the nav bar fixed on the screen -->
				<nav class="navbar navbar-default">
					<div class="container-fluid">

						<!-- Brand and toggle get grouped for better mobile display -->
						<div class="navbar-header">

							<!-- Button that toggles the navbar on and off on small screens -->
							<button type="button" class="navbar-toggle collapsed"
								data-toggle="collapse" data-target="#menu" aria-expanded="false">

								<!-- Hides information from screen readers -->
								<span class="sr-only"></span>

								<!-- Draws 3 bars in navbar button when in small mode -->
								<span class="icon-bar"></span> <span class="icon-bar"></span> <span
									class="icon-bar"></span>
							</button>

						</div>

						<!-- Collect the nav links, forms, and other content for toggling -->
						<div class="collapse navbar-collapse" id="menu">
							<ul class="nav navbar-nav">

								<li id="nav-home" class="dropdown">
									<a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
										<span class="glyphicon glyphicon-home" aria-hidden="true"></span> Home <span class="caret"></span>
									</a>
									<ul class="dropdown-menu">
									<li class="dropdown-header">About</li>
										<li><a href="<c:url value='/' />">The idea</a></li>
										<li><a href="#">Contact</a></li>
										<li role="separator" class="divider"></li>
										<li class="dropdown-header">Legal</li>
										<li><a href="#">Impressum</a></li>
										<li><a href="#">Datenschutz</a></li>
									</ul>
								</li>

								<li id="nav-howto">
									<a href="#">
										<span class="glyphicon glyphicon-cog" aria-hidden="true"></span> How it works
									</a>
								</li>

								<li id="nav-talk">
									<a href="<c:url value='/experience' />">
										<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Talk
									</a>
								</li>
								
								<li id="nav-account" class="dropdown">
									<a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
										<span class="glyphicon glyphicon-user" aria-hidden="true"></span> Account <span class="caret"></span>
									</a>
									<ul class="dropdown-menu">
										<li><a href="<c:url value='/' />">My profile</a></li>
										<li><a href="#">My experiences</a></li>
										<li><a href="#">My history</a></li>

  										<li class="dropdown-submenu"><a tabindex="-1" href="#">Language</a>
  											<ul class="dropdown-menu">
  										    	<li><a tabindex="-1" href="#">German</a></li>
  										    	<li><a href="#">French</a></li>
  										    	<li><a href="#">Italian</a></li>
  										    	<li><a href="#">English</a></li>
  										    </ul>
  										</li>
										
										<li role="separator" class="divider"></li>
										<li><a href="#"><font color="red"><span class="glyphicon glyphicon-off" aria-hidden="true"></span> Logout</font></a></li>
									</ul>
								</li>
								
							</ul>
						</div>
					</div>
				</nav>

			</div>
		</div>

		<div class="row">
			<div class="col-xs-12 col-sm-12 col-md-8 col-md-offset-2 col-lg-6 col-lg-offset-3">