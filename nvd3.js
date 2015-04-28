nv.addGraph(function () {
    var chart = nv.models.lineChart().margin({
        top: 30,
        right: 20,
        bottom: 50,
        left: 45
    }).showLegend(true).tooltipContent(function (key, y, e, graph) {
        return '<h3>' + key + '</h3>' + '<p>' + e + '% at ' + y + '</p>'
    });
    
    //chart.forceY([3, 5]);
    
    chart.xAxis.tickFormat(function (d) {
        //return d3.time.format('%x')(new Date(d))
        //return d3.time.format('%H:%M:%S')(new Date(d))
        return d3.time.format('%d/%m/%y')(new Date(d))
    });

    d3.select('#lineChart svg')
        .datum(data)
        .transition().duration(500)
        .call(chart);

    nv.utils.windowResize(chart.update);
    return chart;
});

data = [{
    "values": [{
        "x": 1025409600000 ,
            "y": 3.5
    }, {
        "x": 1028088000000 ,
            "y": 4
    }, {
        "x": 1030766400000 ,
            "y": 3.9
    }, {
        "x": 1033358400000 ,
            "y": 3
    }, {
        "x": 1036040400000  ,
            "y": 3.8
    }, {
        "x": 1038632400000  ,
            "y": 3.7
    }],
        "key": "Sine Wave",
}]


