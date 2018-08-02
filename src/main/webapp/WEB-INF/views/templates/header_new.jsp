<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
    
    <title>Radius</title>
    
    <!-- fonts -->
    <link href="https://fonts.googleapis.com/css?family=Barlow+Condensed|Fira+Sans+Condensed:300|Saira+Semi+Condensed" rel="stylesheet">

	<!-- jQuery -->
	<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.3.1.min.js"></script>

	<!-- additional CSS -->
    <link rel="stylesheet" type="text/css" href="css/radius-style-desktop-launch.css">
    <link rel="stylesheet" type="text/css" href="css/radius-style-mobile-launch.css">
    <link href="css/custom.css" rel="stylesheet">
	
	<!-- Leaflet -->
	<link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.3/dist/leaflet.css"><link/>
	<script src="https://unpkg.com/leaflet@1.3.3/dist/leaflet.js"></script>

	<!-- favicon -->
	<link rel="shortcut icon" href="img/favicon.ico" />
</head>

<body>
	<!-- 
	<sec:authorize access="isAuthenticated()">
	    Authenticated as <sec:authentication property="principal.username"/><br>
	</sec:authorize>
	<sec:authorize access="!isAuthenticated()">
	   Not authenticated<br>
	</sec:authorize>
	 -->
	<nav role="navigation" id="primary-menubar">
	   <div id="home">
	      <a href="<c:url value='/' />"><img src="img/logo-radius-white.png" id="logo-home"></a>
	   </div>
	   <div id="menuToggle">
	      <input type="checkbox" />
	      <div id="burger">
	         <span></span>
	         <span></span>
	         <span></span>
	      </div>
	      <ul id="menu" class="menupoint language-selector">
	         <a href="?lang=de"> <li>DE</li> </a>
	         <a href="?lang=fr"> <li>FR</li> </a>
	         <a href="?lang=it"> <li>IT</li> </a>
	         <a href="?lang=en"> <li>EN</li> </a>
	      </ul>
	   </div>
	</nav>
