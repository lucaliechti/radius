<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="templates/header.jsp" />

<script> $('#nav-talk').addClass('active'); </script>

<h1><spring:message code="experience.title"/></h1>

<a href="<c:url value='/myexperience' />"><spring:message code="experience.writeExperience"/></a><p>

<c:forEach items="${experiences}" var="exp" >
	<div class="well well-sm" id="exp_<c:out value="experience_${exp.id}"/>">

		<div class="experienceName">
			Name: <c:out value="${exp.name}" />
		</div>
		
		<div class="experiencePlace">
			Place: <c:out value="${exp.place}" />
		</div>
		
		<div class="experienceTime">
			Time: <c:out value="${exp.date}" />
		</div>
		
		<div class="experience">
			Experience: <c:out value="${exp.experience}" />
		</div>
		
	</div>
</c:forEach>

<jsp:include page="templates/footer.jsp" />