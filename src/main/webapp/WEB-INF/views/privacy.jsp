<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="templates/header.jsp" />

<main class="firstcontainer container">
   <section id="fullwidth-section">
         <section class="leftsection-content-element" id="">
            <h2><spring:message code="privacy.title"/></h2>
            <p><spring:message code="privacy.main"/></p><br>
            <p><b><spring:message code="privacy.info.title"/></b></p><br>
            <p><spring:message code="privacy.info.detail"/></p><br>
            <p><b><spring:message code="privacy.cookies.title"/></b></p><br>
            <p><spring:message code="privacy.cookies.detail"/></p><br>
            <p><b><spring:message code="privacy.log.title"/></b></p><br>
            <p><spring:message code="privacy.log.detail"/></p><br>            
            <p><b><spring:message code="privacy.location.title"/></b></p><br>
            <p><spring:message code="privacy.location.detail"/></p><br>
            <p><b><spring:message code="privacy.external.title"/></b></p><br>
            <p><spring:message code="privacy.external.detail"/></p>
         </section>
      </section>
</main>

<jsp:include page="templates/footer.jsp" />
