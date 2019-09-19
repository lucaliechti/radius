<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />

<script>
function showOrHide(checkbox, number) {
  if($(checkbox).is(":checked")){
    $("#answer"+number).show();
  }else{
    $("#answer"+number).hide();
  }
}

function showOrHideSection(checkbox, section) {
  if($(checkbox).is(":checked")){
    $("#"+section).show();
  }else{
    $("#"+section).hide();
  }
}


</script>

<main class="firstcontainer container">
   <section class="fullwidth-section">
      <section class="container leftsection-content-element" id="survey">

        <c:if test="${surveyFailure != null}">
           <p class="result error">
              <spring:message code="home.feedback.error.generic"/><br>
           </p>
        </c:if>

         <h2>Was sind die wichtigsten Fragen der Schweiz?</h2>
         <p>Radius ist eine Plattform, die eine einfache Möglichkeit bietet, persönlich und konstruktiv mit politisch Andersdenkenden zu diskutieren.<br>Wir wollen wissen: Welche politischen Fragen bewegen Sie? Worüber würden Sie sich gerne mit jemand Andersdenkendem unterhalten?</p><br>
         <p>Wählen Sie höchstens 10 Fragen aus, die Sie wichtig finden.</p>
         <div class="form-group leftsection-content-element" id="survey-questions">
            <form:form method="POST" action="survey" modelAttribute="surveyForm">
               <c:forEach begin="1" end="${nrQ}" varStatus="loop">
                  <p>
                     <form:checkbox id="q${loop.index}" path="questions" value="${loop.index}" onchange="showOrHide('#q${loop.index}', ${loop.index})"/>
                     <b>
                        <spring:message code="survey.q${loop.index}"/>
                     </b>
                  </p>
                  <div id="answer${loop.index}">
                     <p>
                        <spring:message code="survey.answer"/><br>
                        <form:radiobutton id="a${loop.index}_yes" path="answers[${loop.index-1}]" value="true"/>
                        <label class="radio-button-label-yes">
                           <spring:message code="question.true"/>&emsp;
                        </label>
                        <form:radiobutton id="a${loop.index}_no" path="answers[${loop.index-1}]" value="false" />
                        <label class="radio-button-label-no">
                           <spring:message code="question.false"/>&emsp;
                        </label>
                     </p>
                  </div>
               </c:forEach>

               <div class="feedback-error" id="survey-feedback-questions">
                  <form:errors path="questions"/>
               </div>
         </div>


         <div class="form-group leftsection-content-element" id="survey-newsletter">
            <div class="form-group">
               <form:label path="emailN">
                  <h1>Anmeldung zum Newsletter</h1>
                  <p><form:checkbox path="newsletter" id="checkbox-newsletter" onchange="showOrHideSection($('#checkbox-newsletter'), 'hiddenNewsletter')"/> Ja, ich würde gerne den Radius-Newsletter per Mail erhalten (deutlich weniger als 1x pro Monat).</p><br>
               </form:label>
               <div id="hiddenNewsletter" >
               <form:label path="emailN">
                  <spring:message code="home.register.email"/>
               </form:label>
               <form:input path="emailN" class="form-control" accept-charset="ISO-8859-1" style="max-width:600px;"/>

               <div class="feedback-error" id="survey-feedback-email">
                  <form:errors path="emailN"/>
               </div>
               <p>
                  <spring:message code="home.register.hint"/>
               </p>
               </div>
            </div>
         </div>


         <div class="form-group leftsection-content-element" id="survey-registration">
            <div class="form-group">
                <form:label path="firstName">
                    <h1>Registrierung bei Radius</h1>
                    <p><form:checkbox path="registration" id="checkbox-registration" onchange="showOrHideSection($('#checkbox-registration'), 'hiddenRegistration')"/> Ja, ich möchte mich bei Radius registrieren, um mit Andersdenkenden in Kontakt zu treten.</p><br>
                </form:label>
                <div id="hiddenRegistration">
                <form:label path="firstName">
                  <spring:message code="home.register.fn"/>
                </form:label>
                <div class="col-10">
                    <form:input path="firstName" class="form-control"  accept-charset="ISO-8859-1" style="max-width:600px;"/>
                </div>
                <div class="feedback-error" id="survey-feedback-firstname">
                    <form:errors path="firstName"/>
                </div>
            <div class="form-group">
               <form:label path="lastName">
                  <spring:message code="home.register.ln"/>
               </form:label>
               <div>
                  <form:input path="lastName" class="form-control" accept-charset="ISO-8859-1" style="max-width:600px;"/>
               </div>
               <div class="feedback-error" id="survey-feedback-lastname">
                  <form:errors path="lastName"/>
               </div>
            </div>
            <div class="form-group">
               <form:label path="canton">
                  <spring:message code="home.register.canton"/>
               </form:label>
               <div>
                  <form:select class="dropdown-input" path="canton" style="max-width:600px;">
                     <form:option value="NONE" label="---"/>
                     <form:options items="${cantons}" />
                  </form:select>
               </div>
            </div>
            <div class="form-group">
               <form:label path="emailR">
                  <spring:message code="home.register.email"/>
               </form:label>
               <div>
                  <form:input path="emailR" class="form-control"  accept-charset="ISO-8859-1" style="max-width:600px;"/>
               </div>
               <div class="feedback-error" id="survey-feedback-email">
                  <form:errors path="emailR"/>
               </div>
            </div>
            <div class="form-group">
               <form:label path="password">
                  <spring:message code="home.register.pw"/>
               </form:label>
               <div>
                  <form:input path="password" type="password" class="form-control" accept-charset="ISO-8859-1" style="max-width:600px;"/>
               </div>
               <div class="feedback-error" id="survey-feedback-password">
                  <form:errors path="password"/>
               </div>
            </div>

            <sec:csrfInput />
            <p>
               <spring:message code="home.register.hint"/>
            </p>
            </div>
            <input type="submit" class="btn btn-primary" value="Absenden" />
         </form:form>

         </div>
      </section>
   </section>
</main>

<script>
showOrHideSection($('#checkbox-newsletter'), 'hiddenNewsletter');
showOrHideSection($('#checkbox-registration'), 'hiddenRegistration');
for (i = 1; i <= 15; i++) { showOrHide($('#q'+i), i); }
</script>

<jsp:include page="templates/footer.jsp" />