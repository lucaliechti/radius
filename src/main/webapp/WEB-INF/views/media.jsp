<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />

<main class="firstcontainer container">
    <section id="leftsection">
        <section class="leftsection-title" id="page-title">
            <h2>
                <spring:message code="media.title"/>
            </h2>
        </section>

        <section class="leftsection-content">
            <section class="leftsection-content-element" id="media">
                <h2>
                    <spring:message code="media.section.title"/>
                </h2>
                <p>
                    <spring:message code="media.link.neo1"/>
                </p>
                <p>
                    <spring:message code="media.link.20min"/>
                </p>
                <p>
                    <spring:message code="media.link.toxic"/>
                </p>
                <p>
                    <spring:message code="media.link.radio32"/>
                </p>
                <p>
                    <spring:message code="media.link.rtn"/>
                </p>
            </section>
        </section>
    </section>
    <section id="rightsection" class="broad">
        <section class="leftsection-content-element" id="releases">
            <h2>
                <spring:message code="media.release.title"/>
            </h2>
            <p>
                <spring:message code="media.release.20200107"/>
            </p>
            <p>
                <spring:message code="media.release.20180917"/>
            </p>
            <p>
                <spring:message code="media.release.20180806"/>
            </p>
        </section>
        <section class="leftsection-content-element" id="request">
            <h2>
                <spring:message code="media.contact.title"/>
            </h2>
            <p>
                <spring:message code="media.contact.detail"/>
            </p>
        </section>
    </section>
</main>

<jsp:include page="templates/footer.jsp" />
