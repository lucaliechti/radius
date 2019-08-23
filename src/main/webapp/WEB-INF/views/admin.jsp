<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />
<script src="js/jquery.tablesort.min.js"></script>

<!-- variables -->
<spring:message code="admin.newsletter.send" var="send" />

<main class="firstcontainer container">
   <section id="leftsection">

     <c:if test="${send_success != null}">
        <p class="result success">
           <spring:message code="admin.newsletter.feedback.success.send"/>
        </p>
     </c:if>

      <section class="leftsection-title" id="page-title">
         <h2>
            <spring:message code="admin.title"/>
         </h2>
      </section>
      <section class="leftsection-content">
         <section class="leftsection-content-element" id="">
              <h2>
                 <spring:message code="admin.newsletter.title"/>
              </h2>
              <p>
                <spring:message code="admin.newsletter.number"/> ${numberRecipients}
              </p><br>
              <form:form method="POST" action="send" modelAttribute="newsletterForm">

                <div class="form-group">
                   <form:label path="subject">
                      <spring:message code="admin.newsletter.subject"/>
                   </form:label>
                   <div class="col-10">
                      <form:input path="subject" class="form-control" accept-charset="ISO-8859-1"/>
                   </div>
                   <div class="feedback-error" id="newsletter-feedback-subject">
                      <form:errors path="subject"/>
                   </div>
                </div>

                <div class="form-group">
                   <form:label path="message">
                      <spring:message code="admin.newsletter.message"/>
                   </form:label>
                   <div class="col-10">
                      <form:textarea path="message" class="input-paragraph form-control" accept-charset="ISO-8859-1"/>
                   </div>
                   <div class="feedback-error" id="newsletter-feedback-message">
                      <form:errors path="message"/>
                   </div>
                </div>

                <input type="submit" class="btn btn-primary" value="${send}" />
                <sec:csrfInput />

              </form:form>
         </section>
         <section class="leftsection-content-element" id="">
            <h2>
              <spring:message code="admin.users.title"/>
            </h2>
            <table>
                <thead>
                    <tr>
                        <th>Vorname</th>
                        <th>Name</th>
                        <th>Email bestätigt</th>
                        <th>Fragen beantwortet</th>
                        <th>Status</th>
                        <th class="no-sort">Letzte Änderung</th>
                    </tr>
                </thead>
                    <tbody>
                      <c:forEach items="${users}" var="user" >
                         <tr>
                            <td>${user.firstname}</td>
                            <td>${user.lastname}</td>
                            <td><spring:message code="question.${user.enabled}"/></td>
                            <td><spring:message code="question.${user.answered}"/></td>
                            <td><spring:message code="status.${user.status}"/></td>
                            <td><fmt:formatDate value="${user.dateModified}" type="date" dateStyle="medium"/></td>
                         </tr>
                      </c:forEach>
                    </tbody>
            </table>
         </section>
         <section class="leftsection-content-element" id="">
            <h2>
               Andere Section
            </h2>
            <p>
               Anderer Inhalt
            </p>
         </section>
      </section>
   </section>

   <section id="rightsection">
      <section class="login-resume">
      Inhalt
      </section>

      <section id="profile-deco" class="profile-resume">
      Anderer Inhalt
      </section>
   </section>
</main>

<script>$('table').tablesort();</script>
<jsp:include page="templates/footer.jsp" />

