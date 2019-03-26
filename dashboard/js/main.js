var config;
var edge;

$('#experiment-selector').on('change', function () {
    $.getJSON("/experiments/" + this.value + "/experiment.config", function (json) {
        config = json;
        var options = "<option>Choose Raspberry Pi</option>";
        $.each(json.edges, function (index, value) {
            options += "<option value='" + index + "'>" + value.name + "</option>";
        });
        $('#edge-selector').html(options);
    });
});

$('#edge-selector').on('change', function () {
    edge = config.edges[this.value];
    var options = "<option>Choose sensor</option>";
    $.each(edge.sensors, function (index, value) {
        options += "<option value='" + index + "'>" + value.name + "</option>";
    });
    $('#sensor-selector').html(options);
});

$('#select-button').on('click', function () {
    var sensor = edge.sensors[$('#sensor-selector').val()];

    $('#sensor-container').append(
            "<div class='col-md-12'>" +
            "<div class='panel panel-default'>" +
            "<div class='panel-heading'>" +
            " <h3 class='panel-title'>Sensor: "+sensor.name+"</h3>" +
            "</div>" +
            "<div class='panel-body' id='"+sensor.name+"-content'>" +
            "" +
            " </div>" +
            "</div>" +
            "</div>");
    
    socket = new WebSocket("ws://" + edge.host + ":8080/sensors/?name=" + sensor.name+"&frequency=50");
    socket.onopen = function(event){
        console.log("Websocket opened");
    };
    socket.onmessage = function (event) {
        console.log(sensor);
        $('#'+sensor.name+"-content").html(event.data)
    };
});






