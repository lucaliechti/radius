<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>



      <section id="profile-name" class="profile-resume">
         <div class="editable-rightsection">
            <h2><spring:message code="status.profile" /></h2>
            <div class="editing-icons-div"><a href="answers"><img src="img/icon-edit.png" class="editing-icons"></a></div><!-- <a href="#"><img src="img/icon-logout.png" class="editing-icons"></a>--></div>
         </div>
         <p> ${user.firstname} ${user.lastname}
         </p>
      </section>
      <section id="profile-history" class="profile-resume">
         <h2><spring:message code="status.history" /></h2>
<c:choose>
<c:when test="${history.size() == 0}">
<p><spring:message code="profile.history.none"/>
</c:when>
<c:otherwise>
<c:forEach items="${history}" var="meeting" varStatus="loop"><p><fmt:formatDate value="${meeting.dateCreated()}" type="date" dateStyle="medium"/>: <b>${meeting.email2()}</b><br><br></c:forEach>

</c:otherwise>
</c:choose>
      </section>