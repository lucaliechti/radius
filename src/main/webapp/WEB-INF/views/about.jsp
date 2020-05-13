<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="templates/header.jsp" />

<main class="firstcontainer container">
   <section id="leftsection">
      <section class="leftsection-title" id="page-title">         
         <h2>
            <spring:message code="about.title"/>
         </h2>
      </section>
      
	<section class="leftsection-content">
	   <section class="leftsection-content-element" id="">
	      <h2>
	         <spring:message code="about.works"/>
	      </h2>
	      <p>
	         <spring:message code="about.works.detail"/>
	      </p>
	   </section>
	   <section class="leftsection-content-element" id="">
	      <h2>
	         <spring:message code="about.rules"/>
	      </h2>
	      <p>
	         <spring:message code="about.rules.detail"/>
	      </p>
	   </section>
	</section>

   </section>
      <section id="rightsection" class="broad">
      <section>
         <h2>
            <spring:message code="about.legal"/>
         </h2>
         <p><spring:message code="about.legal.detail"/></p>
      </section>
   </section>
</main>

<jsp:include page="templates/footer.jsp" />
