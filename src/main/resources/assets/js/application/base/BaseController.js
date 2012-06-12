define(['_', 'backbone'], function (_, Backbone) {

	return Backbone.View.extend({

		elements: {},
		bindings: {},

		/**
		 * We call `refreshElements()` here to be sure
		 * our shortcuts hash is up to date.
		 */
		initialize: function () {
			this.init();
			this.refreshElements();
			this.refreshBindings();
		},

		init: function () {},

		/**
		 * Set the elements html.
		 *
		 * Keep the shortcuts hash up to date and provide
		 * for a more controller like interface.
		 */
		html: function (html) {
			if (arguments.length > 1) {
				for (var i = 1, size = arguments.length; i < size; i++)
					html += arguments[i];
			}
			this.$el.html(html);
			this.refreshElements();
			this.refreshBindings();
			return this;
		},

		/**
		 * Append another controller or element.
		 */
		append: function (el) {
			this.$el.append(this.getElement(el));
			return this;
		},

		/**
		 * Prepend another controller or element.
		 */
		prepend: function (el) {
			this.$el.prepend(this.getElement(el));
			return this;
		},

		/**
		 * A utility method that returns an element if passed
		 * or an element of a controller if a controller was passed.
		 */
		getElement: function (el) {
			if (el.el) {
				return el.el;
			}

			return el;
		},

		/**
		 * Keep the elements shortcut variables up to date.
		 */
		refreshElements: function () {
			var elements = this.elements;

			if (elements) {
				_.each(elements, function (shortcut, selector) {
					this[shortcut] = this.$(selector);
				}, this);
			}

			return this;
		},

		/**
		 * Allows you to correlate a models properties to a
		 * css selector in which the html of that element
		 * will be synced with the models attribute on change.
		 *
		 * The declarative binding syntax looks like so:
		 *    'selector' : 'model:key'
		 *
		 * or you can omit the model portion and it will assume
		 * model.
		 *
		 * 'selector' : 'key' // Assumes "this.model"
		 */
		refreshBindings: function () {
			var bindings = this.bindings;
			var separator = ':';
			var model, key;

			if (bindings) {
				_.each(bindings, function (binding, selector) {
					if (binding.indexOf(separator) !== -1) {
						binding = binding.split(separator);
						model = binding[0];
						key = binding[1];
					}
					else {
						model = 'model';
						key = binding;
					}

					this.bindElement(selector, model, key);
				}, this);
			}
		},


		/**
		 * Binds a child element's html value to sync with
		 * the value of a model property.
		 */
		bindElement: function (selector, model, key) {
			this[model].on('change:' + key, function (model, value) {
				this.$(selector).html(value);
			}, this);
		}
	});

});


