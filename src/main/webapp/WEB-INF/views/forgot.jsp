<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<jsp:include page="templates/header.jsp" />
<main class="firstcontainer container">
   <section class="fullwidth-section">
      <section class="leftsection-content-element" id="">

          <c:if test="${sent != null}">
             <p class="result success">
                <spring:message code="forgot.sent"/> ${emailaddress}<br>
             </p>
          </c:if>

         <h2>
            <spring:message code="forgot.title"/>
         </h2>

         <!-- preparing variables -->
         <spring:message code="forgot.button" var="send" />

         <form:form method="POST" action="forgot" modelAttribute="emailForm">
            <div class="form-group">
               <form:label path="email">
                  <spring:message code="forgot.body"/>
               </form:label>
               <div>
                  <form:input path="email" class="form-control"/>
               </div>
               <div class="feedback-error" id="reset-feedback-email">
                  <form:errors path="email"/>
               </div>
            </div>
            <input type="submit" class="btn btn-primary" value="${send}" />
            <sec:csrfInput />
         </form:form>
      </section>
   </section>
</main>
<jsp:include page="templates/footer.jsp" />