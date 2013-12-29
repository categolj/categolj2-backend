define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var navItemList = require('text!../templates/navItemList.hbs');

    return Backbone.View.extend({
        template: Handlebars.compile(navItemList),

        initialize: function () {
        },
        render: function () {
            this.$el.html(this.template({
                items: this.collection.toJSON()
            }));
            return this;
        }
    });
});