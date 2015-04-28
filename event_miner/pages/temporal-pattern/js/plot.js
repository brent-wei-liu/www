;
$(function() {

	/**
	 * Grid theme for Highcharts JS
	 * 
	 * @author Torstein Hønsi
	 */

	Highcharts.theme = {
		colors : [ '#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5',
				'#64E572', '#FF9655', '#FFF263', '#6AF9C4' ],
		chart : {
			backgroundColor : {
				linearGradient : {
					x1 : 0,
					y1 : 0,
					x2 : 1,
					y2 : 1
				},
				stops : [ [ 0, 'rgb(255, 255, 255)' ],
						[ 1, 'rgb(240, 240, 255)' ] ]
			},
			borderWidth : 2,
			plotBackgroundColor : 'rgba(255, 255, 255, .9)',
			plotShadow : true,
			plotBorderWidth : 1
		},
		title : {
			style : {
				color : '#000',
				font : 'bold 16px "Trebuchet MS", Verdana, sans-serif'
			}
		},
		subtitle : {
			style : {
				color : '#666666',
				font : 'bold 12px "Trebuchet MS", Verdana, sans-serif'
			}
		},
		xAxis : {
			gridLineWidth : 1,
			lineColor : '#000',
			tickColor : '#000',
			labels : {
				style : {
					color : '#000',
					font : '11px Trebuchet MS, Verdana, sans-serif'
				}
			},
			title : {
				style : {
					color : '#333',
					fontWeight : 'bold',
					fontSize : '12px',
					fontFamily : 'Trebuchet MS, Verdana, sans-serif'

				}
			}
		},
		yAxis : {
			minorTickInterval : 'auto',
			lineColor : '#000',
			lineWidth : 1,
			tickWidth : 1,
			tickColor : '#000',
			labels : {
				style : {
					color : '#000',
					font : '11px Trebuchet MS, Verdana, sans-serif'
				}
			},
			title : {
				style : {
					color : '#333',
					fontWeight : 'bold',
					fontSize : '12px',
					fontFamily : 'Trebuchet MS, Verdana, sans-serif'
				}
			}
		},
		legend : {
			itemStyle : {
				font : '9pt Trebuchet MS, Verdana, sans-serif',
				color : 'black'

			},
			itemHoverStyle : {
				color : '#039'
			},
			itemHiddenStyle : {
				color : 'gray'
			}
		},
		labels : {
			style : {
				color : '#99b'
			}
		},

		navigation : {
			buttonOptions : {
				theme : {
					stroke : '#CCCCCC'
				}
			}
		}
	};

	// Apply the theme
	var highchartsOptions = Highcharts.setOptions(Highcharts.theme);

	// window.chart = new Highcharts.Chart(
	var chartOptions = {
		chart : {
			renderTo : 'plotContainer',
			type : 'scatter',
			zoomType : 'xy',
			events : {
				load : function() {
					loadData();
				}
			}
		},
		title : {
			text : 'Discovering Lag Interval for Temporal Dependencies'
		},
		subtitle : {
			text : 'KDD2012'
		},
		xAxis : {
			title : {
				enabled : true,
				text : 'Timestamp(second)'
			},
			startOnTick : true,
			endOnTick : true,
			showLastLabel : true
		},
		yAxis : {
			title : {
				text : 'Event Type(0,1...)'
			}
		},
		legend : {
			enabled : false,
			layout : 'vertical',
			align : 'left',
			verticalAlign : 'top',
			x : 100,
			y : 70,
			floating : true,
			backgroundColor : '#FFFFFF',
			borderWidth : 5
		},
		plotOptions : {
			scatter : {
				marker : {
					radius : 2,
					states : {
						hover : {
							enabled : true,
							lineColor : 'rgb(100,100,100)'
						}
					}
				},
				point : {
					events : {
						click : function() {
							hs.htmlExpand(null, {
								pageOrigin : {
									x : this.pageX,
									y : this.pageY
								},
								headingText : this.series.name,
								maincontentText : convertTime(this.x) + '<br> '
										+ mappingTypes(this.y) + '(' + this.y
										+ ')',
								width : 200
							});
						}
					}
				},
				states : {
					hover : {
						marker : {
							enabled : false
						}
					}
				},
				tooltip : {
					shared : true,
					crosshairs : true
				// headerFormat : '<b>{series.name}</b><br>',
				// pointFormat : '({point.x} , {point.y})'
				}
			}
		},
		series : [ {} ]
	};
	// );
	loadData(chartOptions);

});

function loadData(chartOptions) {
	console.log(chartOptions);
	if (chartOptions == undefined)
		return;
	$.get('LoadData', function(data) {
		if (data == null) {
			return;
		}
		console.log(data.Result);
		if (data.Result != "OK") {
			return;
		}
		var typeNum = data.TypeNum;
		var SeriesData = [];
		for ( var j = 0; j < 1; j++) {
			SeriesData.push({
				name : "events",
				data : []
			});
		}
		var records = data.Records;
		for ( var i = 0; i < records.length; i++) {
			var tp = records[i].tp;
			var tm = records[i].tm;
			SeriesData[0].data.push([ tm, tp ]);
			chartOptions.series = SeriesData;
		}

		window.chart = new Highcharts.Chart(chartOptions);
	}, 'json');
}

function updateDep(dep) {
	console.log(window.chart.series.length);
	if (dep["Result"] != 'OK')
		return;
	while (window.chart.series.length > 1) {
		var index = window.chart.series.length - 1;
		window.chart.series[index].remove(false);
	}
	// console.log("removed:"+window.chart.series.length);
	var typeA = dep.A;
	var typeB = dep.B;
	var records = dep.Records;
	for ( var i = 0; i < records.length; i++) {
		window.chart.addSeries({
			type : 'line',
			color : 'red',
			data : [ [ records[i].A, typeA ], [ records[i].B, typeB ] ],
			tooltip : {
				shared : true,
				crosshairs : true
			// headerFormat : '<b>{series.name}</b><br>',
			// pointFormat : '({point.x} , {point.y})'
			},
			point : {
				events : {
					click : function() {
						hs.htmlExpand(null,
								{
									pageOrigin : {
										x : this.pageX,
										y : this.pageY
									},
									headingText : this.series.name,
									maincontentText : convertTime(this.x)
											+ '<br> ' + mappingTypes(this.y)
											+ '(' + this.y + ')',
									width : 200
								});
					}
				}
			}
		}, false);
	}
	window.chart.redraw();

	console.log(window.chart.series.length);
}