<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="templates/header.jsp" />

<script> $('#nav-account').addClass('active'); </script>

<h1><spring:message code="myexperience.title"/></h1><br>

<form:form method="POST" action="/reach/experience" commandName="experienceForm">
<!-- why doesn't  action="<c:url value='/experience' />" work here? -->
	<div class="form-group">
  		<label for="name" class="col-2 col-form-label">Name</label>
  		<div class="col-10">
    		<form:input path="name" class="form-control" type="text" />
  		</div>
	</div>
	
	<div class="form-group">
  		<label for="place" class="col-2 col-form-label">Place</label>
  		<div class="col-10">
    		<form:input class="form-control" type="text" path="place" />
  		</div>
	</div>
	
 	<div class="form-group">
    	<label for="experience">Your Experience</label>
    	<form:textarea class="form-control" path="experience" rows="4" />
  	</div>
  	<input type="submit" class="btn btn-primary" value="Submit" />
</form:form>

<jsp:include page="templates/footer.jsp" />