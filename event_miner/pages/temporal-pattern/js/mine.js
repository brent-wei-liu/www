;
$(function() {
	$('#btnRun').click(onClickRun);
	

});

function getCheckFilter(){
	var v = $('#filter').is(':checked');
	if(v==true)
		return 1;
	else
		return 0;
}

function onClickRun() {
	var minSupport = $('#minSupport').val();
	var minChiSquare = $('#minChiSquare').val();
	var topK = $('#topK').val();
	var filter =getCheckFilter();
	if (minSupport.length == 0 || minChiSquare.length == 0 || topK.length == 0) {
		console.log("invalid");
		alert("input invalid parameters");
		return;
	}
	var param = {
		'minSupp' : minSupport,
		'minChiS' : minChiSquare,
		'topK' : topK,
		'filter':filter
	};

	$('#DepTab').jtable('load', param);
	// $.get('MineDependence', param, function(data) {
	//		
	// });

}

function validate(evt) {
	var theEvent = evt || window.event;
	var key = theEvent.keyCode || theEvent.which;
	key = String.fromCharCode(key);
	var regex = /[0-9]|\./;
	if (!regex.test(key)) {
		theEvent.returnValue = false;
		if (theEvent.preventDefault)
			theEvent.preventDefault();
	}
}

function markDependencies(dep) {
	$.get('MarkDependence', dep, function(data) {
		console.log("data.num" + data);
		updateDep(data);
	}, 'json');
}

function mappingTypes(uIndex) {
	var type = '';
	// param ={'uIndex':uIndex};
	// type = $.get('MappingType',param);
	$.ajax({
		url : 'MappingType?uIndex=' + uIndex,
		type : 'get',
		dataType : 'html',
		async : false,
		success : function(data) {
			type = data;
		}
	});
	return type;
}

function convertTime(seconds) {
	Date.prototype.customFormat = function(formatString) {
		var YYYY, YY, MMMM, MMM, MM, M, DDDD, DDD, DD, D, hhh, hh, h, mm, m, ss, s, ampm, AMPM, dMod, th;
		var dateObject = this;
		YY = ((YYYY = dateObject.getFullYear()) + "").slice(-2);
		MM = (M = dateObject.getMonth() + 1) < 10 ? ('0' + M) : M;
		MMM = (MMMM = [ "January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November",
				"December" ][M - 1]).substring(0, 3);
		DD = (D = dateObject.getDate()) < 10 ? ('0' + D) : D;
		DDD = (DDDD = [ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
				"Friday", "Saturday" ][dateObject.getDay()]).substring(0, 3);
		th = (D >= 10 && D <= 20) ? 'th' : ((dMod = D % 10) == 1) ? 'st'
				: (dMod == 2) ? 'nd' : (dMod == 3) ? 'rd' : 'th';
		formatString = formatString.replace("#YYYY#", YYYY).replace("#YY#", YY)
				.replace("#MMMM#", MMMM).replace("#MMM#", MMM).replace("#MM#",
						MM).replace("#M#", M).replace("#DDDD#", DDDD).replace(
						"#DDD#", DDD).replace("#DD#", DD).replace("#D#", D)
				.replace("#th#", th);

		h = (hhh = dateObject.getHours());
		if (h == 0)
			h = 24;
		if (h > 12)
			h -= 12;
		hh = h < 10 ? ('0' + h) : h;
		AMPM = (ampm = hhh < 12 ? 'am' : 'pm').toUpperCase();
		mm = (m = dateObject.getMinutes()) < 10 ? ('0' + m) : m;
		ss = (s = dateObject.getSeconds()) < 10 ? ('0' + s) : s;
		return formatString.replace("#hhh#", hhh).replace("#hh#", hh).replace(
				"#h#", h).replace("#mm#", mm).replace("#m#", m).replace("#ss#",
				ss).replace("#s#", s).replace("#ampm#", ampm).replace("#AMPM#",
				AMPM);
	};
	
	var milliseconds = seconds * 1000;
	var tm = new Date(milliseconds);
	return tm.customFormat("#DD#/#MM#/#YYYY# #hh#:#mm#:#ss#");
}