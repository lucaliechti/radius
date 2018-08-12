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
		<form:label path="motivation">Motivation</form:label>
		<div class="col-10">
			<form:input path="motivation" class="form-control"/>
		</div>
	</div>
	<!-- 
	<div class="form-group">
		<form:label path="languages">Languages</form:label>
		<form:checkboxes items="${lang}" path="languages" />
		<form:errors path="languages"/> <br>
	</div>
	 -->
	<div class="form-group">
		<form:label path="languages">Languages<br></form:label>
		<c:forEach items="${lang}" var="l" >
			<spring:message code="language.${l}"/> <form:checkbox id="lang_${l}" path="languages" value="${l}"/>
		</c:forEach>
		<form:errors path="languages"/> <br>
	</div>
	<!-- 
	<div class="form-group">
		<form:label path="modus">Modus </form:label>
		<form:select path="modus"> 
   			<form:options items="${modi}" />
		</form:select>
		<form:errors path="modus"/>
	</div>
	-->
	<div class="form-group">
		<form:label path="modus">Modus </form:label>
		<form:select path="modus">
		<c:forEach items="${modi}" var="m" >
			<form:option id="modus_${m}" value="${m}"><spring:message code="modus.${m}"/></form:option>
		</c:forEach>
		</form:select>
		<form:errors path="modus"/>
	</div>	
	
	<br><br>
	
	<spring:message code="q1"/>
	<form:radiobutton path="q1" value="false"/><spring:message code="question.false"/>
	<form:radiobutton path="q1" value="true"/><spring:message code="question.true"/>
	<form:errors path="q1"/> <br>

	<spring:message code="q2"/>
	<form:radiobutton path="q2" value="false"/><spring:message code="question.false"/>
	<form:radiobutton path="q2" value="true"/><spring:message code="question.true"/>
	<form:errors path="q2"/><br>
	
	<spring:message code="q3"/>
	<form:radiobutton path="q3" value="false"/><spring:message code="question.false"/>
	<form:radiobutton path="q3" value="true"/><spring:message code="question.true"/>
	<form:errors path="q3"/> <br>
	
	<spring:message code="q4"/>
	<form:radiobutton path="q4" value="false"/><spring:message code="question.false"/>
	<form:radiobutton path="q4" value="true"/><spring:message code="question.true"/>
	<form:errors path="q4"/> <br>
	
	<spring:message code="q5"/>
	<form:radiobutton path="q5" value="false"/><spring:message code="question.false"/>
	<form:radiobutton path="q5" value="true"/><spring:message code="question.true"/>
	<form:errors path="q5"/> <br>
	
	<br>

	Select ur location()s):
	<div id="map"></div>
	<form:hidden path="locations" />
	<form:errors path="locations"/>
	
	
	<input type="submit" class="btn btn-primary" value="Answer" />
	<sec:csrfInput />
</form:form>

<script src="css/custom.css"></script>
<script type="text/javascript" src="js/ms.js"></script>
<script type="text/javascript" src="js/map.js"></script>
</main>
<jsp:include page="templates/footer_new.jsp" />