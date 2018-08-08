var map = L.map('map').setView([46.75, 8.25], 8);

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
        var correspondingDiv = document.getElementById("region_" + region.feature.id);
        correspondingDiv.parentNode.removeChild(correspondingDiv);
    } else {
        region.setStyle(styleSelected);
		selectedRegions.add(region.feature.id)
        var reg = document.createElement('a');
        document.body.appendChild(reg);
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