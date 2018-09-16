<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header_new.jsp" />

<main class="firstcontainer container">
   <section id="leftsection">
      <section class="leftsection-title" id="page-title">         
         <h2>
            <spring:message code="support.thankyou"/>
         </h2>
      </section>
      
	<section class="leftsection-content">
	   <section class="leftsection-content-element" id="">
	      <h2>
	         <spring:message code="support.helpers.title"/>
	      </h2>
	      <p>
	         <spring:message code="support.helpers.detail"/> <spring:message code="support.helpers"/>
	      </p>
	   </section>
	   <section class="leftsection-content-element" id="">
	      <h2>
	         <spring:message code="support.crowdfunding.title"/>
	      </h2>
	      <p>
	         <spring:message code="support.crowdfunding.detail"/> <spring:message code="support.crowdfunding"/>
	      </p>
	   </section>
	</section>

   </section>
      <section id="broadrightsection">
      <section>
         <h2>
            <spring:message code="support.title"/>
         </h2>
         <p><spring:message code="support.intro"/></p><br>
         <p><b><spring:message code="support.iban"/>:</b> <spring:message code="support.iban.iban"/>
         <p><b><spring:message code="support.receiver"/>:</b> <spring:message code="support.receiver.detail"/><br><br>
         <spring:message code="support.thanks"/>
      </section>
   </section>
</main>

<jsp:include page="templates/footer_new.jsp" />
