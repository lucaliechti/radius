<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header_new.jsp" />

<main class="firstcontainer container">
   <section id="leftsection">
   
         <c:if test="${waitForEmailConfirmation != null}">
            <p class="success" style="margin-bottom: 1%;">
               <spring:message code="home.feedback.success.waitforemail"/><br>
            </p>
         </c:if>
         
         <c:if test="${loggedout != null}">
            <p class="success" style="margin-bottom: 1%;">
               <spring:message code="home.feedback.success.logout"/><br>
            </p>
         </c:if>

         <c:if test="${emailconfirmed != null}">
            <p class="success" style="margin-bottom: 1%;">
               <spring:message code="home.feedback.success.mailconfirmed"/><br>
            </p>
         </c:if>
                  
         <c:if test="${loginerror != null}">
            <p class="error" style="margin-bottom: 1%;">
               <spring:message code="home.feedback.success.loginerror"/><br>
            </p>
         </c:if>
         
         <c:if test="${delete_success != null}">
            <p class="success" style="margin-bottom: 1%;">
               <spring:message code="home.feedback.success.delete"/><br>
            </p>
         </c:if>
         
         <c:if test="${not_enabled != null}">
            <p class="error" style="margin-bottom: 1%;">
               <spring:message code="home.feedback.error.notenabled"/><br>
            </p>
         </c:if>
         
      <section class="leftsection-title" id="page-title">         
         <h2>
            <spring:message code="home.title"/>
         </h2>
      </section>
      <section class="leftsection-content">
         <section class="leftsection-content-element" id="">
            <h2>
               <spring:message code="home.welcome.title"/>
            </h2>
            <p>
               <spring:message code="home.welcome.details"/>
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
   
   <section id="rightsection">
      <section class="login-resume">
         <h2>
            <spring:message code="home.login.title"/>
         </h2>
         <form name="loginForm" action="login" method="POST">
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
            <input id="remember_me" name="remember-me" type="checkbox"/>
            <label for="remember_me">
               <spring:message code="home.login.remember"/>
            </label>
            <input type="submit" class="btn btn-primary" value='${login}' />
            <sec:csrfInput />
         </form><!-- 
         <a href="#"> kommt noch!
            <spring:message code="home.login.forgot"/>
         </a> -->
      </section>
      
      <section id="profile-deco" class="profile-resume">
         <h2>
            <spring:message code="home.register.title"/>
         </h2>
         <form:form method="POST" action="register" commandName="registrationForm">
            <div class="form-group">
               <form:label path="firstName">
                  <spring:message code="home.register.fn"/>
               </form:label>
               <div class="col-10">
                  <form:input path="firstName" class="form-control"  accept-charset="ISO-8859-1"/>
               </div>
               <div class="feedback-error" id="register-feedback-email">
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
               <div class="feedback-error" id="register-feedback-email">
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
                  <form:input path="email" class="form-control"  accept-charset="ISO-8859-1"/>
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
                  <form:input path="password" type="password" class="form-control" accept-charset="ISO-8859-1"/>
               </div>
               <div class="feedback-error" id="register-feedback-email">
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
   </section>
</main>
<jsp:include page="templates/footer_new.jsp" />

