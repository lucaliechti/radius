<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header_new.jsp" />
<main class="firstcontainer container">

<c:if test="${newUser != null}">
  		Almost done! Just answer the following questions:
</c:if>

<h1><spring:message code="answers.title"/></h1>

<form:form method="POST" action="answers" commandName="answerForm">
	<div class="form-group">
		<form:label path="motivation" class="col-2 col-form-label">Motivation</form:label>
		<div class="col-10">
			<form:input path="motivation" class="form-control"/>
		</div>
	</div>
	
	<div class="form-group">
		<form:label path="languages" class="col-2 col-form-label">Languages</form:label>
		<div class="col-10">
			<form:checkboxes items="${lang}" path="languages" />
		</div>
		<form:errors path="languages"/> <br>
	</div>
	
	Question 1?
	<form:radiobutton path="q1" value="false"/>No
	<form:radiobutton path="q1" value="true"/>Yes
	<form:errors path="q1"/> <br>

	Question 2?
	<form:radiobutton path="q2" value="false"/>No 
	<form:radiobutton path="q2" value="true"/>Yes
	<form:errors path="q2"/><br>
	
	Question 3?
	<form:radiobutton path="q3" value="false"/>No
	<form:radiobutton path="q3" value="true"/>Yes
	<form:errors path="q3"/> <br>
	
	Question 4?
	<form:radiobutton path="q4" value="false"/>No
	<form:radiobutton path="q4" value="true"/>Yes
	<form:errors path="q4"/> <br>
	
	Question 5?
	<form:radiobutton path="q5" value="false"/>No
	<form:radiobutton path="q5" value="true"/>Yes
	<form:errors path="q5"/> <br>

	<div class="form-group">
		<form:label path="modus" class="col-2 col-form-label">Modus</form:label>
		<form:select path="modus"> 
   			<form:options items="${modi}" />
		</form:select>
		<form:errors path="modus"/>
	</div>

	Select ur location()s):
	<div id="map"></div>
	<form:hidden path="locations" />
	<form:errors path="locations"/>
	
	
	<input type="submit" class="btn btn-primary" value="Answer" />
	<sec:csrfInput />
</form:form>

<script src="css/custom.css"></script>
<script type="text/javascript" src="js/lozern.js"></script>
<script type="text/javascript" src="js/ms.js"></script>
<script type="text/javascript" src="js/map.js"></script>
</main>
<jsp:include page="templates/footer_new.jsp" />