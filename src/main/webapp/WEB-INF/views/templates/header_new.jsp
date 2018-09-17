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
    <link rel="stylesheet" type="text/css" href="css/radius-style-desktop-new.css"> <!-- was -desktop-launch -->
    <link rel="stylesheet" type="text/css" href="css/radius-style-mobile-new.css"> <!-- was -mobile-launch -->
	
	<!-- Leaflet -->
	<link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.3/dist/leaflet.css"><link/>
	<script src="https://unpkg.com/leaflet@1.3.3/dist/leaflet.js"></script>

	<!-- favicon -->
	<link rel="shortcut icon" href="img/favicon.ico" />
</head>
<body>

<!-- preparing variables -->
<spring:message code="logout.title.short" var="logout" />

<nav role="navigation" id="primary-menubar">
   <div id="home">
      <a href="home"><img id="logo-home" /></a> <!-- src="img/logo-radius.png" -->
   </div>
   <div id="menuToggle">
      <input type="checkbox" />
      <div id="burger">
         <span></span>
         <span></span>
         <span></span>
      </div>
      <ul id="menu" class="menupoint navigations">
         <li><a href="<c:url value='/about' />"><spring:message code="about.title"/></a></li>
         <sec:authorize access="isAuthenticated()"><li><a href="<c:url value='/profile' />"><spring:message code="profile.title"/></a></li></sec:authorize>
         <!-- <li><a href="<c:url value='/experience' />">Experiences</a></li> -->
         <sec:authorize access="isAuthenticated()"><li><a href="<c:url value='/logout' />"><spring:message code="logout.title.short"/></a></li></sec:authorize>
         <li>
            <ul id="language-select" class="menupoint navigations">
               <li><a href="?lang=de">DE</a></li>
               <li><a href="?lang=fr">FR</a></li>
               <!-- <li><a href="?lang=it">IT</a></li> -->
               <li><a href="?lang=en">EN</a></li>
            </ul>
         </li>
      </ul>
   </div><!-- 
   <sec:authorize access="isAuthenticated()">
      <form action="<c:url value='/logout' />" method="post">
         <input id="button-logout" type="submit" class="btn btn-primary" value='${logout}' >
         <sec:csrfInput />
      </form>
   </sec:authorize> -->
</nav>