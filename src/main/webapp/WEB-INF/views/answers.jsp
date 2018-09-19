<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header_new.jsp" />

<main class="firstcontainer container">
<section class="fullwidth-section-only-vertical">
<!-- 
<c:if test="${newUser != null}">
  		Almost done! Just answer the following questions:
</c:if>
 -->
 
<h1><spring:message code="answers.title"/></h1>
<p><spring:message code="answers.subtitle"/></p>

<!-- preparing variables -->
<spring:message code="answers.motivation.ph" var="answers.motivation.ph" />



<form:form method="POST" action="answers" commandName="answerForm" accept-charset="ISO-8859-1">
   <div class="form-group leftsection-content-element" id="register-motivation">
      <form:label path="motivation" class="label-title">
         <spring:message code="answers.motivation"/>
      </form:label>
      <div class="col-10">
         <form:textarea path="motivation" placeholder='${answers.motivation.ph}' class="input-paragraph form-control" accept-charset="ISO-8859-1"/>
      </div>
   </div>
   <div class="form-group leftsection-content-element" id="register-languages">
      <form:label path="languages" class="label-title">
         <spring:message code="answers.languages"/>
      </form:label>
      <c:forEach items="${lang}" var="l" >
         <p>
            <form:checkbox id="lang_${l}" path="languages" value="${l}"/>
            <spring:message code="language.${l}"/>
         </p>
      </c:forEach>
      <div class="feedback-error" id="register-feedback-email">
         <form:errors path="languages"/>
      </div>
   </div>
   <div class="form-group leftsection-content-element" id="register-modus">
      <p>
         <form:label path="modus" class="label-title">
            <spring:message code="modus"/>
         </form:label>
      </p>
      <c:forEach items="${modi}" var="m" >
         <label class="radio-button-label-no">
            <spring:message code="modus.${m}"/>
         </label>
         <form:radiobutton path="modus" id="modus_${m}" value="${m}" />
      </c:forEach>
      <div class="feedback-error" id="register-feedback-email">
         <form:errors path="modus"/>
      </div>
   </div>
   <div class="form-group leftsection-content-element" id="register-questions">
      <label for="questions" class="label-title"><spring:message code="answers.answers"/></label>
      <p>
         <spring:message code="q1"/>
      </p>
      <form:radiobutton path="q1" value="true"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.true"/>
      </label>
      <form:radiobutton path="q1" value="false"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.false"/>
      </label>
      <div class="feedback-error">
         <form:errors path="q1"/>
      </div>
      
      <p>
         <spring:message code="q2"/>
      </p>
      <form:radiobutton path="q2" value="true"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.true"/>
      </label>
      <form:radiobutton path="q2" value="false"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.false"/>
      </label>
      <div class="feedback-error">
         <form:errors path="q2"/>
      </div>
      
      <p>
         <spring:message code="q3"/>
      </p>
      <form:radiobutton path="q3" value="true"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.true"/>
      </label>
      <form:radiobutton path="q3" value="false"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.false"/>
      </label>
      <div class="feedback-error">
         <form:errors path="q3"/>
      </div>
      
      <p>
         <spring:message code="q4"/>
      </p>
      <form:radiobutton path="q4" value="true"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.true"/>
      </label>
      <form:radiobutton path="q4" value="false"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.false"/>
      </label>
      <div class="feedback-error">
         <form:errors path="q4"/>
      </div>
      
      <p>
         <spring:message code="q5"/>
      </p>
      <form:radiobutton path="q5" value="true"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.true"/>
      </label>
      <form:radiobutton path="q5" value="false"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.false"/>
      </label>
      <div class="feedback-error">
         <form:errors path="q5"/>
      </div>
   </div>
   <div class="form-group leftsection-content-element" id="register-locations">
      <form:label path="locations" class="label-title">
         <spring:message code="answers.location"/>
      </form:label>
      <p>
         <spring:message code="answers.location.detail"/>
      </p>
      <section class="map-section">
         <div id="map"></div>
         <form:hidden path="locations"/>
         <div id="register-map-locations"> </div>
      </section><br>
      <div class="feedback-error">
         <form:errors path="locations"/>
      </div>
        <input type="submit" class="btn btn-primary" value="Answer" />
   		<sec:csrfInput />
   		</form:form>
   </div>





<script>
   var locationes = $('#locations').val();
   var split = locationes.split(';');
</script>
<!-- <script src="css/custom.css"></script> -->
<script type="text/javascript" src="js/ms.js"></script>
<script type="text/javascript" src="js/map.js"></script>
<script>
   for(var i = 0; i < split.length; i++) {
       var region = gl.getLayer("radius_" + split[i]);
       selectDeselect(region);
   }
</script>
</section>
</main>
<jsp:include page="templates/footer_new.jsp" />