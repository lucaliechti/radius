<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />

<main class="firstcontainer container">
   <section id="leftsection">
        <!-- only while the survey is on -->

        <c:if test="${newsletter_subscribe_success != null}">
           <p class="result success">
              <spring:message code="home.feedback.success.newsletter.subscribe"/><br>
           </p>
        </c:if>

        <c:if test="${newsletter_unsubscribe_success != null}">
           <p class="result success">
              <spring:message code="home.feedback.success.newsletter.unsubscribe"/><br>
           </p>
        </c:if>

        <c:if test="${surveySuccess != null}">
           <p class="result success">
              <spring:message code="survey.feedback.success"/><br>
           </p>
        </c:if>
   
         <c:if test="${waitForEmailConfirmation != null}">
            <p class="result success">
               <spring:message code="home.feedback.success.waitforemail"/><br>
            </p>
         </c:if>

         <c:if test="${passwordReset != null}">
            <p class="result success">
               <spring:message code="home.feedback.success.passwordreset"/><br>
            </p>
         </c:if>
         
         <c:if test="${loggedout != null}">
            <p class="result success">
               <spring:message code="home.feedback.success.logout"/><br>
            </p>
         </c:if>

         <c:if test="${emailconfirmed != null}">
            <p class="result success">
               <spring:message code="home.feedback.success.mailconfirmed"/><br>
            </p>
         </c:if>
                  
         <c:if test="${loginerror != null}">
            <p class="result error">
               <spring:message code="home.feedback.success.loginerror"/><br>
            </p>
         </c:if>
         
         <c:if test="${delete_success != null}">
            <p class="result success">
               <spring:message code="home.feedback.success.delete"/><br>
            </p>
         </c:if>
         
         <c:if test="${not_enabled != null}">
            <p class="result error">
               <spring:message code="home.feedback.error.notenabled"/><br>
            </p>
         </c:if>
         
         <c:if test="${confirmation_error != null}">
            <p class="result error">
               <spring:message code="home.feedback.error.confirmation"/><br>
            </p>
         </c:if>

         <c:if test="${generic_error != null}">
            <p class="result error">
               <spring:message code="home.feedback.error.generic"/><br>
            </p>
         </c:if>

      <section class="leftsection-title" id="page-title">
         <h2>
            <spring:message code="home.title"/>
         </h2>
          <p style="margin-top: 10px;"> <!-- lets test this here -->
              <spring:message code="home.welcome.details"/>
          </p>
      </section>
      <section class="leftsection-content">
         <section class="leftsection-content-element" id="">
<!--            <h2>
               <spring:message code="home.welcome.title"/>
            </h2>
            <p>
               <spring:message code="home.welcome.details"/>
            </p>                                                      -->
            <h2>
               Was sind die wichtigsten Fragen der Schweiz?
            </h2>
            <p>
               Radius ist eine Plattform, die eine einfache Möglichkeit bietet, persönlich und konstruktiv mit politisch Andersdenkenden zu diskutieren.<br>
               Wir wollen wissen: Welche politischen Fragen bewegen Sie? Worüber würden Sie sich gerne mit jemand Andersdenkendem unterhalten? Lassen Sie es uns in der <a href="<c:url value='/survey' />">Umfrage</a> wissen!
            </p>
         </section>
         <section class="leftsection-content-element" id="">
            <h2>
               <spring:message code="home.crowdfunding.title"/>
            </h2>
            <p>
               <spring:message code="home.crowdfunding.details"/>
            </p>
         </section>
      </section>
   </section>
   
   <!-- preparing variables -->
   <spring:message code="home.login.login" var="login" />
   <spring:message code="home.register.register" var="register" />
   <spring:message code="newsletter.register" var="feed" />
   
   <section id="rightsection">
      <section class="login-resume">
         <h2>
            <spring:message code="home.login.title"/>
         </h2>
         <form name="loginForm" action="home" method="POST">
            <div class="form-group">
               <label for="username" class="col-2 col-form-label">
                  <spring:message code="home.login.email"/>
               </label>
               <div class="col-10">
                  <input id="username" name="username" type="text" class="form-control" value=""/>
               </div>
            </div>
            <div class="form-group">
               <label for="password" class="col-2 col-form-label">
                  <spring:message code="home.login.pw"/>
               </label>
               <div class="col-10">
                  <input id="password" name="password" type="password" class="form-control" value=""/>
               </div>
            </div>
            <input type="submit" class="btn btn-primary" value='${login}' />
            <div>
                <input id="remember_me" name="remember-me" type="checkbox"/>
                <label for="remember_me">
                   <spring:message code="home.login.remember"/>
                </label>
            </div>
            <sec:csrfInput />
         </form>

         <a href="<c:url value='/forgot' />"><spring:message code="home.login.forgot"/></a>

      </section>
      
      <section id="profile-deco" class="profile-resume">
         <h2>
            <spring:message code="home.register.title"/>
         </h2>
         <form:form method="POST" action="register" modelAttribute="registrationForm">
            <div class="form-group">
               <form:label path="firstName">
                  <spring:message code="home.register.fn"/>
               </form:label>
               <div class="col-10">
                  <form:input path="firstName" class="form-control"/>
               </div>
               <div class="feedback-error" id="register-feedback-firstname">
                  <form:errors path="firstName"/>
               </div>
            </div>
            <div class="form-group">
               <form:label path="lastName">
                  <spring:message code="home.register.ln"/>
               </form:label>
               <div>
                  <form:input path="lastName" class="form-control"/>
               </div>
               <div class="feedback-error" id="register-feedback-lastname">
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
               <form:label path="email">
                  <spring:message code="home.register.email"/>
               </form:label>
               <div>
                  <form:input path="email" class="form-control"/>
               </div>
               <div class="feedback-error" id="register-feedback-email">
                  <form:errors path="email"/>
               </div>
            </div>
            <div class="form-group">
               <form:label path="password">
                  <spring:message code="home.register.pw"/>
               </form:label>
               <div>
                  <form:input path="password" type="password" class="form-control"/>
               </div>
               <div class="feedback-error" id="register-feedback-password">
                  <form:errors path="password"/>
               </div>
            </div>
            <input type="submit" class="btn btn-primary" value="${register}" />
            <sec:csrfInput />
         </form:form>
         <p>
            <spring:message code="home.register.hint"/>
         </p>
         <c:if test="${registrationError != null}">
            <p class="error">
               <spring:message code="home.register.error.general"/>
            </p>
         </c:if>
         <c:if test="${emailExistsError != null}">
            <p class="error">
               <spring:message code="home.register.error.emailexists"/>
            </p>
         </c:if>
      </section>

            <section id="profile-deco" class="profile-resume">
               <h2>
                  <spring:message code="newsletter.title"/>
               </h2>
                <form:form method="POST" action="subscribe" modelAttribute="newsletterForm">
                    <div class="form-group">
                       <form:label path="email">
                          <spring:message code="home.register.email"/>
                       </form:label>
                       <div class="col-10">
                          <form:input path="email" class="form-control"/>
                       </div>
                       <div class="feedback-error" id="register-feedback-newsletter-email">
                          <form:errors path="email"/>
                       </div>
                    </div>
                    <input type="submit" class="btn btn-primary" value="${feed}" />
                    <p><spring:message code="newsletter.description"/></p>
                    <sec:csrfInput />
                </form:form>
            </section>

   </section>
</main>
<jsp:include page="templates/footer.jsp" />

