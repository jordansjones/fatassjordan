//
// Created on 6/9/12 at 7:02 PM
//
define(['require'], function (require) {

	// Configure Require
	require.config({
		paths: {
			"_": "vendor/underscore",
			"jquery": "vendor/jquery-1.7.2",
			"backbone": "vendor/backbone"
		},

		shim: {
			"_": {
				"exports": "_"
			},
			"backbone": {
				"deps": ["_", "jquery"],
				"exports": "Backbone"
			}
		}
	});

	return {};
});