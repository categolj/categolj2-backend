define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var userRow = require('text!app/js/admin/templates/users/userRow.hbs');

    return Backbone.View.extend({
        tagName: 'tr',
        template: Handlebars.compile(userRow),

        initialize: function () {
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        }
    });
});
