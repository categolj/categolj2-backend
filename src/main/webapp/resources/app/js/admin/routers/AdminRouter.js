define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var AdminView = require('app/js/admin/views/AdminView');
    var DashboardView = require('app/js/admin/views/DashboardView');
    var EntriesRouter = require('app/js/admin/routers/EntriesRouter');
    var CategoriesRouter = require('app/js/admin/routers/CategoriesRouter');
    var UploadsRouter = require('app/js/admin/routers/UploadsRouter');
    var UsersRouter = require('app/js/admin/routers/UsersRouter');
    var ReportsRouter = require('app/js/admin/routers/ReportsRouter');
    var SystemRouter = require('app/js/admin/routers/SystemRouter');
    var H2ConsoleRouter = require('app/js/admin/routers/H2ConsoleRouter');

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
            this.adminView.renderTab(this.tabPanelView, new DashboardView().render());
        }
    });
});