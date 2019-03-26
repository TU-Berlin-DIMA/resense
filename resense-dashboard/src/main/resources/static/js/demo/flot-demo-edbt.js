$(function() {

    var container = $("#flot-line-chart-moving");

    // Determine how many data points to keep based on the placeholder's initial size;
    // this gives us a nice high-res plot while avoiding more than one point per pixel.

    var maximum = container.outerWidth() / 2 || 300;

    //

    var data = [];

    function getRandomData() {

        if (data.length) {
            data = data.slice(1);
        }

        while (data.length < maximum) {
            var previous = data.length ? data[data.length - 1] : 50;
            var y = previous + Math.random() * 10 - 5;
            data.push(y < 0 ? 0 : y > 100 ? 100 : y);
        }

        // zip the generated y values with the x values

        var res = [];
        for (var i = 0; i < data.length; ++i) {
            res.push([i, data[i]])
        }

        return res;
    }

    series = [{
        data: getRandomData(),
        lines: {
            fill: true
        }
    }];


    var plot = $.plot(container, series, {
        grid: {

            color: "#999999",
            tickColor: "#D4D4D4",
            borderWidth:0,
            minBorderMargin: 20,
            labelMargin: 10,
            backgroundColor: {
                colors: ["#ffffff", "#ffffff"]
            },
            margin: {
                top: 8,
                bottom: 20,
                left: 20
            },
            markings: function(axes) {
                var markings = [];
                var xaxis = axes.xaxis;
                for (var x = Math.floor(xaxis.min); x < xaxis.max; x += xaxis.tickSize * 2) {
                    markings.push({
                        xaxis: {
                            from: x,
                            to: x + xaxis.tickSize
                        },
                        color: "#fff"
                    });
                }
                return markings;
            }
        },
        colors: ["#1ab394"],
        xaxis: {
            tickFormatter: function() {
                return "";
            }
        },
        yaxis: {
            min: 0,
            max: 110
        },
        legend: {
            show: true
        }
    });

    // Update the random dataset at 25FPS for a smoothly-animating chart

    setInterval(function updateRandom() {
        series[0].data = getRandomData();
        plot.setData(series);
        plot.draw();
    }, 40);

});

$(function() {

    var container = $("#flot-line-chart-moving-2");

    // Determine how many data points to keep based on the placeholder's initial size;
    // this gives us a nice high-res plot while avoiding more than one point per pixel.

    var maximum = container.outerWidth() / 2 || 300;

    //

    var data = [];

    function getRandomData() {

        if (data.length) {
            data = data.slice(1);
        }

        while (data.length < maximum) {
            var previous = data.length ? data[data.length - 1] : 50;
            var y = previous + Math.random() * 10 - 5;
            data.push(y < 0 ? 0 : y > 100 ? 100 : y);
        }

        // zip the generated y values with the x values

        var res = [];
        for (var i = 0; i < data.length; ++i) {
            res.push([i, data[i]])
        }

        return res;
    }

    series = [{
        data: getRandomData(),
        lines: {
            fill: true
        }
    }];


    var plot = $.plot(container, series, {
        grid: {

            color: "#999999",
            tickColor: "#D4D4D4",
            borderWidth:0,
            minBorderMargin: 20,
            labelMargin: 10,
            backgroundColor: {
                colors: ["#ffffff", "#ffffff"]
            },
            margin: {
                top: 8,
                bottom: 20,
                left: 20
            },
            markings: function(axes) {
                var markings = [];
                var xaxis = axes.xaxis;
                for (var x = Math.floor(xaxis.min); x < xaxis.max; x += xaxis.tickSize * 2) {
                    markings.push({
                        xaxis: {
                            from: x,
                            to: x + xaxis.tickSize
                        },
                        color: "#fff"
                    });
                }
                return markings;
            }
        },
        colors: ["#1ab394"],
        xaxis: {
            tickFormatter: function() {
                return "";
            }
        },
        yaxis: {
            min: 0,
            max: 110
        },
        legend: {
            show: true
        }
    });

    // Update the random dataset at 25FPS for a smoothly-animating chart

    setInterval(function updateRandom() {
        series[0].data = getRandomData();
        plot.setData(series);
        plot.draw();
    }, 40);

});

