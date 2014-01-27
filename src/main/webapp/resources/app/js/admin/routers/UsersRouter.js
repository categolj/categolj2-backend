define(function (require) {
    var Backbone = require('backbone');
    var $ = require('jquery');
    var _ = require('underscore');

    var UsersView = require('app/js/admin/views/users/UsersView');
    var Users = require('app/js/admin/collections/Users');

    return Backbone.Router.extend({
        routes: {
            'users': 'list'
        },
        initialize: function (opts) {
            this.adminView = opts.adminView;
            this.tabPanelView = this.adminView.createTabPanelView('users');
        },
        list: function () {
            var users = new Users();
            this.adminView.renderTab(this.tabPanelView, new UsersView({
                collection: users
            }).render());
            users.fetch();
        }
    });
});