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
<p style="margin-bottom: 10px;"><spring:message code="answers.subtitle"/></p>

<!-- preparing variables -->
<spring:message code="answers.motivation.ph" var="motivationph" />
<spring:message code="answers.modus.explain" var="explain" />

<form:form method="POST" action="answers" modelAttribute="answerForm">
   <div class="form-group leftsection-content-element" id="register-motivation">
      <form:label path="motivation" class="label-title">
         <spring:message code="answers.motivation"/>
      </form:label>
      <div class="col-10">
         <form:textarea path="motivation" placeholder='${motivationph}' class="input-paragraph form-control"/>
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
      <div class="feedback-error" id="answers-feedback-languages">
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
      <div class="feedback-error" id="answers-feedback-modi">
         <form:errors path="modus"/>
      </div>
   </div>

   <!-- hier kommt die SpecialForm -->

        <div class="leftsection-content-element" id="register-special" style="border: 3px solid; border-color: #ff0000; position: relative; padding-top: 0px; padding-bottom: 0px; margin-top: 20px; margin-bottom: 0px;">
            <img src="img/ch.png" class="pos top left"/>
            <img src="img/ch.png" class="pos top right"/>
            <div id="specialAnswers">
            <label for="specialanswers" class="label-title special"><spring:message code="questions.special"/></label>

            <c:forEach begin="1" end="${nrV}" varStatus="loop">
                 <p>
                    <spring:message code="questions.special.${currentVote}.${loop.index}"/>
                 </p>
                 <form:radiobutton path="specialanswers[${loop.index-1}]" value="TRUE"/>
                 <label>
                    <spring:message code="question.true"/>
                 </label>
                 <form:radiobutton path="specialanswers[${loop.index-1}]" value="FALSE"/>
                 <label>
                    <spring:message code="question.false"/>
                 </label>
                 <form:radiobutton path="specialanswers[${loop.index-1}]" value="DONTCARE"/>
                 <label>
                    <spring:message code="question.dontcare"/>
                 </label>
            </c:forEach>

             </div>
             <img src="img/ch.png" class="pos bottom left"/>
             <img src="img/ch.png" class="pos bottom right"/>
          </div>

   <!-- Ende SpecialForm -->

   <!-- UNBEDINGT machen, dass die Fragen anders heissen: wenn nur normale Fragen, dann "Antworten", ansonsten "Reguläre Antworten" oder sowas -->

   <div class="form-group leftsection-content-element" id="register-questions">
      <label for="regularanswers" class="label-title"><spring:message code="answers.answers"/></label>
      <c:forEach begin="1" end="${nrQ}" varStatus="loop">
        <p>
           <spring:message code="q${loop.index}"/>
        </p>
        <form:radiobutton path="regularanswers[${loop.index-1}]" value="TRUE"/>
        <label>
           <spring:message code="question.true"/>
        </label>
        <form:radiobutton path="regularanswers[${loop.index-1}]" value="FALSE"/>
        <label>
           <spring:message code="question.false"/>
        </label>
        <form:radiobutton path="regularanswers[${loop.index-1}]" value="DONTCARE"/>
        <label>
           <spring:message code="question.dontcare"/>
        </label>
        <div class="feedback-error" id="answers-feedback-q${loop.index}">
           <form:errors path="regularanswers"/>
        </div>
      </c:forEach>
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
      <div class="feedback-error" id="answers-feedback-location">
         <form:errors path="locations"/>
      </div>
        <input type="submit" class="btn btn-primary" value="Answer" />
   		<sec:csrfInput />
   		</form:form>
   </div>
</section>

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

</main>
<jsp:include page="templates/footer.jsp" />