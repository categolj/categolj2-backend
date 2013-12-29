define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var AdminView = require('../views/AdminView');
    var DashboardView = require('../views/DashboardView');
    var EntriesRouter = require('../routers/EntriesRouter');
    var CategoriesRouter = require('../routers/CategoriesRouter');
    var UploadsRouter = require('../routers/UploadsRouter');
    var UsersRouter = require('../routers/UsersRouter');
    var ReportsRouter = require('../routers/ReportsRouter');
    var SystemRouter = require('../routers/SystemRouter');
    var H2ConsoleRouter = require('../routers/H2ConsoleRouter');

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
                'categories': new CategoriesRouter({adminView: this.adminView}),
                'uploads': new UploadsRouter({adminView: this.adminView}),
                'users': new UsersRouter({adminView: this.adminView}),
                'reports': new ReportsRouter({adminView: this.adminView}),
                'system': new SystemRouter({adminView: this.adminView}),
                'h2Console': new H2ConsoleRouter({adminView: this.adminView})
            };
        },
        home: function () {
            this.adminView.renderTab(this.tabPanelView, new DashboardView());
        }
    });
});