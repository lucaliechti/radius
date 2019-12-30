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
<link rel="stylesheet" type="text/css" href="css/admin.css"/>
<script type="text/javascript" src="js/admin.js"></script>
<script type="text/javascript" src="js/ms.js"></script>

<script>
    $(document).ready(function() {
        var map = L.map('map').setView([46.75, 8.25], 8);

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
    });

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
</script>

<!-- variables -->
<spring:message code="admin.newsletter.send" var="send" />

<main class="firstcontainer container">
    <section id="leftsection" style="max-width: 1140px;margin: 0 auto;margin-bottom: 20px;">

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

            <section class="leftsection-content-element" id="survey">
                <h2>
                    <spring:message code="survey.title"/>
                </h2>
                <figure>
                    <div class="graphic">
                        <c:forEach items="${surveyStats}" var="question" varStatus="loop">
                            <div class="row">
                                <div class="chart">
                                    <span id="survey_${loop.index}" class="block" style="color: #000000; font-family: 'Fira Sans Condensed', sans-serif;"></span>
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

            <section class="leftsection-content-element" id="registrationmap">
                <h2>Where come from?</h2>
                <div id="map"></div>
            </section>

            <section class="leftsection-content-element" id="votes">
                <h2>
                    Im Moment ist Abstimmung: <spring:message code="question.${special}"/>
                </h2>
                <p>
                    Anzahl Abstimmungen: ${nrvotes}
                </p>
            </section>

            <section class="leftsection-content-element" id="matches">
                <h2>
                    Matches
                </h2>
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
        </section>
    </section>
</main>

<jsp:include page="templates/footer.jsp" />
