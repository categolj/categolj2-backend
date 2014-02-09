define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var LoginHistories = require('app/js/admin/collections/LoginHistories');
    var LoginHistoriesView = require('app/js/admin/views/dashboard/LoginHistoriesView');

    var RecentlyPosts = require('app/js/admin/collections/RecentlyPosts');
    var RecentlyPostsView = require('app/js/admin/views/dashboard/RecentlyPostsView');

    var dashboard = require('text!app/js/admin/templates/dashboard/dashboard.hbs');

    return Backbone.View.extend({
        dashboardTemplate: Handlebars.compile(dashboard),

        initialize: function () {
            this.loginHistories = new LoginHistories();
            this.recentlyPosts = new RecentlyPosts();
            this.render();
            this.listenTo(this.loginHistories, 'sync', this.renderLoginHistories);
            this.listenTo(this.recentlyPosts, 'sync', this.renderRecentlyPosts);
            this.loginHistories.fetch();
            this.recentlyPosts.fetch();
        },
        render: function () {
            this.$el.html(this.dashboardTemplate());
            this.loginHistoriesView = new LoginHistoriesView({
                el: this.$('#login-history'),
                collection: this.loginHistories
            });
            this.recentlyPostsView = new RecentlyPostsView({
                el: this.$('#recently-posts'),
                collection: this.recentlyPosts
            });
            this.renderLoginHistories();
            this.renderRecentlyPosts();
            return this;
        },
        renderLoginHistories: function () {
            this.loginHistoriesView.render();
        },
        renderRecentlyPosts: function () {
            this.recentlyPostsView.render();
        }
    });
});