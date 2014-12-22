define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var AdminView = require('app/js/admin/views/AdminView');
    var DashboardView = require('app/js/admin/views/dashboard/DashboardView');

    var EntriesRouter = require('app/js/admin/routers/EntriesRouter');
    var LinksRouter = require('app/js/admin/routers/LinksRouter');
    var UploadsRouter = require('app/js/admin/routers/UploadsRouter');
    var UsersRouter = require('app/js/admin/routers/UsersRouter');
    var LoggersRouter = require('app/js/admin/routers/LoggersRouter');
    var ApisRouter = require('app/js/admin/routers/ApisRouter');
    var ReportsRouter = require('app/js/admin/routers/ReportsRouter');
    var SystemRouter = require('app/js/admin/routers/SystemRouter');

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