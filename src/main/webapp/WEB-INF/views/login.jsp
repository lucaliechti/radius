<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />

<script> $('#nav-login').addClass('active'); </script>

<h1><spring:message code="login.title"/></h1>

<c:if test="${loginerror}">
<div class="alert alert-danger" role="alert">
	Wrong username or password. Try again.
</div>
</c:if>

<form name="loginForm" action="login" method="POST">
	<div class="form-group">
  		<label for="name" class="col-2 col-form-label">Username</label>
  		<div class="col-10">
    		<input id="username" name="username" type="text" class="form-control" value=""/>
  		</div>
	</div>
	
	<div class="form-group">
  	<label for="place" class="col-2 col-form-label">Password</label>
  		<div class="col-10">
    		<input id="password" name="password" type="password" class="form-control" value=""/>
  		</div>
	</div>
	
	<input type="submit" class="btn btn-primary" value="Submit" /> <input id="remember_me" name="remember-me" type="checkbox"/>
	<label for="remember_me">Remember me</label>
	
	<sec:csrfInput />
</form>


<jsp:include page="templates/footer.jsp" />