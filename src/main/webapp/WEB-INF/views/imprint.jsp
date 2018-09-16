<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header_new.jsp" />

<main class="firstcontainer container">
   <section class="fullwidth-section">
         <section class="leftsection-content-element" id="">
            <h2>
               <spring:message code="imprint.title"/>
            </h2>
            <p>
               <spring:message code="imprint.imprint"/>
            </p>
         </section>
      </section>
</main>

<jsp:include page="templates/footer_new.jsp" />
