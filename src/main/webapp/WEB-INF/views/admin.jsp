<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css"/>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/select/1.3.1/css/select.dataTables.min.css"/>
<script type="text/javascript" src="https://cdn.datatables.net/select/1.3.1/js/dataTables.select.min.js"></script>

<script>
    $(document).ready(function() {
        var recipientUsers = $('#recipients_users');
        var recipientNewsletter = $('#recipients_newsletter');

        var usertable = $('#usertable').DataTable({
            "columnDefs": [ {
                orderable: false,
                className: 'select-checkbox',
                targets:   0
            } ],
            "lengthMenu": [ 10, 20, 50, 100 ],
            "select": {
                style:    'multi',
                selector: 'td:first-child'
            }
        });
        usertable
            .on( 'select', function () { updateSelectedRows(usertable, USER_EMAIL_COLUMN, recipientUsers); } )
            .on( 'deselect', function () { updateSelectedRows(usertable, USER_EMAIL_COLUMN, recipientUsers); } );

        var newslettertable = $('#newslettertable').DataTable({
            "columnDefs": [ {
                orderable: false,
                className: 'select-checkbox',
                targets:   0
            } ],
            "lengthMenu": [ 10, 20, 50, 100 ],
            "select": {
                style:    'multi',
                selector: 'td:first-child'
            }
        });
        newslettertable
            .on( 'select', function () { updateSelectedRows(newslettertable, NEWSLETTER_EMAIL_COLUMN, recipientNewsletter); } )
            .on( 'deselect', function () { updateSelectedRows(newslettertable, NEWSLETTER_EMAIL_COLUMN, recipientNewsletter); } );
    });
    var USER_EMAIL_COLUMN = 3;
    var USER_STATUS_COLUMN = 5;
    var NEWSLETTER_EMAIL_COLUMN = 1;
    var NEWSLETTER_SOURCE_COLUMN = 2;

    function updateSelectedRows(table, emailColumn, recipientField) {
        recipientSet = new Set();
        var selectedRows = table.rows( { selected: true } );
        var indexArray = selectedRows.indexes().toArray();
        for (var i = 0; i < indexArray.length; i++) {
            recipientSet.add(table.row(indexArray[i]).data()[emailColumn]);
        }
        recipientField.val([...recipientSet].join(';'));
    }

    function selectDeselect(isSelected, table, attr, attributeColumn) {
        var rows = table.data().toArray();
        for(var i = 0; i < rows.length; i++) {
            if(rows[i][attributeColumn] === attr) {
                isSelected ? table.rows(i).select() : table.rows(i).deselect();
            }
        }
    }
</script>

<!-- variables -->
<spring:message code="admin.newsletter.send" var="send" />

