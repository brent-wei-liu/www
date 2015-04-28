;
$(function() {
	$("#DepTab").jtable(
			{
				title : 'Temporal Dependencies',
				paging : true, // Enable paging
				pageSize : 5,
				sorting : true, // Enable sorting
				defaultSorting : 'ChiSquare DESC',
				selecting : true,
				actions : {
					listAction : 'MineDependence'
				},
				fields : {
					Antecedent : {
						title : 'Antecedent',
						width : '20%',
						display : function(data) {
							return "" + data.record.Antecedent + ":"
									+ mappingTypes(data.record.Antecedent);
						}
					},
					Consequent : {
						title : 'Consequent',
						width : '20%',
						display : function(data) {
							return "" + data.record.Consequent + ":"
									+ mappingTypes(data.record.Consequent);
						}
					},
					MinInterval : {
						title : 'MinInterval',
						width : '3%',
						display : function(data) {
							return " " + data.record.MinInterval;
						}
					},
					MaxInterval : {
						title : 'MaxInterval',
						width : '3%',
						display : function(data) {
							return " " + data.record.MaxInterval;
						}
					},
					ChiSquare : {
						title : 'ChiSquare',
						width : '10%',
						display : function(data) {
							return " " + data.record.ChiSquare;
						}
					},
					SuppAntecedent : {
						title : 'SuppAntecedent',
						width : '20%',
						display : function(data) {
							// console.log();
							return " " + data.record.SuppAntecedent +" (account:"+data.record.ATotal+", all:"+ data.record.Total+")";
						}
					},
					
					SuppConsequent : {
						title : 'SuppConsequent',
						width : '20%',
						display : function(data) {
							return " " + data.record.SuppConsequent +" (account:"+data.record.BTotal+", all:"+ data.record.Total+")";
						}
					}
				},
				selectionChanged : function() {
					// Get all selected rows
					var $selectedRows = $('#DepTab').jtable('selectedRows');

					if ($selectedRows.length > 0) {
						// Show selected rows
						$selectedRows.each(function() {

							var record = $(this).data('record');
							console.log(record.ChiSquare);
							markDependencies(record);
							// createActionLog(record.SchedSeq);
						});
					}
				}
			});

//	var param = {
//		'minSupp' : 0.1,
//		'minChiS' : 10.01,
//		'topK' : 10,
//		'filter' : 0
//	};

	//$('#DepTab').jtable('load', param);

});