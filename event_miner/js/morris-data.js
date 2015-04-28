$(function() {

    var pieData = [];

    Morris.Donut({
        element: 'morris-donut-chart',
        data: [{
            label: "Download Sales",
            value: 12
        }, {
            label: "In-Store Sales",
            value: 30
        }, {
            label: "Mail-Order Sales",
            value: 20
        }],
        resize: true
    });

//Flot Pie Chart
    var data = [{
        label: "Series 0",
        data: 1
    }, {
        label: "Series 1",
        data: 3
    }, {
        label: "Series 2",
        data: 9
    }, {
        label: "Series 3",
        data: 20
    }];

    var plotObj = $.plot(("#flot-pie-chart"), [], {
        series: {
            pie: {
                show: true
            }
        },
        grid: {
            hoverable: true
        },
        tooltip: true,
        tooltipOpts: {
            content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
            shifts: {
                x: 20,
                y: 0
            },
            defaultTheme: false
        }
    });

    Morris.Bar({
        element: 'morris-bar-chart',
        data: [{
            y: '2006',
            a: 100,
            b: 90
        }, {
            y: '2007',
            a: 75,
            b: 65
        }, {
            y: '2008',
            a: 50,
            b: 40
        }, {
            y: '2009',
            a: 75,
            b: 65
        }, {
            y: '2010',
            a: 50,
            b: 40
        }, {
            y: '2011',
            a: 75,
            b: 65
        }, {
            y: '2012',
            a: 100,
            b: 90
        }],
        xkey: 'y',
        ykeys: ['a', 'b'],
        labels: ['Series A', 'Series B'],
        hideHover: 'auto',
        resize: true
    });

    url = "http://131.94.128.175:8000/main/log_type/";
    $.getJSON( url ,function(result){
        $logType = $("#log_types");

        for ( var i = 0; i < 8; i++ ) {
            var str = "<a href='#' class='list-group-item'> \
                       <i class='fa fa-warning fa-fw'></i> " +result[i]["typeName"] +" \
                       <span class='pull-right text-muted small'><em>" +result[i]["count"] +"</em> \
                       </span> \
                       </a>";
            $logType.append(str);
            pieData.push({
                label : result[i]["typeName"],
                data : result[i]["count"]
            }); 
        }
        plotObj.setData( pieData);
        plotObj.draw();

    });


});
