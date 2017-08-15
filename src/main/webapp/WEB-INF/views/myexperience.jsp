<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />

<script> $('#nav-account').addClass('active'); </script>

<h1><spring:message code="myexperience.title"/></h1><br>

<a href="<c:url value='/writeexperience' />"><spring:message code="myexperience.writeExperience"/></a><p>

<jsp:include page="templates/footer.jsp" />