define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var AdminView = require('js/views/AdminView');
    var DashboardView = require('js/views/dashboard/DashboardView');

    var EntriesRouter = require('js/routers/EntriesRouter');
    var LinksRouter = require('js/routers/LinksRouter');
    var UploadsRouter = require('js/routers/UploadsRouter');
    var UsersRouter = require('js/routers/UsersRouter');
    var LoggersRouter = require('js/routers/LoggersRouter');
    var ApisRouter = require('js/routers/ApisRouter');
    var ReportsRouter = require('js/routers/ReportsRouter');
    var SystemRouter = require('js/routers/SystemRouter');

    return Backbone.Router.extend({
        routes: {
            '': 'home',
            'dashboard': 'home'
        },
        initialize: function () {
            this.adminView = new AdminView({
                el: $('.container')
            });
            this.routers = this.initializeRouters();
            this.tabPanelView = this.adminView.createTabPanelView('dashboard');
        },
        initializeRouters: function () {
            return {
                'entries': new EntriesRouter({adminView: this.adminView}),
                'links': new LinksRouter({adminView: this.adminView}),
                'uploads': new UploadsRouter({adminView: this.adminView}),
                'users': new UsersRouter({adminView: this.adminView}),
                'loggers': new LoggersRouter({adminView: this.adminView}),
                'apis': new ApisRouter({adminView: this.adminView}),
                'reports': new ReportsRouter({adminView: this.adminView}),
                'system': new SystemRouter({adminView: this.adminView})
            };
        },
        home: function () {
            this.adminView.renderTab(this.tabPanelView, new DashboardView().render());
        }
    });
});