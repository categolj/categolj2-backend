define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var CategoriesView = require('../views/CategoriesView');

    return Backbone.Router.extend({
        routes: {
            'categories': 'list'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('categories');
        },
        list: function () {
            this.adminView.renderTab(this.tabPanelView, new CategoriesView().render());
        }
    });
});