//
// Created on 6/3/12 at 3:31 PM.
//
define(
[
	'jquery',
	'_',
	'application/base/BaseController',
	'application/collections/StatusUpdateCollection',
	'application/views/StatusUpdateView'
],
function ($, _, BaseController, StatusUpdateCollection, StatusUpdateView) {

	return BaseController.extend({

		init: function () {
			this.collection = new StatusUpdateCollection();
			this.collection.on("add", this.onNewStatusUpdate, this);

			$.ajax({
				url: "/tweets"
			}).success(_.bind(function (data) {
				this.collection.add(data);
			}, this));
		},

		onNewStatusUpdate: function(model) {
			this.$el.prepend(StatusUpdateView(model.toJSON()));
		}

	});
});
