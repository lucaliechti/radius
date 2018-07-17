<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>radius</title>

	<!-- Bootstrap -->
	<link href="css/bootstrap.min.css" rel="stylesheet">

	<!--  additional CSS -->
	<link href="css/custom.css" rel="stylesheet">
	
	<!-- BEGIN test Leaflet -->
	<link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.1/dist/leaflet.css"
   		integrity="sha512-Rksm5RenBEKSKFjgI3a41vrjkw4EVPlJ3+OiI65vTjIdo9brlAacEuKOiQ5OFh7cOI1bkDwLqdLw3Zg0cRJAAQ=="
   		crossorigin=""/>
   	<script src="https://unpkg.com/leaflet@1.3.1/dist/leaflet.js"
   		integrity="sha512-/Nsx9X4HebavoBvEBuyp3I7od5tA0UzAxs+j83KgC8PU0kgB4XiK4Lfe4y4cgBtaRJQEIFCW+oC506aPT2L1zw=="
   		crossorigin=""></script>
	 <!-- END test Leaflet -->

</head>

<html>
<body>
	<script src="js/jquery-3.2.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	
<!-- fancy header: http://www.newthinktank.com/2015/11/learn-bootstrap-one-video/ -->
<sec:authorize access="isAuthenticated()">
    Authenticated as <sec:authentication property="principal.username"/>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
   Not authenticated
</sec:authorize>

<a href="?lang=de">DE</a>
<a href="?lang=fr">FR</a>
<a href="?lang=it">IT</a>
<a href="?lang=en">EN</a>
				
<a href="<c:url value='/' />"><spring:message code="home.title.short"/></a>
<a href="<c:url value='/contact' />"><spring:message code="contact.title.short"/></a>
<a href="<c:url value='/privacy' />"><spring:message code="privacy.title.short"/></a>
<a href="<c:url value='/imprint' />"><spring:message code="imprint.title.short"/></a>
<a href="<c:url value='/howto' />"><spring:message code="howto.title.short"/></a>
<a href="<c:url value='/experience' />"><spring:message code="experience.title.short"/></a>
								
<!-- if not logged in -->
<sec:authorize access="!isAuthenticated()">
	<a href="<c:url value='/login' />">		<font color="green"><spring:message code="login.title.short"/></font></a>
</sec:authorize>
								
<!-- if logged in -->
<sec:authorize access="isAuthenticated()">
	<a href="<c:url value='/profile' />"><spring:message code="profile.title.short"/></a>
	<a href="<c:url value='/myexperience' />"><spring:message code="myexperience.title.short"/></a>
	<a href="<c:url value='/history' />"><spring:message code="history.title.short"/></a>
	<form action="<c:url value='/logout' />" method="post"><button name="logout" value="logout" class="btn btn-link"><sec:csrfInput /><font color="red"><spring:message code="logout.title.short"/></font></button></form>
</sec:authorize>