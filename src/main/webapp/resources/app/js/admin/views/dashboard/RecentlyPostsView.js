define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var recentlyPosts = require('text!app/js/admin/templates/dashboard/recentlyPosts.hbs');

    return Backbone.View.extend({
        tagName: 'ul',
        className: 'list-group',
        template: Handlebars.compile(recentlyPosts),
        events: {
        },
        initialize: function () {
        },
        render: function () {
            this.$el.html(this.template({
                contents: this.collection.toJSON()
            }));
            return this;
        }
    });
});