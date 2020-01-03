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
<spring:message code="answers.answer" var="answer" />

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

    <c:if test="${special}">
        <div class="leftsection-content-element" id="register-special" style="border: 3px solid; border-color: #ff0000; position: relative; padding-top: 0px; padding-bottom: 0px; margin-top: 20px; margin-bottom: 0px;">
            <img src="img/ch.png" class="pos top left"/>
            <img src="img/ch.png" class="pos top right"/>
            <div id="specialAnswers">
            <label for="specialanswers" class="label-title special"><spring:message code="questions.special"/></label>

            <c:forEach begin="1" end="${nrV}" varStatus="loop">
                <p>
                   <spring:message code="questions.special.${currentVote}.${loop.index}"/>
                </p>
                <label class="answer">
                   <form:radiobutton path="specialanswers[${loop.index-1}]" value="TRUE"/>
                   <spring:message code="question.true"/>
                </label>
                <label class="answer">
                    <form:radiobutton path="specialanswers[${loop.index-1}]" value="FALSE"/>
                    <spring:message code="question.false"/>
                </label>
                <label class="answer">
                    <form:radiobutton path="specialanswers[${loop.index-1}]" value="DONTCARE"/>
                    <spring:message code="question.dontcare"/>
                </label>
                <hr style="height:6px; visibility:hidden;" />
            </c:forEach>

            <div class="feedback-error" id="specialanswers-feedback-error">
               <form:errors path="specialanswers"/>
            </div>

            </div>
            <img src="img/ch.png" class="pos bottom left"/>
            <img src="img/ch.png" class="pos bottom right"/>
          </div>
   </c:if>

   <div class="form-group leftsection-content-element" id="register-questions">
      <label for="regularanswers" class="label-title"><spring:message code="answers.answers"/></label>
      <c:forEach begin="1" end="${nrQ}" varStatus="loop">
          <p>
              <spring:message code="q${loop.index}"/>
          </p>
          <label class="answer">
              <form:radiobutton path="regularanswers[${loop.index-1}]" value="TRUE"/>
              <spring:message code="question.true"/>
          </label>
          <label class="answer">
              <form:radiobutton path="regularanswers[${loop.index-1}]" value="FALSE"/>
              <spring:message code="question.false"/>
          </label>
          <label class="answer">
              <form:radiobutton path="regularanswers[${loop.index-1}]" value="DONTCARE"/>
              <spring:message code="question.dontcare"/>
          </label>
          <hr style="height:6px; visibility:hidden;" />
      </c:forEach>
      <div class="feedback-error" id="regularanswers-feedback-error">
         <form:errors path="regularanswers"/>
      </div>
       <p>
       <c:choose>
           <c:when test="${special}">
               <spring:message code="answers.answers.hint.special"/>
           </c:when>
           <c:otherwise>
               <spring:message code="answers.answers.hint.regular"/>
           </c:otherwise>
       </c:choose>
       </p>
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
        <input type="submit" class="btn btn-primary" value='${answer}' />
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
