<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="templates/header_new.jsp" />
<main class="firstcontainer container">
<h1><spring:message code="profile.title"/></h1>

<h2>Personal data <a href="#">(edit)</a></h2>
<p><b>Vorname:</b> ${firstName}
<p><b>Name:</b> ${lastName}
<p><b>Email:</b> ${email}
<p><b>Canton:</b> ${canton}
<p><b>Motivation:</b> ${motivation}
<h2>Radius data <a href="<c:url value='/answers'/>">(edit)</a></h2>
<h3>Questions</h3>
<p><b>This is the first question?</b><br><spring:message code="question.${q1}"/>
<p><b>This is the second question?</b><br><spring:message code="question.${q2}"/>
<p><b>This is the third question?</b><br><spring:message code="question.${q3}"/>
<p><b>This is the fourth question?</b><br><spring:message code="question.${q4}"/>
<p><b>This is the fifth question?</b><br><spring:message code="question.${q5}"/>
<h3>Other data</h3>
<p><b>Languages:</b> <c:forEach items="${languages}" var="item" varStatus="loop"><spring:message code="language.${item}"/>${!loop.last ? ', ' : ''}</c:forEach>
<p><b>Modus:</b> ${modus}
<p><b>Locations:</b><br>${locations}
<h2>Delete profile</h2>
<p><a href="#">delete profile</a></p>
</main>
<jsp:include page="templates/footer_new.jsp" />