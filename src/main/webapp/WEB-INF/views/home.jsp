<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="templates/header.jsp" />

<script> $('#nav-home').addClass('active'); </script>

<c:if test="${loggedout != null}">
	<div class="alert alert-success fade in" role="alert">
	<a href="#" class="close" data-dismiss="alert">&times;</a>
  		You have been successfully logged out.
	</div>
</c:if>

<h1><spring:message code="home.title"/></h1>

<a href="<c:url value='/login' />"><spring:message code="login.title"/></a><p>
<a href="<c:url value='/register' />"><spring:message code="register.title"/></a>



<!-- BEGIN TEST LEAFLET -->
<script type="text/javascript" src="js/geotest.js"></script> <!-- load three random geojson areas -->

<div id="mapid"></div>
<script>
var mymap = L.map('mapid').setView([46.75, 8.25], 7);
//alert(testgj.features[0].geometry.type);
var tl = L.tileLayer('https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}.png', {
	maxZoom: 9,
	minZoom: 7,
	attribution: '<a href="https://wikimediafoundation.org/wiki/Maps_Terms_of_Use">Wikimedia</a>',
}).addTo(mymap);

var myStyle = {
	    "color": "#ff4400",
	    "weight": 2,
	    "opacity": 0.65
	};

L.geoJson(testgj,{
	style: myStyle
}).addTo(mymap);

//works
//var locs = ${locs}
//alert(locs[0])
</script>

<c:out value="${locs}" />

<!-- END TEST LEAFLET -->



<jsp:include page="templates/footer.jsp" />