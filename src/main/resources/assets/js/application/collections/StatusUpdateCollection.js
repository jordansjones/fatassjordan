//
// Created on 6/11/12 at 9:42 PM.
//
define(['backbone', 'application/models/StatusUpdateModel'], function (Backbone, StatusUpdateModel) {
	return Backbone.Collection.extend({
		model: StatusUpdateModel,
		url: '/tweets',
		comparator: function (model) {
			return model.get("timestamp") * -1;
		}
	});
});