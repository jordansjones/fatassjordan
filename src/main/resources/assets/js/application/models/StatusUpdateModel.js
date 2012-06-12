//
// Created on 6/11/12 at 9:40 PM.
//
define(['backbone'], function (Backbone) {
	return Backbone.Model.extend({
		defaults: {
			id: "",
			date: "",
			time: "",
			text: "",
			timestamp: ""
		}
	});
});