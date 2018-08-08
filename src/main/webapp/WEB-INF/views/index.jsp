<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header_new.jsp" />

<main class="firstcontainer container">
   <h1><spring:message code="index.title"/></h1>
   <p><spring:message code="index.subtitle"/></p>
   <h2><spring:message code="index.about.title"/></h2>
   <p><spring:message code="index.about"/></p>
   <h2><spring:message code="index.works.title"/></h2>
   <p><spring:message code="index.works"/></p>
   <h2><spring:message code="index.interested"/></h2>
   <p class=interested><spring:message code="index.interested.1"/>: <a style="color:#D02643;font-weight: bold;" href="https://wemakeit.com/projects/radius"><spring:message code="index.interested.1.link"/></a></p>
   <p class=interested><spring:message code="index.interested.2"/>:</p>
   <spring:message code="index.send" var="send"/>
      <div id="feedback">
      <c:choose>
         <c:when test="${success == 0}">
            <p class="error"><spring:message code="index.error"/></p>
         </c:when>
         <c:when test="${success == 1}">
            <p class="success"><spring:message code="index.success"/></p>
         </c:when>
         <c:otherwise>
         </c:otherwise>
      </c:choose>
   </div>

   <form:form method="POST" action="" commandName="reminderForm" id="form">
      <form:input path="email" type="text" placeholder="your@email.ch"/>
      <input type="submit" value="${send}"/><br>
 	  <sec:csrfInput />
<!--  <form:errors path="email" /> -->
   </form:form>

</main>

<jsp:include page="templates/footer_new.jsp" />
