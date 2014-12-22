define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var SystemView = require('js/admin/views/system/SystemView');

    return Backbone.Router.extend({
        routes: {
            'system': 'info',
            'system/threadDump': 'threadDump'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('system');
        },
        info: function() {
            this.adminView.renderTab(this.tabPanelView, new SystemView().render());
        },
        threadDump: function() {
            this.adminView.renderTab(this.tabPanelView, new SystemView().threadDump());
        }
    });
});