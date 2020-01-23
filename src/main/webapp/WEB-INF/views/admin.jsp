<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="templates/header.jsp" />
<!-- Leaflet -->
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.3/dist/leaflet.css"><link/>
<script src="https://unpkg.com/leaflet@1.3.3/dist/leaflet.js"></script>
<!-- DataTables -->
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css"/>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/select/1.3.1/css/select.dataTables.min.css"/>
<script type="text/javascript" src="https://cdn.datatables.net/select/1.3.1/js/dataTables.select.min.js"></script>
<!-- other stuff -->
<link rel="stylesheet" type="text/css" href="/css/admin.css"/>
<script type="text/javascript" src="/js/admin.js"></script>
<script type="text/javascript" src="/js/ms.js"></script>

<script>
    regionalDensities = regionDensityAsJS();

    function regionDensityAsJS() {
        jsArray = [];
        <c:forEach items="${regionDensity}" var="answer">
        jsArray.push(${answer});
        </c:forEach>
        return jsArray;
    }

    function getCount(id) {
        return regionalDensities[id-1];
    }

    var map;

    function prepareMap() {
        map = L.map('map').setView([46.75, 8.25], 8);

        var tl = L.tileLayer('https://cartodb-basemaps-{s}.global.ssl.fastly.net/light_all/{z}/{x}/{y}{r}.png', {
            maxZoom: 8,
            minZoom: 8,
            maxBoundsViscosity: 1,
            attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> &copy; <a href="http://cartodb.com/attributions">CartoDB</a>'
        }).addTo(map);

        map.setMaxBounds([[45.22, 4.95], [48.24, 11.55]]);

        function style(feature) {
            return {
                fillColor: getColor(getCount(feature.id)),
                weight: 2,
                opacity: 1,
                color: 'white',
                dashArray: '3',
                fillOpacity: 0.7
            };
        }

        gl = L.geoJson(ms, {
            style: style
        }).addTo(map);

        function getColor(d) {
            return d >= 100 ? '#800026' :
                d >= 50  ? '#BD0026' :
                d >= 20  ? '#E31A1C' :
                d >= 10  ? '#FC4E2A' :
                d >= 5   ? '#FD8D3C' :
                d >= 2   ? '#FEB24C' :
                d >= 1   ? '#FED976' :
                '#FFEDA0';
        }

        var legend = L.control({position: 'topright'});
        legend.onAdd = function (map) {
            var div = L.DomUtil.create('div', 'info maplegend'),
                grades = [0, 1, 2, 5, 10, 20, 50, 100],
                labels = [];

            for (var i = 0; i < grades.length; i++) {
                div.innerHTML +=
                    '<i style="background:' + getColor(grades[i]) + '"></i> ' +
                    grades[i] + (grades[i + 1] ? '&ndash;' + grades[i + 1] + '<br>' : '+');
            }
            return div;
        };
        legend.addTo(map);
    }
</script>

<script>
    $(document).ready(function() {
        $('.value').each(function() {
            var givenAnswers = $(this).text();
            $(this).parent().css('width', WIDTH_PER_ANSWER*givenAnswers);
        });
        for(i = 0; i < survey.length; i++) {
            document.getElementById('survey_' + i).innerHTML = survey[i];
        }
        <c:choose>
        <c:when test="${activetab != null}">
            document.getElementById("tab_${activetab}").click();
        </c:when>
        <c:otherwise>
            document.getElementById("tab_statistics").click();
        </c:otherwise>
        </c:choose>
    });

    var survey = [
        "Keine Kriegsmaterialexporte",
        "Weltweites Niederlassungsrecht",
        "Wehrdienst ersetzen",
        "70 Wochen Elternzeit",
        "Cannabis legalisieren",
        "30-Stunden-Woche",
        "BGE",
        "Rentenalter 65 für Frauen",
        "Mikrosteuer auf Finanztransaktionen",
        "Ausländerstimmrecht",
        "Parteienfinanzierung offenlegen",
        "Umweltabgabe Fleisch",
        "Atomausstieg vorziehen",
        "Umweltabgabe fliegen",
        "Landschaftsschutz lockern"
    ];

    function maxAnswers() {
        let max = 0;
        <c:forEach items="${surveyStats}" var="answer" varStatus="loop">
            var currentSum = ${answer[0] + answer[1] + answer[2]};
            if(currentSum > max) {
                max = currentSum;
            }
        </c:forEach>
        return max;
    }

    var MAXIMUM_WIDTH = 700;
    var MAXIMUM_ANSWERS = maxAnswers();
    var WIDTH_PER_ANSWER = MAXIMUM_WIDTH/MAXIMUM_ANSWERS;

    function showSection(evt, sectionName) {
        var i, tabcontent, tablinks;
        tabcontent = document.getElementsByClassName("tabcontent");
        for (i = 0; i < tabcontent.length; i++) {
            tabcontent[i].style.display = "none";
        }
        tablinks = document.getElementsByClassName("tablinks");
        for (i = 0; i < tablinks.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" active", "");
        }
        document.getElementById(sectionName).style.display = "block";
        evt.currentTarget.className += " active";
    }

    function enableDisableForm(configField){
        if($('#' + configField).is("[disabled]")) {
            $('#' + configField).removeAttr("disabled")
        } else {
            $('#' + configField).attr('disabled', 'disabled');
        }
    }
