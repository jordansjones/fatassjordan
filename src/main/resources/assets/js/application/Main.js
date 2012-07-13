//
// Created on 6/3/12 at 3:31 PM.
//
define(
[
	'jquery',
	'_',
	'moment',
	'application/base/BaseController',
	'application/collections/StatusUpdateCollection',
	'./views/StatusHeaderView',
	'application/views/StatusUpdateView'
],
function ($, _, moment, BaseController, StatusUpdateCollection, StatusHeaderView, StatusUpdateView) {

	return BaseController.extend({

		init: function () {
			this.collection = new StatusUpdateCollection();
			this.collection.on('reset', this.renderCollection, this);

			this.today = moment().local();
			this.headTextToday = 'TODAY MY FATASS WEIGHS';
			this.headTextPrevious = 'PREVIOUSLY MY FATASS WEIGHED';

			this.html('');

			var opts = null;
//			opts = {error: _.bind(this.loadMockTweets, this)};
			this.collection.fetch(opts);
		},

		renderCollection: function () {
			if (this.collection.length < 1) return;

			var idx = 0;

			var model = this.collection.at(idx);
			var modelTs = moment(model.get('timestamp')).local();


			if (this.today.format('L') === modelTs.format('L')) {
				idx += 1;
				this.append(StatusHeaderView({
					'class': 'today',
					'text': this.headTextToday
				}));
				this.renderModel(model, 'today');
			}

			if (idx === this.collection.length) {
				return;
			}

			this.append(StatusHeaderView({
				'class': 'previous',
				'text': this.headTextPrevious
			}));

			for (; idx < this.collection.length; idx++) {
				this.renderModel(this.collection.at(idx));
			}
		},

		renderModel: function (model, className) {
			if (!model || model === null) return;
			var dt = moment(model.get('timestamp')).local();
			var data = {
				'id': model.get('id'),
				'date': model.get('date'),
				'time': model.get('time'),
				'moment': dt.format('MMM DD, YYYY'),
				'className': className || ''
			};
			data['value'] = model.get('formattedWeight');

			this.append(StatusUpdateView(data));
		},

		onNewStatusUpdate: function(model) {
			this.$el.prepend(StatusUpdateView(model.toJSON()));
		},

		loadMockTweets: function () {

			var mocks = [];
			for (var i = 0; i < 11; i++) {
				var dt = moment().subtract('days', i);
				mocks.push({
					id: (i + Math.random()),
					date: dt.format('LL'),
					time: dt.format('LT'),
					text: (220 + i) + '.' + (i % 10),
					timestamp: dt.valueOf()
				})
			}

			this.collection.reset(mocks);
		}

	});
});
