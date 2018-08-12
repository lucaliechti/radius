<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="templates/header_new.jsp" />
<main class="firstcontainer container">
<h1><spring:message code="profile.title"/></h1>

<h3>Personal data <a href="#">(edit)</a></h3> 
<p><b>Vorname:</b> ${firstName}
<p><b>Name:</b> ${lastName}
<p><b>Email:</b> ${email}
<p><b>Canton:</b> <spring:message code="canton.${canton}"/>
<p><b>Motivation:</b> ${motivation}
<h3>Radius data <a href="<c:url value='/answers'/>">(edit)</a></h3>
<h2>Questions</h2>
<p><b><spring:message code="q1"/></b><br><spring:message code="question.${q1}"/>
<p><b><spring:message code="q2"/></b><br><spring:message code="question.${q2}"/>
<p><b><spring:message code="q3"/></b><br><spring:message code="question.${q3}"/>
<p><b><spring:message code="q4"/></b><br><spring:message code="question.${q4}"/>
<p><b><spring:message code="q5"/></b><br><spring:message code="question.${q5}"/>
<h2>Other data</h2>
<p><b>Languages:</b> <c:forEach items="${languages}" var="item" varStatus="loop"><spring:message code="language.${item}"/>${!loop.last ? ', ' : ''}</c:forEach>
<p><b>Modus:</b> <spring:message code="modus.${modus}"/>
<p><b>Locations:</b> <c:forEach items="${locations}" var="item" varStatus="loop">${item}${!loop.last ? ', ' : ''}</c:forEach>
<h2>Delete profile</h2>
<p><a href="#">delete profile</a></p>
</main>
<jsp:include page="templates/footer_new.jsp" />