</script>

<!-- variables -->
<spring:message code="admin.newsletter.send" var="send" />
<c:set var="phde">🥔🥔🥔</c:set>
<c:set var="phfr">🥐🥐🥐</c:set>
<c:set var="phen">🥫🥫🥫</c:set>

<main class="firstcontainer container">
    <section class="section-admin" id="leftsection" style="max-width: 1140px;margin: 0 auto;margin-bottom: 20px;">

        <c:if test="${success != null}">
            <p class="result success">
                 Aktion erfolgreich ausgeführt.<br>
            </p>
        </c:if>

        <c:if test="${failure != null}">
            <p class="result error">
                Ein Fehler ist aufgetreten. Versuchen Sie es nochmal.<br>
            </p>
        </c:if>

        <c:if test="${successfullySent != null}">
            <p class="result success">
                ${successfullySent} Mails wurden erfolgreich gesendet.<br>
            </p>
        </c:if>

        <c:if test="${failedRecipients != null}">
            <p class="result error">
                Mails an folgende Adressen konnten nicht zugestellt werden:<br> ${failedRecipients}
            </p>
        </c:if>

        <c:if test="${configupdate_success != null}">
            <p class="result success">
                Configuration updated successfully.<br>
            </p>
        </c:if>

        <section class="leftsection-title" id="page-title">
            <div class="tab">
                <button class="tablinks" id="tab_statistics" onclick="showSection(event, 'statistics')">Statistics</button>
                <button class="tablinks" id="tab_newsletter" onclick="showSection(event, 'newsletter')">Newsletter</button>
                <button class="tablinks" id="tab_users" onclick="showSection(event, 'users')">Users</button>
                <button class="tablinks" id="tab_survey" onclick="showSection(event, 'survey')">Survey</button>
                <button class="tablinks" id="tab_registrationmap" onclick="showSection(event, 'registrationmap'); if(!map) { prepareMap() }">Geography</button>
                <button class="tablinks" id="tab_matches" onclick="showSection(event, 'matches')">Matches</button>
                <button class="tablinks" id="tab_config" onclick="showSection(event, 'config')">Configuration</button>
                <button class="tablinks" id="tab_questions" onclick="showSection(event, 'questions')">Questions</button>
                <button class="tablinks" id="tab_press" onclick="showSection(event, 'press')">Press</button>
                <button class="tablinks" id="tab_news" onclick="showSection(event, 'news')">News</button>
            </div>
        </section>

        <section class="leftsection-content">
            <section class="tabcontent" id="statistics">
                <h2>
                    Statistics
                </h2>
                <table>
                    <thead>
                    <tr>
                        <th style="width: 100px;"></th>
                        <th style="width: 65px;text-align: right;">active</th>
                        <th style="width: 65px;text-align: right;">total</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Users</td>
                        <td style="text-align: right;">${statistics.usersActive}</td>
                        <td style="text-align: right;">${statistics.usersTotal}</td>
                    </tr>
                    </tbody>
                </table>
                <table>
                    <thead>
                        <tr>
                            <th style="width: 100px;"></th>
                            <th style="width: 65px;text-align: right;">7d</th>
                            <th style="width: 65px;text-align: right;">30d</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Registrations</td>
                            <td style="text-align: right;">${statistics.registrationsWeek}</td>
                            <td style="text-align: right;">${statistics.registrationsMonth}</td>
                        </tr>
                        <tr>
                            <td>Matches</td>
                            <td style="text-align: right;">${statistics.matchesWeek}</td>
                            <td style="text-align: right;">${statistics.matchesMonth}</td>
                        </tr>
                        <tr>
                            <td>Users online</td>
                            <td style="text-align: right;">${statistics.onlineWeek}</td>
                            <td style="text-align: right;">${statistics.onlineMonth}</td>
                        </tr>
                    </tbody>
                </table><br>
                <div class="feedback-error mobileonly" id="admin-mobile-hint">
                    Log in to the desktop version to see the full admin panel.
                </div>
            </section>
            <section class="tabcontent" id="newsletter">
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
                            <th>Delete</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${newsletterRecipients}" var="recipient" >
                            <tr>
                                <td></td>
                                <td>${recipient.email}</td>
                                <td>${recipient.source}</td>
                                <td>
                                    <a href="<c:url value='/edit/unsubscribe?uuid=${recipient.uuid}'/>" class="adminbutton delete" onClick="return confirm('Diese Aktion kann nicht rückgängig gemacht werden. Sicher?');">delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <form:form method="POST" action="/mail/newsletter" modelAttribute="newsletterForm">
                    <form:input path="subject" placeholder="Subject" style="margin-top: 20px;"/>
                    <form:errors path="subject"/>
                    <form:textarea path="message" placeholder="Message"/>
                    <form:errors path="message"/>
                    <form:hidden path="recipients"  id="recipients_newsletter"/>
                    <form:errors path="recipients"/>
                    <p>
                        <form:radiobutton path="language" value="de" checked="checked"/> Deutsch
                    </p>
                    <p>
                        <form:radiobutton path="language" value="fr"/> Französisch
                    </p>
                    <p>
                        <form:radiobutton path="language" value="en"/> Englisch
                    </p>
                    <input type="submit" class="btn btn-primary" value="Send" />
                    <sec:csrfInput />
                </form:form>
            </section>

            <section class="tabcontent" id="users">
                <h2>
                    <spring:message code="admin.users.title"/>
                </h2>
                <p>
                    <input type="checkbox" id="checkbox_waiting" onclick="selectDeselect(
                        $('#checkbox_waiting').prop('checked'),
                        $('#usertable').DataTable(),
                        'WAITING',
                        USER_STATUS_COLUMN);"> waiting</input>
                </p>
                <p>
                    <input type="checkbox" id="checkbox_inactive" onclick="selectDeselect(
                        $('#checkbox_inactive').prop('checked'),
                        $('#usertable').DataTable(),
                        'INACTIVE',
                        USER_STATUS_COLUMN);"> inactive</input>
                </p>
                <p style="margin-bottom: 20px;">
                    <input type="checkbox" id="checkbox_matched" onclick="selectDeselect(
                        $('#checkbox_matched').prop('checked'),
                        $('#usertable').DataTable(),
                        'MATCHED',
                        USER_STATUS_COLUMN);"> matched</input>
                </p>

                <table id="usertable" class="table table-striped table-bordered table-sm">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Vorname</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Status</th>
                            <th>Letzte Änderung</th>
                            <th>Privat</th>
                            <th>Sperren</th>
                            <th>Löschen</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${users}" var="user" >
                            <tr>
                                <td></td>
                                <td>${user.firstname}</td>
                                <td>${user.lastname}</td>
                                <td>${user.email}</td>
                                <td>${user.status}</td>
                                <td><fmt:formatDate value="${user.dateModified}" pattern = "yyyy-MM-dd"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${user.privateUser}">👪👪</c:when>
                                        <c:otherwise>
                                            <a href="<c:url value='/edit/private?uuid=${user.uuid}'/>" class="adminbutton private" onClick="return confirm('Diese Aktion kann nur manuell rückgängig gemacht werden. Sicher?');">privat</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${user.banned}">🔒🔒</c:when>
                                        <c:otherwise>
                                            <a href="<c:url value='/edit/ban?uuid=${user.uuid}'/>" class="adminbutton ban" onClick="return confirm('Diese Aktion kann nur manuell rückgängig gemacht werden. Sicher?');">sperren</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="<c:url value='/edit/delete?uuid=${user.uuid}'/>" class="adminbutton delete" onClick="return confirm('Diese Aktion kann nicht rückgängig gemacht werden. Sicher?');">löschen</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <form:form method="POST" action="/mail/users" modelAttribute="contactUserForm">
                    <form:input path="subject" placeholder="Subject" style="margin-top: 20px;"/>
                    <form:errors path="subject"/>
                    <p><b>Liebe/r User/in,</b></p><br>
                    <form:textarea path="message" placeholder="Message"/>
                    <form:errors path="message"/>
                    <form:hidden path="recipients"  id="recipients_users"/>
                    <form:errors path="recipients"/>
                    <p>
                        <form:radiobutton path="language" value="de" checked="checked"/> Deutsch
                    </p>
                    <p>
                        <form:radiobutton path="language" value="fr"/> Französisch
                    </p>
                    <p>
                        <form:radiobutton path="language" value="en"/> Englisch
                    </p>
                    <input type="submit" class="btn btn-primary" value="Send" />
                    <sec:csrfInput />
                </form:form>
            </section>

            <section class="tabcontent" id="survey">
                <h2>
                    <spring:message code="survey.title"/>
                </h2>
                <figure>
                    <div class="graphic">
                        <c:forEach items="${surveyStats}" var="question" varStatus="loop">
                            <div class="row">
                                <div class="chart">
                                    <span id="survey_${loop.index}" class="block" style="color: var(--text-color); font-family: 'Fira Sans Condensed', sans-serif;"></span>
                                    <span class="block">
                                        <span class="value">${question[0]}</span>
                                    </span>
                                    <span class="block">
                                        <span class="value">${question[1]}</span>
                                    </span>
                                    <span class="block">
                                        <span class="value">${question[2]}</span>
                                    </span>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <ul class="legend">
                        <li>Ja</li>
                        <li>Nein</li>
                        <li>Keine Antwort</li>
                    </ul>
                </figure>
            </section>

            <section class="tabcontent" id="registrationmap">
                <h2>Where come from?</h2>
                <div id="map"></div>
            </section>

            <section class="tabcontent" id="matches">
                <h2>
                    Matches
                </h2><br>
                <div class="result fakebutton">
                    <a href="/runMatching" class="matchingbutton"><i class="material-icons" style="font-size:40px">flight_takeoff</i></a>
                </div><br><br>
                <table id="matchingtable" class="table table-striped table-bordered table-sm">
                    <thead>
                        <tr>
                            <th>Zeit</th>
                            <th>User 1</th>
                            <th>User 2</th>
                            <th>Modus</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${matches}" var="match" >
                            <tr>
                                <td><fmt:formatDate value="${match.dateCreated()}" pattern = "yyyy-MM-dd"/></td>
                                <td>${match.email1()}</td>
                                <td>${match.email2()}</td>
                                <td>${match.mode()}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </section>

            <section class="tabcontent" id="config">
                <h2>
                    Edit Configuration <input type="submit" onclick="enableDisableForm('configFields');" value="on/off" />
                </h2>
                <form:form method="POST" action="/update/configuration" modelAttribute="configurationForm">
                    <fieldset id="configFields" disabled="disabled">
                    <div class="form-group">
                        <div style="display: inline-block; margin-right: 30px;">
                            <p>Special is active</p>
                            <label class="answer">
                                <form:radiobutton path="specialActive" value="TRUE"/>
                                <spring:message code="question.true"/>
                            </label>
                            <label class="answer">
                                <form:radiobutton path="specialActive" value="FALSE"/>
                                <spring:message code="question.false"/>
                            </label>
                            <div class="feedback-error">
                                <form:errors path="specialActive"/>
                            </div>
                        </div>

                        <div style="display: inline-block; margin-right: 30px;">
                            <p>Matching is active</p>
                            <label class="answer">
                                <form:radiobutton path="matchingActive" value="TRUE"/>
                                <spring:message code="question.true"/>
                            </label>
                            <label class="answer">
                                <form:radiobutton path="matchingActive" value="FALSE"/>
                                <spring:message code="question.false"/>
                            </label>
                            <div class="feedback-error">
                                <form:errors path="matchingActive"/>
                            </div>
                        </div>

                        <div style="display: inline-block; margin-right: 30px;">
                            <p>Factor waiting time</p>
                            <label class="answer">
                                <form:radiobutton path="matchingFactorWaitingTime" value="TRUE"/>
                                <spring:message code="question.true"/>
                            </label>
                            <label class="answer">
                                <form:radiobutton path="matchingFactorWaitingTime" value="FALSE"/>
                                <spring:message code="question.false"/>
                            </label>
                            <div class="feedback-error">
                                <form:errors path="matchingFactorWaitingTime"/>
                            </div>
                        </div><br><br>

                        <div style="display: inline-block;">
                            <label>Nr. of questions (regular)</label>
                            <form:input type="number" path="numberOfRegularQuestions" class="form-control"/>
                            <form:errors path="numberOfRegularQuestions" class="feedback-error"/>
                        </div>
                        <div style="display: inline-block;">
                            <label>Nr. of questions (special)</label>
                            <form:input type="number" path="numberOfVotes" class="form-control" style="border: #f00 solid 2px;"/>
                            <form:errors path="numberOfVotes" class="feedback-error"/>
                        </div><br>

                        <div style="display: inline-block;">
                            <label>Min. disagreements (regular)</label>
                            <form:input type="number" path="matchingMinimumDisagreementsRegular" class="form-control"/>
                            <form:errors path="matchingMinimumDisagreementsRegular" class="feedback-error"/>
                        </div>
                        <div style="display: inline-block;">
                            <label>Min. disagreements (special)</label>
                            <form:input type="number" path="matchingMinimumDisagreementsSpecial" class="form-control" style="border: #f00 solid 2px;"/>
                            <form:errors path="matchingMinimumDisagreementsSpecial" class="feedback-error"/>
                        </div><br>

                        <div style="display: inline-block;">
                            <label>Current Vote</label>
                            <form:input path="currentVote" class="form-control"/>
                            <form:errors path="currentVote" class="feedback-error"/>
                        </div>
                    </div>
                <input type="submit" class="btn btn-primary" value="Update Configuration" />
                <sec:csrfInput />
                </fieldset>
                </form:form>
            </section>

            <section class="tabcontent" id="questions">
                <h2>
                    Edit Questions <input type="submit" onclick="enableDisableForm('questionFields');" value="on/off" />
                </h2>
                <form:form method="POST" action="/update/questions" modelAttribute="questionForm">
                <fieldset id= "questionFields" disabled="disabled">
                    <div class="form-group">
                    <c:if test="${configurationForm.specialActive}">
                        <p><b style="color: #f00;">Special Questions</b></p>
                        <c:forEach begin="1" end="${configurationForm.numberOfVotes}" varStatus="questionloop">
                            <p>${questionloop.index}</p>
                            <c:forEach begin="1" end="3" varStatus="languageloop">
                                <label class="answer">
                                    <c:choose>
                                        <c:when test="${languageloop.index == 1}"><c:set var="ph" value="${phde}"/></c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${languageloop.index == 2}"><c:set var="ph" value="${phfr}"/></c:when>
                                                <c:otherwise><c:set var="ph" value="${phen}"/></c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                    <form:input path="specialQuestions[${languageloop.index-1}][${questionloop.index-1}]" placeholder="${ph}" style="display: inline-block;border: #f00 solid 2px;"/>
                                </label>
                            </c:forEach>
                            <br>
                        </c:forEach>
                        <br><br>
                    </c:if>

                    <p><b>Regular Questions</b></p>
                    <c:forEach begin="1" end="${configurationForm.numberOfRegularQuestions}" varStatus="questionloop">
                        <p>${questionloop.index}</p>
                        <c:forEach begin="1" end="3" varStatus="languageloop">
                            <label class="answer">
                                <c:choose>
                                    <c:when test="${languageloop.index == 1}"><c:set var="ph" value="${phde}"/></c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${languageloop.index == 2}"><c:set var="ph" value="${phfr}"/></c:when>
                                            <c:otherwise><c:set var="ph" value="${phen}"/></c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                                <form:input path="regularQuestions[${languageloop.index-1}][${questionloop.index-1}]" placeholder="${ph}" style="display: inline;"/>
                            </label>
                        </c:forEach>
                        <br>
                    </c:forEach>
                    </div>
                    <input type="submit" class="btn btn-primary" value="Update Questions" />
                    <sec:csrfInput />
                    </fieldset>
                </form:form>
            </section>

            <section class="tabcontent" id="press">
                <h2>Add new media mention</h2>
                <form:form method="POST" action="/press/mention" modelAttribute="mentionForm">
                    <div class="form-group" style="display: inline-block;margin-right: 10px;">
                        <form:label path="date">
                            Date
                        </form:label>
                        <div class="form-group">
                            <form:input type="date" path="date" min="2017-07-09" class="form-control" style="display:inline-block;"/>
                        </div>
                        <div class="feedback-error">
                            <form:errors path="date"/>
                        </div>
                    </div>
                    <div class="form-group" style="display: inline-block;margin-right: 10px;">
                        <form:label path="medium">
                            Medium
                        </form:label>
                        <div class="form-group">
                            <form:input path="medium" class="form-control"/>
                        </div>
                        <div class="feedback-error">
                            <form:errors path="medium"/>
                        </div>
                    </div>
                    <div class="form-group" style="display: inline-block;margin-right: 10px;">
                        <form:label path="type">
                            Typ
                        </form:label>
                        <div class="form-group">
                            <form:select class="dropdown-input" path="type">
                                <form:option value="ARTICLE" label="Article"/>
                                <form:option value="INTERVIEW" label="Interview"/>
                            </form:select>
                        </div>
                        <div class="feedback-error">
                            <form:errors path="type"/>
                        </div>
                    </div>
                    <div class="form-group" style="display: inline-block;">
                        <form:label path="language">
                            Language
                        </form:label>
                        <div class="form-group">
                            <form:select class="dropdown-input" path="language">
                                <form:option value="DE" label="German"/>
                                <form:option value="FR" label="French"/>
                                <form:option value="EN" label="English"/>
                            </form:select>
                        </div>
                        <div class="feedback-error">
                            <form:errors path="language"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <form:label path="link">
                            Link
                        </form:label>
                        <div class="form-group">
                            <form:input path="link" class="form-control"/>
                        </div>
                        <div class="feedback-error">
                            <form:errors path="link"/>
                        </div>
                    </div>
                    <input type="submit" class="btn btn-primary" value="Add media mention" />
                    <sec:csrfInput />
                </form:form>
                <hr style="margin: 20px 0 20px 0;">
                <h2>Add new press release</h2>
                <form:form method="POST" action="/press/release" modelAttribute="pressreleaseForm" enctype="multipart/form-data">
                    <div class="form-group" style="display: inline-block;margin-right: 10px;">
                        <form:label path="date">
                            Date
                        </form:label>
                        <div class="form-group">
                            <form:input type="date" path="date" min="2017-07-09" class="form-control" style="display:block;"/>
                        </div>
                        <div class="feedback-error">
                            <form:errors path="date"/>
                        </div>
                    </div>
                    <div class="form-group" style="display: block;margin-right: 10px;">
                        <form:label path="file_de">
                            Upload release (German)
                        </form:label>
                        <div class="form-group">
                            <form:input type="file" path="file_de" class="form-control" style="border:none;"/>
                        </div>
                        <div class="feedback-error">
                            <form:errors path="file_de"/>
                        </div>
                    </div>
                    <div class="form-group" style="display: block;margin-right: 10px;">
                        <form:label path="file_fr">
                            Upload release (French)
                        </form:label>
                        <div class="form-group">
                            <form:input type="file" path="file_fr" class="form-control" style="border:none;"/>
                        </div>
                        <div class="feedback-error">
                            <form:errors path="file_fr"/>
                        </div>
                    </div>
                    <input type="submit" class="btn btn-primary" value="Add press release" />
                    <p>(Files will be automatically renamed.)</p>
                    <sec:csrfInput />
                </form:form>
            </section>
            <section class="tabcontent" id="news">
                <form:form method="POST" action="/press/publishnews" modelAttribute="newsForm">
                    <div class="form-group" style="display: inline-block;margin-right: 10px;">
                        <form:label path="date">
                            Date
                        </form:label>
                        <div class="form-group">
                            <form:input type="date" path="date" min="2017-07-09" class="form-control" style="display:inline-block;"/>
                        </div>
                        <div class="feedback-error">
                            <form:errors path="date"/>
                        </div>
                    </div>
                    <c:forEach begin="1" end="3" varStatus="loop">
                        <c:choose>
                            <c:when test="${loop.index == 1}"><c:set var="ph" value="${phde}"/></c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${loop.index == 2}"><c:set var="ph" value="${phfr}"/></c:when>
                                    <c:otherwise><c:set var="ph" value="${phen}"/></c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                        <div class="form-group">
                            <form:label path="titles[${loop.index}]">
                                Title
                            </form:label>
                            <div class="form-group">
                                <form:input path="titles[${loop.index-1}]" placeholder="${ph}" class="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <form:label path="texts[${loop.index}]">
                                Text
                            </form:label>
                            <div class="form-group">
                                <form:textarea path="texts[${loop.index-1}]" placeholder="${ph}" class="input-paragraph form-control"/>
                            </div>
                        </div>
                    </c:forEach>
                    <input type="submit" class="btn btn-primary" value="Add news article" />
                    <sec:csrfInput />
                </form:form>
            </section>
        </section>
    </section>
</main>

<jsp:include page="templates/footer.jsp" />
