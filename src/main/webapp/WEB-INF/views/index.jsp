<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />

<script> $('#nav-login').addClass('active'); </script>

<h1><spring:message code="register.title"/></h1>

<c:choose>
  <c:when test="${success == 0}">
	<div class="alert alert-danger fade-in" role="alert">
		<a href="#" class="close" data-dismiss="alert">&times;</a>
		Something went wrong. Try again.
	</div>
  </c:when>
  <c:when test="${success == 1}">
	<div class="alert alert-success fade-in" role="alert">
	<a href="#" class="close" data-dismiss="alert">&times;</a>
  		Your email has successfully been added to the reminder list.
	</div>
  </c:when>
  <c:otherwise>
  </c:otherwise>
</c:choose>

<form:form method="POST" action="home" commandName="reminderForm">
	<div class="form-group">
		<form:label path="email" class="col-2 col-form-label">Email</form:label>
		<div class="col-10">
			<form:input path="email" class="form-control"/>
		</div>
		<form:errors path="email"/>
	</div>
	<input type="submit" class="btn btn-primary" value="send me a reminder" />
	<sec:csrfInput />
</form:form>