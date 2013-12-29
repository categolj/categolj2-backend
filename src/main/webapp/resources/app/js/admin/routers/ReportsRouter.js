define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var ReportsView = require('../views/ReportsView');

    return Backbone.Router.extend({
        routes: {
            'reports': 'list'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('reports');
        },
        list: function () {
            this.adminView.renderTab(this.tabPanelView, new ReportsView());
        }
    });
});