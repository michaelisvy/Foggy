function chartOptions(seriesType, chartName, chartText) {
	var options = {
			chart: {
				renderTo: chartName,
				defaultSeriesType: seriesType
			},
			title: {
				text: chartText
			},
			xAxis: {
				categories: []
			},
			yAxis: {
				min: 0,
				max: 500,
				title: {
					text: 'Index'
				}
			},
			series: []
		};
		return options;			
}

function parseData(options, data) {
	// parse data
	var lines = data.split(' ');
	$.each(lines, function(lineNo, line) {
		var items = line.split(',');
			
		// header line containes categories
		if (lineNo == 0) {
			$.each(items, function(itemNo, item) {
				if (itemNo > 0) options.xAxis.categories.push(item);
			});
		}
			
		// the rest of the lines contain data with their name in the first position
		else {
			var series = { 
				data: []
			};
			$.each(items, function(itemNo, item) {
				if (itemNo == 0) {
					series.name = item;
				} else {
					series.data.push(parseFloat(item));
				}
			});					
			options.series.push(series);
		}					
	}); //end each
}

function chartTheme(type, contextPath) {
	var theme = {
			   colors: ["#514F78", "#42A07B", "#9B5E4A", "#72727F", "#1F949A", "#82914E", "#86777F", "#42A07B"],
			   chart: {
			      className: type,
			      borderWidth: 0,
			      plotShadow: true,
			      plotBackgroundImage: contextPath+'/images/'+type+'.jpg',
			      plotBackgroundColor: {
			         linearGradient: [0, 0, 250, 500],
			         stops: [
			            [0, 'rgba(255, 255, 255, 1)'],
			            [1, 'rgba(255, 255, 255, 0)']
			         ]
			      },
			      plotBorderWidth: 1
			   },
			   title: {
			      style: { 
			         color: '#3E576F',
			         font: '16px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
			      }
			   },
			   subtitle: {
			      style: { 
			         color: '#6D869F',
			         
			      }
			   },
			   xAxis: {
			      gridLineWidth: 0,
			      lineColor: '#C0D0E0',
			      tickColor: '#C0D0E0',
			      labels: {
			         style: {
			            color: '#666',
			            fontWeight: 'bold',
			            font: '10px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
			         }
			      },
			      title: {
			         style: {
			            color: '#666',
			            font: '12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
			         }            
			      }
			   },
			   yAxis: {
			      alternateGridColor: 'rgba(255, 255, 255, .5)',
			      lineColor: '#C0D0E0',
			      tickColor: '#C0D0E0',
			      tickWidth: 1,
			      labels: {
			         style: {
			            color: '#666',
			            fontWeight: 'bold'
			         }
			      },
			      title: {
			         style: {
			            color: '#666',
			            font: '12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
			         }            
			      }
			   },
			   legend: {
			      itemStyle: {
			         font: '9pt Trebuchet MS, Verdana, sans-serif',
			         color: '#3E576F'
			      },
			      itemHoverStyle: {
			         color: 'black'
			      },
			      itemHiddenStyle: {
			         color: 'silver'
			      }
			   },
			   labels: {
			      style: {
			         color: '#3E576F'
			      }
			   }
	};
	return theme;
}