$(function () {
    $('#expStopBtn').on('click', function (e) {
        e.preventDefault();

        var experimentName = $("#experiment option:selected").text().replace(" ", "-").toLowerCase();

        if (experimentName !== "new..") {
            $.ajax({
                url: "/experiment/stop",
                type: "get",
                data: {
                    name: experimentName,
                    datapath: "test"
                },
                success: function (response) {
                    console.log('response received');
                },
                error: function (xhr) {
                    console.log('ajax failed');
                },
            });
        }
    });
});

$(function () {
    $('#expStartBtn').on('click', function (e) {
        e.preventDefault();

        var experimentName = $("#experiment option:selected").text().replace(" ", "-").toLowerCase();

        if (experimentName !== "new..") {
            $.ajax({
                url: "/experiment/start",
                type: "get",
                data: {
                    name: experimentName,
                    datapath: "test"
                },
                success: function (response) {
                    console.log('response received');
                },
                error: function (xhr) {
                    console.log('ajax failed');
                },
            });
        }
    });
});

$(function () {
    var sensorStartButtons = document.getElementsByClassName('sensor-start-btn');

    function sensorStartBtnEvent() {
        var sensorName = $(this).closest('td').prev().text();
        var experimentName = $(this).parent().closest(':has(h5)').find('h5').text().replace(" ", "-").toLowerCase();
        $.ajax({
            url: "/sensor/start/replay",
            type: "get",
            data: {
                name: sensorName,
                experiment: experimentName
            },
            success: function (response) {
                console.log('response received');
            },
            error: function (xhr) {
                console.log('ajax failed');
            },
        });

        console.log("clicked on a sensor start btn");
        console.log("the closest sensor name is " + sensorName);
        console.log("the closest exp name is " + experimentName);
    }

    for (var i = 0; i < sensorStartButtons.length; i++) {
        var sensorStartBtn = sensorStartButtons[i];
        sensorStartBtn.onclick = sensorStartBtnEvent;
    }
});

$(function () {
    var sensorStopButtons = document.getElementsByClassName('sensor-stop-btn');

    function sensorStopBtnEvent() {
        var sensorName = $(this).closest('td').prev().text();
        var experimentName = $(this).parent().closest(':has(h5)').find('h5').text().replace(" ", "-").toLowerCase();
        $.ajax({
            url: "/sensor/stop/replay",
            type: "get",
            data: {
                name: sensorName,
                experiment: experimentName
            },
            success: function (response) {
                console.log('response received');
            },
            error: function (xhr) {
                console.log('ajax failed');
            },
        });

        console.log("clicked on a sensor stop btn");
        console.log("the closest sensor name is " + sensorName);
        console.log("the closest exp name is " + experimentName);
    }
    
    for (var i = 0; i < sensorStopButtons.length; i++) {
        var sensorStopBtn = sensorStopButtons[i];
        sensorStopBtn.onclick = sensorStopBtnEvent;
    } 
});

$(function () {
    var sensorRecButtons = document.getElementsByClassName('sensor-rec-btn');

    function sensorRecBtnEvent() {
        var sensorName = $(this).closest('td').prev().text();
        var experimentName = $(this).parent().closest(':has(h5)').find('h5').text().replace(" ", "-").toLowerCase();
        $.ajax({
            url: "/sensor/start/record",
            type: "get",
            data: {
                name: sensorName,
                experiment: experimentName
            },
            success: function (response) {
                console.log('response received');
            },
            error: function (xhr) {
                console.log('ajax failed');
            },
        });

        console.log("clicked on a sensor rec btn");
        console.log("the closest sensor name is " + sensorName);
        console.log("the closest exp name is " + experimentName);
    }

    for (var i = 0; i < sensorRecButtons.length; i++) {
        var sensorRecBtn = sensorRecButtons[i];
        sensorRecBtn.onclick = sensorRecBtnEvent;
    }
});

