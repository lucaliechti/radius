<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />

<script> $('#nav-account').addClass('active'); </script>

<h1><spring:message code="writeexperience.title"/></h1><br>

<form:form method="POST" action="/reach/experience" commandName="experienceForm">
<!-- why doesn't  action="<c:url value='/experience' />" work here? -->
 	<div class="form-group">
    	<label for="experience">Your Experience</label>
    	<form:textarea class="form-control" path="experience" rows="4" />
  	</div>
  	<input type="submit" class="btn btn-primary" value="Submit" />
</form:form>

<jsp:include page="templates/footer.jsp" />