<main class="firstcontainer container">
    <section id="leftsection" style="max-width: 1140px;margin: 0 auto;">

        <c:if test="${send_success != null}">
            <p class="result success">
                <spring:message code="admin.newsletter.feedback.success.send"/>
            </p>
        </c:if>

        <section class="leftsection-title" id="page-title">
            <h2>
                <spring:message code="admin.title"/>
            </h2>
        </section>

        <section class="leftsection-content">
            <section class="leftsection-content-element" id="newsletter">
                <h2>
                    <spring:message code="admin.newsletter.title"/>
                </h2>
                <p>
                    <input type="checkbox" id="checkbox_website" onclick="selectDeselect(
                        $('#checkbox_website').prop('checked'),
                        $('#newslettertable').DataTable(),
                        'Website',
                        NEWSLETTER_SOURCE_COLUMN);"> Website</input>
                </p>
                <p>
                    <input type="checkbox" id="checkbox_crowdfunding" onclick="selectDeselect(
                        $('#checkbox_crowdfunding').prop('checked'),
                        $('#newslettertable').DataTable(),
                        'Crowdfunding',
                        NEWSLETTER_SOURCE_COLUMN);"> Crowdfunding</input>
                </p>
                <p style="margin-bottom: 20px;">
                    <input type="checkbox" id="checkbox_survey" onclick="selectDeselect(
                        $('#checkbox_survey').prop('checked'),
                        $('#newslettertable').DataTable(),
                        'Survey Winter 2019/2020',
                        NEWSLETTER_SOURCE_COLUMN);"> Survey</input>
                </p>
                <table id="newslettertable" class="table table-striped table-bordered table-sm">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Email</th>
                            <th>Source</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${newsletterRecipients}" var="recipient" >
                        <tr>
                            <td></td>
                            <td>${recipient.email}</td>
                            <td>${recipient.source}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <form:form method="POST" action="sendNewsletter" modelAttribute="newsletterForm">
                    <form:input path="subject" placeholder="Subject" style="margin-top: 20px;"/>
                    <form:errors path="subject"/>
                    <form:textarea path="message" placeholder="Message"/>
                    <form:errors path="message"/>
                    <form:hidden path="recipients"  id="recipients_newsletter"/>
                    <form:errors path="recipients"/>
                    <input type="submit" class="btn btn-primary" value="Answer" />
                    <sec:csrfInput />
                </form:form>
            </section>

            <section class="leftsection-content-element" id="users">
                <h2>
                    <spring:message code="admin.users.title"/>
                </h2>
                <p>
                    <input type="checkbox" id="checkbox_waiting" onclick="selectDeselect(
                        $('#checkbox_waiting').prop('checked'),
                        $('#usertable').DataTable(),
                        'Aktiv',
                        USER_STATUS_COLUMN);"> waiting</input>
                </p>
                <p>
                    <input type="checkbox" id="checkbox_inactive" onclick="selectDeselect(
                        $('#checkbox_inactive').prop('checked'),
                        $('#usertable').DataTable(),
                        'Inaktiv',
                        USER_STATUS_COLUMN);"> inactive</input>
                </p>
                <p style="margin-bottom: 20px;">
                    <input type="checkbox" id="checkbox_matched" onclick="selectDeselect(
                        $('#checkbox_matched').prop('checked'),
                        $('#usertable').DataTable(),
                        'Partner/in gefunden!',
                        USER_STATUS_COLUMN);"> matched</input>
                </p>

                <table id="usertable" class="table table-striped table-bordered table-sm">
                    <thead>
                    <tr>
                        <th></th>
                        <th>Vorname</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Email bestätigt</th>
                        <th>Status</th>
                        <th>Letzte Änderung</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${users}" var="user" >
                        <tr>
                            <td></td>
                            <td>${user.firstname}</td>
                            <td>${user.lastname}</td>
                            <td>${user.email}</td>
                            <td><spring:message code="question.${user.enabled}"/></td>
                            <td><spring:message code="status.${user.status}"/></td>
                            <td><fmt:formatDate value="${user.dateModified}" pattern = "yyyy-MM-dd"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <form:form method="POST" action="contactUsers" modelAttribute="contactUserForm">
                    <form:input path="subject" placeholder="Subject" style="margin-top: 20px;"/>
                    <form:errors path="subject"/>
                    <form:textarea path="message" placeholder="Message"/>
                    <form:errors path="message"/>
                    <form:hidden path="recipients"  id="recipients_users"/>
                    <form:errors path="recipients"/>
                    <input type="submit" class="btn btn-primary" value="Answer" />
                    <sec:csrfInput />
                </form:form>

            </section>
            <section class="leftsection-content-element" id="">
                <h2>
                    Im Moment ist Abstimmung: <spring:message code="question.${special}"/>
                </h2>
                <p>
                    Anzahl Abstimmungen: ${nrvotes}
                </p>
            </section>
        </section>
    </section>
    <!--
    <section id="rightsection">
        <section class="login-resume">
            Inhalt
        </section>

        <section id="profile-deco" class="profile-resume">
            Anderer Inhalt
        </section>
    </section>
    -->
</main>

<jsp:include page="templates/footer.jsp" />

