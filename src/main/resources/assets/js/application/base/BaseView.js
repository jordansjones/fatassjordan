define(['handlebars'], function (Handlebars) {
	return function (options) {
		return Handlebars.compile(options.template);
	};
});
