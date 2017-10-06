<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="templates/header.jsp" />

<script> $('#nav-account').addClass('active'); </script>

<c:if test="${loggedin != null}">
	<div class="alert alert-success fade-in" role="alert">
	<a href="#" class="close" data-dismiss="alert">&times;</a>
  		You have been successfully logged in.
	</div>
</c:if>

<h1><spring:message code="profile.title"/></h1>

<jsp:include page="templates/footer.jsp" />