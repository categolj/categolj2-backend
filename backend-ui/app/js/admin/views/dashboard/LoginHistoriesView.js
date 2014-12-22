define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var loginHistories = require('text!js/admin/templates/dashboard/loginHistories.hbs');

    return Backbone.View.extend({
        className: 'list-group',
        template: Handlebars.compile(loginHistories),
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