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
      <section class="leftsection-content-element" id="survey">
         <h1> Welche Fragen würden Sie gerne diskutieren?</h1>
         <p>Wählen Sie höchstens 10.</p>
         <div class="form-group leftsection-content-element" id="survey-questions">
            <form:form method="POST" action="survey" modelAttribute="surveyForm">
               <c:forEach begin="1" end="15" varStatus="loop">
                  <p>
                     <form:checkbox id="q${loop.index}" path="questions" value="${loop.index}" onchange="showOrHide('#q${loop.index}', ${loop.index})"/>
                     <b>
                        <spring:message code="survey.q${loop.index}"/>
                     </b>
                  </p>
                  <div id="answer${loop.index}">
                     <p>
                        <spring:message code="survey.answer"/><br>
                        <form:radiobutton id="a${loop.index}_yes" path="a${loop.index}" value="true"/>
                        <label class="radio-button-label-yes">
                           <spring:message code="question.true"/>&emsp;
                        </label>
                        <form:radiobutton id="a${loop.index}_no" path="a${loop.index}" value="false" />
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
                 <form:input path="emailN" class="form-control" accept-charset="ISO-8859-1"/>

              <div class="feedback-error" id="survey-feedback-email">
                 <form:errors path="emailN"/>
              </div>
              </div>
           </div>
         </div>


         <div class="form-group leftsection-content-element" id="survey-registration">
            <h1>Registrierung bei Radius</h1>
            <p><form:checkbox path="registration" id="checkbox-registration" onchange="showOrHideSection($('#checkbox-registration'), 'hiddenRegistration')"/> Ja, ich würde mich gerne bei Radius registrieren.</p><br>
            <div id="hiddenRegistration">
            <div class="form-group">
               <form:label path="firstName">
                  <spring:message code="home.register.fn"/>
               </form:label>
               <div class="col-10">
                  <form:input path="firstName" class="form-control"  accept-charset="ISO-8859-1"/>
               </div>
               <div class="feedback-error" id="survey-feedback-email">
                  <form:errors path="firstName"/>
               </div>
            </div>
            <div class="form-group">
               <form:label path="lastName">
                  <spring:message code="home.register.ln"/>
               </form:label>
               <div>
                  <form:input path="lastName" class="form-control" accept-charset="ISO-8859-1"/>
               </div>
               <div class="feedback-error" id="survey-feedback-email">
                  <form:errors path="lastName"/>
               </div>
            </div>
            <div class="form-group">
               <form:label path="canton">
                  <spring:message code="home.register.canton"/>
               </form:label>
               <div>
                  <form:select class="dropdown-input" path="canton">
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
                  <form:input path="emailR" class="form-control"  accept-charset="ISO-8859-1"/>
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
                  <form:input path="password" type="password" class="form-control" accept-charset="ISO-8859-1"/>
               </div>
               <div class="feedback-error" id="survey-feedback-email">
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