define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var LoginHistories = require('js/collections/LoginHistories');
    var LoginHistoriesView = require('js/views/dashboard/LoginHistoriesView');

    var RecentPosts = require('js/collections/RecentPosts');
    var RecentPostsView = require('js/views/dashboard/RecentPostsView');

    var dashboard = require('text!js/templates/dashboard/dashboard.hbs');

    return Backbone.View.extend({
        dashboardTemplate: Handlebars.compile(dashboard),

        initialize: function () {
            this.loginHistories = new LoginHistories();
            this.recentPosts = new RecentPosts();
            this.render();
            this.listenTo(this.loginHistories, 'sync', this.renderLoginHistories);
            this.listenTo(this.recentPosts, 'sync', this.renderRecentPosts);
            this.loginHistories.fetch();
            this.recentPosts.fetch();
        },
        render: function () {
            this.$el.html(this.dashboardTemplate());
            this.loginHistoriesView = new LoginHistoriesView({
                el: this.$('#login-history'),
                collection: this.loginHistories
            });
            this.recentPostsView = new RecentPostsView({
                el: this.$('#recent-posts'),
                collection: this.recentPosts
            });
            this.renderLoginHistories();
            this.renderRecentPosts();
            return this;
        },
        renderLoginHistories: function () {
            this.loginHistoriesView.render();
        },
        renderRecentPosts: function () {
            this.recentPostsView.render();
        }
    });
});