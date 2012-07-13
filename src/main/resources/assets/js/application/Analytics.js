define(function () {
	window._gaq = window._gaq || [];
	window.push(['_setAccount', 'UA-33360072-1']);
	window.push(['_setDomainName', 'fatassjordan.com']);
	window.push(['_trackPageview']);

	return function () {
		var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	};

});