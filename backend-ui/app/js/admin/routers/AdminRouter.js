define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var AdminView = require('js/admin/views/AdminView');
    var DashboardView = require('js/admin/views/dashboard/DashboardView');

    var EntriesRouter = require('js/admin/routers/EntriesRouter');
    var LinksRouter = require('js/admin/routers/LinksRouter');
    var UploadsRouter = require('js/admin/routers/UploadsRouter');
    var UsersRouter = require('js/admin/routers/UsersRouter');
    var LoggersRouter = require('js/admin/routers/LoggersRouter');
    var ApisRouter = require('js/admin/routers/ApisRouter');
    var ReportsRouter = require('js/admin/routers/ReportsRouter');
    var SystemRouter = require('js/admin/routers/SystemRouter');

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