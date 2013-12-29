define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var SystemView = require('../views/SystemView');

    return Backbone.Router.extend({
        routes: {
            'system': 'info'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('system');
        },
        info: function() {
            this.adminView.renderTab(this.tabPanelView, new SystemView());
        }
    });
});