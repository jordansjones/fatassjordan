//
// Created on 6/9/12 at 7:02 PM
//
// Configure Require
requirejs.config({
	paths: {
		'_': 'vendor/underscore',
		'jquery': 'vendor/jquery-1.7.2',
		'backbone': 'vendor/backbone',
		'handlebars': 'vendor/handlebars',
		'moment': 'vendor/moment',
		'text': 'vendor/text'
	},

	shim: {
		'_': {
			'exports': '_'
		},
		'backbone': {
			'deps': ['_', 'jquery'],
			'exports': 'Backbone'
		},
		'handlebars': {
			'exports': 'Handlebars'
		}
	}
});

requirejs(['application/Analytics', 'jquery', 'application/Main'], function (Analytics, $, Main) {
	Analytics();
	$(function () {
		var app = new Main({
			el: '.page-container'
		});
		app.render();
	});
});