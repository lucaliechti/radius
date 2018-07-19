<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="templates/header.jsp" />

<script> $('#nav-home').addClass('active'); </script>

<c:if test="${loggedout != null}">
	<div class="alert alert-success fade in" role="alert">
	<a href="#" class="close" data-dismiss="alert">&times;</a>
  		You have been successfully logged out.
	</div>
</c:if>

<h1><spring:message code="home.title"/></h1>

<a href="<c:url value='/login' />"><spring:message code="login.title"/></a><p>
<a href="<c:url value='/register' />"><spring:message code="register.title"/></a>




<jsp:include page="templates/footer.jsp" />