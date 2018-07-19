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
<script type="text/javascript" src="js/ms.js"></script>


<div id="map"></div>
<div id="selectedIDs"></div>
<script>
    var map = L.map('map').setView([46.75, 8.25], 7);

    var tl = L.tileLayer('https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}.png', {
        maxZoom: 9,
        minZoom: 7,
        attribution: '<a href="https://wikimediafoundation.org/wiki/Maps_Terms_of_Use">Wikimedia</a>',
    }).addTo(map);

    var styleUnselected = {
        "color": "#ff4400",
        "weight": 2,
        "opacity": 0.65
    };

    var styleSelected = {
        "color": "#00FFFF",
        "weight": 2,
        "opacity": 0.65
    };

    function makeselectable(feature, layer) {
        layer.on({
            click: function(e) {
                var region = e.target;
                selectDeselect(region);
                $('#selectedIDs').text(getSelectedIDs());
            },
            mouseover: function(e) {
                var region = e.target;
                region.setStyle({
                    weight: 4
                });
            },
            mouseout: function(e) {
                var region = e.target;
                region.setStyle({
                    weight: 2
                });
            }
        });
        layer._leaflet_id = feature.id; //makes map selectable from outside L
    }

    function selectDeselect(region) {
        if (region.feature.properties.selected == true) {
            region.setStyle(styleUnselected);
            region.feature.properties.selected = false;
            var correspondingDiv = document.getElementById("region_" + region.feature.id);
            correspondingDiv.parentNode.removeChild(correspondingDiv);
        } else {
            region.setStyle(styleSelected);
            region.feature.properties.selected = true;
            var reg = document.createElement('a');
            document.body.appendChild(reg);
            reg.id = 'region_' + region.feature.id;
            $("#"+reg.id).click(function(){ 
            	region = gl.getLayer(region.feature.id);
            	selectDeselect(region);
            });
            reg.innerHTML = region.feature.properties.name;
        }
    }
    
    function getSelectedIDs() {
    	var result = "";
    	for(var i = 1; i <= 106; i++){ //hardcoded number of regions
    		region = gl.getLayer(i);
    		if(region.feature.properties.selected == true) {
    			if(result == ""){
    				result = region.feature.id;
    			}
    			else {
    				result += ";" + region.feature.id;
    			}
    		}
    	}
    	return result;
    }

    gl = L.geoJson(ms, {
        style: styleUnselected,
        onEachFeature: makeselectable
    }).addTo(map);
    
    //works
    //var loc = ${locs}
    //alert(locs[0])
</script>

<!-- END TEST LEAFLET -->



<jsp:include page="templates/footer.jsp" />