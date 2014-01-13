define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var LoginHistories = require('app/js/admin/collections/LoginHistories');
    var LoginHistoriesView = require('app/js/admin/views/dashboard/LoginHistoriesView');

    var dashboard = require('text!app/js/admin/templates/dashboard/dashboard.hbs');


    return Backbone.View.extend({
        dashboardTemplate: Handlebars.compile(dashboard),

        initialize: function () {
            this.collection = new LoginHistories();
            this.render();
            this.listenTo(this.collection, 'sync', this.renderLoginHistories);
            this.collection.fetch();
        },
        render: function () {
            this.$el.html(this.dashboardTemplate());
            this.loginHistoriesView = new LoginHistoriesView({
                el: this.$('#login-history'),
                collection: this.collection
            });
            this.renderLoginHistories();
            return this;
        },
        renderLoginHistories: function () {
            console.log('foo', this.loginHistoriesView);
            this.loginHistoriesView.render();
        }
    });
});