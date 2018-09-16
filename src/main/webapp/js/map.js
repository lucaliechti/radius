var map = L.map('map').setView([46.75, 8.25], 7);

var tl = L.tileLayer('https://cartodb-basemaps-{s}.global.ssl.fastly.net/light_all/{z}/{x}/{y}{r}.png', {
	maxZoom: 10,
    minZoom: 6,
	maxBoundsViscosity: 1,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> &copy; <a href="http://cartodb.com/attributions">CartoDB</a>'
}).addTo(map);

map.setMaxBounds([[45.22, 4.95], [48.24, 11.55]]); //map has to be 800x1200 for this to look good!

var styleUnselected = {
    "color": "#006F68",
    "fillOpacity": 0.2,
    "weight": 2,
    "opacity": 1
};

var styleSelected = {
    "color": "#FFFFFF",
    "fillOpacity": 0.6,
    "weight": 2,
    "opacity": 1
};
$('#locations').val(""); //just to be sure
var selectedRegions = new Set();

function makeselectable(feature, layer) {
    layer.on({
        click: function(e) {
            var region = e.target;
            selectDeselect(region);
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
    layer._leaflet_id = "radius_" + feature.id; //makes map selectable from outside L
}

function selectDeselect(region) {
    if (region.feature.properties.selected) {
		selectedRegions.delete(region.feature.id);
        region.setStyle(styleUnselected);
        $("#region_" + region.feature.id).remove();
    } else {
        region.setStyle(styleSelected);
		selectedRegions.add(region.feature.id)
        var reg = document.createElement('a');
		$('#register-map-locations').append(reg);
        reg.id = 'region_' + region.feature.id;
        $("#" + reg.id).click(function() {
            region = gl.getLayer("radius_" + region.feature.id);
            selectDeselect(region);
        });
        reg.innerHTML = region.feature.properties.name + "\t";
    }
	$('#locations').val([...selectedRegions].join(';'));
	region.feature.properties.selected = !region.feature.properties.selected
}

gl = L.geoJson(ms, {
	style: styleUnselected,
	onEachFeature: makeselectable
}).addTo(map);