<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />

<!-- Leaflet -->
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.3/dist/leaflet.css"><link/>
<script src="https://unpkg.com/leaflet@1.3.3/dist/leaflet.js"></script>

<main class="firstcontainer container">
<section class="fullwidth-section-only-vertical">
 
<h1><spring:message code="answers.title"/></h1>
<p><spring:message code="answers.subtitle"/></p>

<!-- preparing variables -->
<spring:message code="answers.motivation.ph" var="motivationph" />
<spring:message code="answers.modus.explain" var="explain" />


<form:form method="POST" action="answers" modelAttribute="answerForm" accept-charset="ISO-8859-1">
   <div class="form-group leftsection-content-element" id="register-motivation">
      <form:label path="motivation" class="label-title">
         <spring:message code="answers.motivation"/>
      </form:label>
      <div class="col-10">
         <form:textarea path="motivation" placeholder='${motivationph}' class="input-paragraph form-control" accept-charset="ISO-8859-1"/>
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
         <span title="${explain}">(?)</span>
      </p>
      <c:forEach items="${modi}" var="m" >
      <form:radiobutton path="modus" id="modus_${m}" value="${m}" />
         <label class="radio-button-label-no">
            <spring:message code="modus.${m}"/>
         </label>
         
      </c:forEach>
      <div class="feedback-error" id="register-feedback-email">
         <form:errors path="modus"/>
      </div>
   </div>

   <!-- this is just for fun

   <div class="leftsection-content-element" id="register-special" style="border: 3px solid; border-color: #ff0000; position: relative; padding-top: 0px; padding-bottom: 0px; margin-top: 20px; margin-bottom: 0px;">
       <img src="img/ch.png" class="topleft" style="position: absolute; top: 0px; left: 0px;"/>
       <img src="img/ch.png" class="topright" style="position: absolute; top: 0px; right: 0px;"/>
       <div style="margin: 50px 50px;">
       <label for="questions" class="label-title"><font color="#ff0000"><spring:message code="questions.special"/></font></label>

      <p>
         <spring:message code="questions.special.2019.4.1"/>
      </p>
      <form:radiobutton path="special" value="true"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.true"/>
      </label>
      <form:radiobutton path="special" value="false"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.false"/>
      </label>
      <form:radiobutton path="special" value="dontcare"/>
      <label class="radio-button-label-dontcare">
         <spring:message code="question.dontcare"/>
      </label>

      <p>
         <spring:message code="questions.special.2019.4.2"/>
      </p>
      <form:radiobutton path="special" value="true"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.true"/>
      </label>
      <form:radiobutton path="special" value="false"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.false"/>
      </label>
      <form:radiobutton path="special" value="dontcare"/>
      <label class="radio-button-label-dontcare">
         <spring:message code="question.dontcare"/>
      </label>

      <p>
         <spring:message code="questions.special.2019.4.3"/>
      </p>
      <form:radiobutton path="special" value="true"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.true"/>
      </label>
      <form:radiobutton path="special" value="false"/>
      <label class="radio-button-label-yes">
         <spring:message code="question.false"/>
      </label>
      <form:radiobutton path="special" value="dontcare"/>
      <label class="radio-button-label-dontcare">
         <spring:message code="question.dontcare"/>
      </label>


       </div>
       <img src="img/ch.png" class="bottomleft" style="position: absolute; bottom: 0px; left: 0px;"/>
       <img src="img/ch.png" class="bottomright" style="position: absolute; bottom: 0px; right: 0px;"/>
   </div> -->

   <!-- UNBEDINGT machen, dass die Fragen anders heissen: wenn nur normale Fragen, dann "Antworten", ansonsten "Reguläre Antworten" oder sowas -->


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
<jsp:include page="templates/footer.jsp" />