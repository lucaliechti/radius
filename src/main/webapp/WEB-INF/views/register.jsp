<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header_new.jsp" />
<main class="firstcontainer container">

<h1><spring:message code="register.title"/></h1>
 
<c:if test="${registrationError != null}">
<div class="alert alert-danger fade-in" role="alert">
	<a href="#" class="close" data-dismiss="alert">&times;</a>
	Something went wrong. Try again.
</div>
</c:if>

<c:if test="${emailExistsError != null}">
<div class="alert alert-danger fade-in" role="alert">
	<a href="#" class="close" data-dismiss="alert">&times;</a>
	A user with this email already exists. Try another email.
</div>
</c:if>


<form:form method="POST" action="register" commandName="registrationForm">
	<div class="form-group">
		<form:label path="firstName" class="col-2 col-form-label">First Name</form:label>
		<div class="col-10">
			<form:input path="firstName" class="form-control"/>
		</div>
		<form:errors path="firstName"/>
	</div>
	
	<div class="form-group">
		<form:label path="lastName" class="col-2 col-form-label">Last Name</form:label>
		<div class="col-10">
			<form:input path="lastName" class="form-control"/>
		</div>
		<form:errors path="lastName"/>
	</div>
	
	<div class="form-group">
		<form:label path="canton" class="col-2 col-form-label">Canton</form:label>
		<div class="col-10">
			<form:select path="canton"> 
   				<form:option value="NONE" label="---"/>
   				<form:options items="${cantons}" />
			</form:select>
		</div>
	</div>
	
	<div class="form-group">
		<form:label path="email" class="col-2 col-form-label">Email</form:label>
		<div class="col-10">
			<form:input path="email" class="form-control"/>
		</div>
		<form:errors path="email"/>
	</div>

	<div class="form-group">
		<form:label path="password" class="col-2 col-form-label">Password</form:label>
		<div class="col-10">
			<form:input path="password" type="password" class="form-control"/>
		</div>
		<form:errors path="password"/>
	</div>	
	
	<input type="submit" class="btn btn-primary" value="Register" />
	<sec:csrfInput />
</form:form>
</main>
<jsp:include page="templates/footer.jsp" />