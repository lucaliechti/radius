<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />

<!-- preparing variables -->
<spring:message code="profile.delete" var="delete" />
<spring:message code="profile.inactive" var="inactive" />
<spring:message code="profile.delete.confirm" var="confirm" />


<main class="firstcontainer container">
<section id="leftsection">

         <c:if test="${delete_failed != null}">
            <p class="result error">
               <spring:message code="profile.feedback.error.deletefailed"/><br>
            </p>
         </c:if>
         
<section id="leftsection-content">
   <section class="leftsection-title" id="page-title">    
      <h2>
         <spring:message code="profile.title"/>
      </h2>
      <!-- 	
         <div class="editing-icons-div"><a href="#"><img src="img/icon-edit.png" class="editing-icons"></a>				
         <a href="#"><img src="img/icon-logout.png" class="editing-icons"></a></div>-->
   </section>
   <section class="leftsection-content">
   <section class="leftsection-content-element" id="">
      <h2><spring:message code="profile.personaldata"/></h2>
   	  <!-- <div class="editing-icons-div"><a href="answers"><img src="img/icon-edit.png" class="editing-icons"></a></div> -->
   
   <p><b><spring:message code="profile.fn"/>:</b> ${firstName}
   <p><b><spring:message code="profile.ln"/>:</b> ${lastName}
   <p><b><spring:message code="profile.email"/>:</b> ${email}
   <p><b><spring:message code="profile.canton"/>:</b> <c:if test="${canton != null}"><spring:message code="canton.${canton}"/></c:if>
   <p><b><spring:message code="profile.motivation"/>:</b> ${motivation}	
   </section>
   <section class="leftsection-content-element" id="">
      <div class="editable">
      <h2><spring:message code="profile.radiusdata"/></h2>
      <div class="editing-icons-div"><a href="answers"><img src="img/icon-edit.png" class="editing-icons"></a></div>
   </div>

   <c:forEach begin="1" end="${nrQ}" varStatus="loop">
      <p><b><spring:message code="q${loop.index}"/></b>
         <br>
         <spring:message code="question.${answers[loop.index-1]}"/>
   </c:forEach>

   </section>	
   <section class="leftsection-content-element" id="">
         <div class="editable">
      <h2>
         <spring:message code="profile.additionaldata"/>
      </h2>
      <div class="editing-icons-div"><a href="answers"><img src="img/icon-edit.png" class="editing-icons"></a></div>
   </div>
   
   <p><b><spring:message code="profile.languages"/>:</b> <c:forEach items="${languages}" var="item" varStatus="loop"><spring:message code="language.${item}"/>${!loop.last ? ', ' : ''}</c:forEach>
   <p><b><spring:message code="profile.modus"/>:</b> <spring:message code="modus.${modus}"/>
   <p><b><spring:message code="profile.locations"/>:</b> <c:forEach items="${locations}" var="item" varStatus="loop">${item}${!loop.last ? ', ' : ''}</c:forEach>
   </section>
   <section class="leftsection-content-element" id="">
   <div class="editable">
      <h2><spring:message code="profile.delete"/></h2>
   </div>
   <form action="<c:url value='/delete' />" method="post">
   	  <input type="submit" class="btn btn-primary" value='${delete}' onClick="return confirm('${confirm}');">
   	  <sec:csrfInput />
   </form><p><spring:message code="profile.delete.detail"/></p>
   </section>
</section>
</section>
</section>
<section id="rightsection"><!-- 
   <section id="profile-history" class="profile-resume">
      <h2><spring:message code="profile.history"/></h2>
      <p><spring:message code="profile.history.none"/></p>
   </section>
   <section id="profile-experiences" class="profile-resume">
      <h2><spring:message code="profile.experiences"/></h2>
      <p><spring:message code="profile.experiences.none"/></p>
   </section> -->
   <jsp:include page="history.jsp" />
</section>

</main>
<jsp:include page="templates/footer.jsp" />