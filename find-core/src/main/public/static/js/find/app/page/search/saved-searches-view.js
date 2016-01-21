define([
    'backbone',
    'underscore',
    'jquery',
    'find/app/util/tab-view',
    'i18n!find/nls/bundle',
    'text!find/templates/app/page/search/saved-searches/saved-searches-view.html'
], function(Backbone, _, $, TabView, i18n, template) {
    "use strict";

    return Backbone.View.extend({

        template: _.template(template),

        initialize: function (options) {
            this.collection = options.savedSearchesCollection;

            this.tabView = new TabView({
                collection: this.collection
            });

            this.listenTo(this.collection, 'reset update', this.updateVisibility);
        },

        render: function() {
            this.$el.html(this.template({
                i18n: i18n
            }));

            this.tabView.setElement(this.$('.saved-search-tab-view')).render();
            this.updateVisibility();
        },

        updateVisibility: function() {
            this.$('.saved-searches').toggleClass('hide', this.collection.isEmpty());
            this.$('.no-saved-searches').toggleClass('hide', !this.collection.isEmpty());
        }

    });
});