$(function () {
    var sensorLists = document.getElementsByClassName('sensor-list');
    var flotChartDrawingAreas = document.getElementsByClassName('flot-chart-content');
    document.socketMap = {};
    document.messageMap = {};

    function sensorListDrpEvent() {
        console.log('clicked on a sensor');
        var host = $(this).find(':selected').attr('host');
        var name = $(this).find(':selected').text();
        var mapKey = host+name;
        if (document.socketMap[mapKey] === undefined || !(mapKey in document.socketMap) || document.socketMap[mapKey].readyState !== WebSocket.OPEN) {
            if (document.messageMap[mapKey] === undefined || !(mapKey in document.messageMap)) {
                document.messageMap[mapKey] = [];
            }
            var url = 'ws://' + host + ':8080/sensors/?name=' + name + '&frequency=1000';
            var edgeSocket = new WebSocket(url);
            edgeSocket.onopen = function(event) {
                console.log("Websocket opened");
            };

            edgeSocket.onmessage = function(event) {
                var messageData = event.data;
                document.messageMap[mapKey].push(messageData);
            };

            document.socketMap[mapKey] = edgeSocket;
        }
    }

    for (var i=0;i<sensorLists.length;i++) {
        var sensorList = sensorLists[i];
        sensorList.onchange = sensorListDrpEvent;
        var listOpts = document.getElementsByName(sensorList.getAttribute('name'))[0].options;
        var name = listOpts[0].text;
        var host = listOpts[0].getAttribute('host');
        var mapKey = host+name;
        if (document.socketMap[mapKey] === undefined || !(mapKey in document.socketMap) || document.socketMap[mapKey].readyState !== WebSocket.OPEN) {
            if (document.messageMap[mapKey] === undefined || !(mapKey in document.messageMap)) {
                document.messageMap[mapKey] = [];
            }

            var url = 'ws://' + host + ':8080/sensors/?name=' + name + '&frequency=1000';
            var edgeSocket = new WebSocket(url);
            edgeSocket.onopen = function(event) {
                console.log("Websocket opened");
            };

            edgeSocket.onmessage = function(event) {
                var messageData = event.data;
                document.messageMap[mapKey].push(messageData);
            };

            document.socketMap[mapKey] = edgeSocket;
        }
    }

    for (var i=0;i<flotChartDrawingAreas.length;i++) {
        var currentContainer = $(document.getElementById(flotChartDrawingAreas[i].getAttribute('id')));
        var maximum = currentContainer.outerWidth() / 2 || 300;
        var data = [];

        function getRandomData() {

            if (data.length) {
                data = data.slice(1);
            }

            while (data.length < maximum) {
                var previous = data.length ? data[data.length - 1] : 50;
                var y = previous + Math.random() * 10 - 5;
                data.push(y < 0 ? 0 : y > 100 ? 100 : y);
            }

            // zip the generated y values with the x values

            var res = [];
            for (var i = 0; i < data.length; ++i) {
                res.push([i, data[i]])
            }

            return res;
        }

        var series = [{
            data: getRandomData(data),
            lines: {
                fill: true
            }
        }];


        var plot = $.plot(currentContainer, series, {
            grid: {

                color: "#999999",
                tickColor: "#D4D4D4",
                borderWidth:0,
                minBorderMargin: 20,
                labelMargin: 10,
                backgroundColor: {
                    colors: ["#ffffff", "#ffffff"]
                },
                margin: {
                    top: 8,
                    bottom: 20,
                    left: 20
                },
                markings: function(axes) {
                    var markings = [];
                    var xaxis = axes.xaxis;
                    for (var x = Math.floor(xaxis.min); x < xaxis.max; x += xaxis.tickSize * 2) {
                        markings.push({
                            xaxis: {
                                from: x,
                                to: x + xaxis.tickSize
                            },
                            color: "#fff"
                        });
                    }
                    return markings;
                }
            },
            colors: ["#1ab394"],
            xaxis: {
                tickFormatter: function() {
                    return "";
                }
            },
            yaxis: {
                min: 0,
                max: 110
            },
            legend: {
                show: true
            }
        });

        // Update the random dataset at 25FPS for a smoothly-animating chart

        setInterval(function updateRandom() {
            series[0].data = getRandomData();
            plot.setData(series);
            plot.draw();
        }, 40);
    }
});
