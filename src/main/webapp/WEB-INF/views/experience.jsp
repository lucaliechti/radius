<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="templates/header.jsp" />

<script> $('#nav-talk').addClass('active'); </script>

<h1><spring:message code="experience.title"/></h1>

<h4>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed
	diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam
	erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et
	ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem
	ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing
	elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna
	aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo
	dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus
	est Lorem ipsum dolor sit amet.
</h4>

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