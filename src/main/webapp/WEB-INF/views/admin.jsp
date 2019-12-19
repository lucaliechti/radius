<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css"/>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/select/1.3.1/css/select.dataTables.min.css"/>
<script type="text/javascript" src="https://cdn.datatables.net/select/1.3.1/js/dataTables.select.min.js"></script>

<script>
$(document).ready(function() {
  $('table').DataTable({
    "lengthMenu": [ 20, 50, 100 ]
  });
});
</script>

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

         <section class="leftsection-content-element" id="newsletter">
              <h2>
                 <spring:message code="admin.newsletter.title"/>
              </h2>
              <p>
                <spring:message code="admin.newsletter.number"/> ${numberRecipients}
              </p>
         </section>
         <section class="leftsection-content-element" id="users">
            <h2>
              <spring:message code="admin.users.title"/>
            </h2>
            <table id="usertable" class="table table-striped table-bordered table-sm">
                <thead>
                    <tr>
                        <th>Vorname</th>
                        <th>Name</th>
                        <th>Email bestätigt</th>
                        <th>Fragen beantwortet</th>
                        <th>Status</th>
                        <th>Letzte Änderung</th>
                    </tr>
                </thead>
                    <tbody>
                      <c:forEach items="${users}" var="user" >
                         <tr>
                            <td>${user.firstname}</td>
                            <td>${user.lastname}</td>
                            <td><spring:message code="question.${user.enabled}"/></td>
                            <td><spring:message code="status.${user.status}"/></td>
                            <td><fmt:formatDate value="${user.dateModified}" type="date" dateStyle="medium"/></td>
                         </tr>
                      </c:forEach>
                    </tbody>
            </table>
         </section>
         <section class="leftsection-content-element" id="">
            <h2>
               Im Moment ist Abstimmung: <spring:message code="question.${special}"/>
            </h2>
            <p>
               Anzahl Abstimmungen: ${nrvotes}
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

<jsp:include page="templates/footer.jsp" />

