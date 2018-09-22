<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header_new.jsp" />


<main class="firstcontainer container">
   <section id="leftsection">
   
         <c:choose>
         <c:when test="${success == 0}">
           	<p class="error" style="margin-bottom: 1%;"><spring:message code="status.change.feedback.error"/><br></p>
         </c:when>
         <c:when test="${success == 1}">
            <p class="success" style="margin-bottom: 1%;"><spring:message code="status.change.feedback.success"/><br></p>
         </c:when>
         <c:otherwise>
         </c:otherwise>
      </c:choose>
      
<!-- preparing variables -->
<spring:message code="status.meeting.confirm" var="confirm" />
<spring:message code="status.modi.${modi}" var="compatiblemodi" />
<c:forEach items="${commonlanguages}" var="lang" varStatus="stat">
  <c:set var="languages">${stat.first ? "" : languages}${stat.last ? "" : ", "}<spring:message code="language.${lang}"/></c:set>
</c:forEach>
  
      <section class="leftsection-title" id="page-title">
         <h2><spring:message code="status.title"/></h2>
      </section>
      <section class="leftsection-content">
         <section class="leftsection-content-element" id="status-info">
         <div class="subsection">
         	<c:choose>
         	<c:when test="${user.status != 'INACTIVE'}"> <!--  there should be some more and different icons, e.g. one for each status  -->
         	<img src="img/icon-done.png" class="illustration-info">
         	</c:when>
         	<c:otherwise>
         	<img src="img/icon-info.png" class="illustration-info">
         	</c:otherwise>
         	</c:choose>
         	</div>
            <div class="subsection">
            <div class="editable"><h2><spring:message code="status.yourstatus"/>: <b><spring:message code="status.${user.status}"/></b></h2></div>
            <p><spring:message code="status.${user.status}.detail" arguments="${match.firstname};${match.lastname};${match.firstname};${match.lastname};${match.email};${commonlocations};${languages};${compatiblemodi}" argumentSeparator=";"/><br><br>
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
            	<form:form action="toggleStatus" method="post" commandName="feedbackForm">
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
		    	<form:form action="toggleStatus" method="post" commandName="feedbackForm">
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
            <div class="subsection"><img src="img/icon-info.png" class="illustration-info">
            </div>
            <div class="subsection">
               <div class="editable">
                  <h2><spring:message code="status.yoursearch"/></h2> <img src="img/icon-edit.png" class="editing-icons">
               </div>
               <p><spring:message code="status.search.modus"/>: <b><spring:message code="modus.${user.modus}"/></b><br>
               <spring:message code="status.search.languages"/>: <b><c:forEach items="${user.languages}" var="item" varStatus="loop"><spring:message code="language.${item}"/>${!loop.last ? ', ' : ''}</c:forEach></b><br>
               <spring:message code="status.search.locations"/>: <b><c:forEach items="${userlocations}" var="item" varStatus="loop">${item}${!loop.last ? ', ' : ''}</c:forEach></b></p>
            </div>
         </section>
      </section>
   </section>
   <section id="rightsection">
      <section id="profile-name" class="profile-resume">
         <div class="editable-rightsection">
            <h2><spring:message code="status.profile" /></h2><div><a href="#"><img src="img/icon-edit.png" class="editing-icons"></a><!-- <a href="#"><img src="img/icon-logout.png" class="editing-icons"></a>--></div>
         </div>
         <p> ${user.firstname} ${user.lastname}
         </p>
      </section>
      <section id="profile-history" class="profile-resume">
         <h2>Verlauf</h2>
         <p> bla <br>bla <br>bla <br>bla <br>
         </p>
         <input type="submit"  value="show all"/>
      </section>
      <section id="profile-experiences" class="profile-resume">
         <h2>veröffentlichte Erfahrungen</h2>
         <p>bla <br>bla <br>bla <br>bla <br>
         </p>
         <input type="submit"  value="show all"/>
      </section>
   </section>
</main>





<jsp:include page="templates/footer_new.jsp" />