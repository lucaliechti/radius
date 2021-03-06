<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="templates/header.jsp" />


<main class="firstcontainer container">
   <section id="leftsection">
   
         <c:choose>
         <c:when test="${success == 0}">
             <div class="alert alert-danger" role="alert">
                <spring:message code="status.change.feedback.error"/>
             </div>
         </c:when>
         <c:when test="${success == 1}">
             <div class="alert alert-success" role="alert">
                <spring:message code="status.change.feedback.success"/>
            </div>
         </c:when>
         <c:otherwise>
         </c:otherwise>
      </c:choose>
      
<!-- preparing variables -->
<spring:message code="status.meeting.confirm" var="confirm" />
<c:forEach items="${commonlanguages}" var="lang" varStatus="stat">
  <c:set var="languages">${stat.first ? "" : languages}<spring:message code="language.${lang}"/>${stat.last ? "" : ","}</c:set>
</c:forEach>
<c:set var="languages">${fn:replace(languages, ',', ', ')}</c:set>
  
      <section class="leftsection-title" id="page-title">
         <h2><spring:message code="status.title"/></h2>
      </section>
      <section class="leftsection-content">
         <section class="leftsection-content-element" id="status-info">
            <div class="subsection">
            <div><h2><spring:message code="status.yourstatus"/>: <b><spring:message code="status.${user.status}"/></b></h2></div>
            <p><spring:message code="status.${user.status}.detail" arguments="${match.firstname};${match.lastname};${match.firstname};${match.lastname};${match.email};${commonlocations};${languages}" argumentSeparator=";"/><br><br>
            <c:choose>
            <c:when test="${user.status != 'MATCHED'}">
            	<a href="toggleStatus"><spring:message code="status.${user.status}.toggle"/></a><br><br>
            	<p><spring:message code="status.${user.status}.info"/></p>
            </c:when>
            <c:otherwise>
            
            <a href="#" onClick="document.getElementById('next').style.display = 'block';document.getElementById('inactive').style.display = 'none';"><spring:message code="status.MATCHED.toggle.newmatch"/></a> | 
            <a href="#" onClick="document.getElementById('inactive').style.display = 'block';document.getElementById('next').style.display = 'none';"><spring:message code="status.MATCHED.toggle.inactive"/></a><br><br>
            <div id="next" style="display: none;">
            	<p><spring:message code="status.${user.status}.confirmed" arguments="${match.firstname};${match.lastname}" argumentSeparator=";"/>
            	<form:form action="toggleStatus" method="post" modelAttribute="feedbackForm">
            		<form:hidden path="nextState" value="WAITING" />
            		<form:radiobutton path="confirmed" value="true" />
		    		<label class="radio-button-label-no">
		    			<spring:message code="question.true"/>
		    		</label>
		    		<form:radiobutton path="confirmed" value="false" />
		    		<label class="radio-button-label-no">
		    			<spring:message code="question.false"/>
		    		</label>
            		<input type="submit" class="btn btn-primary" name="new" value="${confirm}">
            		<sec:csrfInput />
            	</form:form>
            </div>
		    <div id="inactive" style="display: none;">
		    	<p><spring:message code="status.${user.status}.confirmed" arguments="${match.firstname};${match.lastname}" argumentSeparator=";"/>
		    	<form:form action="toggleStatus" method="post" modelAttribute="feedbackForm">
		    	<form:hidden path="nextState" value="INACTIVE" />
		    		<form:radiobutton path="confirmed" value="true" />
		    		<label class="radio-button-label-no">
		    			<spring:message code="question.true"/>
		    		</label>
		    		<form:radiobutton path="confirmed" value="false" />
		    		<label class="radio-button-label-no">
		    			<spring:message code="question.false"/>
		    		</label>
		    		<input type="submit" class="btn btn-primary" name="new" value="${confirm}">
		    		<sec:csrfInput />
		    	</form:form>
		    </div>
            </c:otherwise>
            </c:choose>
            </div>
         </section>
         
         <section class="leftsection-content-element" id="search-settings">
            <div class="subsection">
               <div class="editable">
                  <h2><spring:message code="status.yoursearch"/></h2>
                  <div class="editing-icons-div"><a href="answers"><i class="material-icons">create</i></a></div>
               </div>
               <p>
               <spring:message code="status.search.languages"/>: <b><c:forEach items="${user.languages}" var="item" varStatus="loop"><spring:message code="language.${item}"/>${!loop.last ? ', ' : ''}</c:forEach></b><br>
               <spring:message code="status.search.locations"/>: <b><c:forEach items="${userlocations}" var="item" varStatus="loop">${item}${!loop.last ? ', ' : ''}</c:forEach></b></p>
            </div>
         </section>
      </section>
   </section>
   <section id="rightsection">

    <jsp:include page="history.jsp" />

   </section>
</main>

<jsp:include page="templates/footer.jsp" />
