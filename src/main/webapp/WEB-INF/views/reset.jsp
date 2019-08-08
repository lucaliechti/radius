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
             <p class="success" style="margin-bottom: 1%;">
                <spring:message code="forgot.sent"/> ${emailaddress}<br>
             </p>
          </c:if>

         <h2>
            <spring:message code="reset.title"/>
         </h2>

         <!-- preparing variables -->
         <spring:message code="reset.button" var="send" />

         <form:form method="POST" action="reset" modelAttribute="passwordForm">
            <div class="form-group">
               <form:label path="password">
                  <spring:message code="reset.body"/>
               </form:label>
               <div>
                  <form:input path="password" type="password" class="form-control" accept-charset="ISO-8859-1"/>
               </div>
               <div class="feedback-error" id="reset-feedback-password">
                  <form:errors path="password"/>
               </div>
            </div>
            <input type="hidden" id="uuid" name="uuid" value="${uuid}">
            <input type="submit" class="btn btn-primary" value="${send}" />
            <sec:csrfInput />
         </form:form>
      </section>
   </section>
</main>
<jsp:include page="templates/footer.jsp" />