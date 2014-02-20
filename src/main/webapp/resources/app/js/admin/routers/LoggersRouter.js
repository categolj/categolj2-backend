define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var LoggersView = require('app/js/admin/views/loggers/LoggersView');
    var Loggers = require('app/js/admin/collections/Loggers');

    return Backbone.Router.extend({
        routes: {
            'loggers': 'list'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('loggers');
        },
        list: function () {
            var loggers = new Loggers();
            this.adminView.renderTab(this.tabPanelView, new LoggersView({
                collection: loggers
            }).render());
            loggers.fetch();
        }
    });
});