define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var apis = require('text!js/admin/templates/apis/apis.hbs');;

    return Backbone.View.extend({
        template: Handlebars.compile(apis),
        initialize: function () {
            this.$el.html(this.template());
        },
        render: function () {
            return this;
        }
    });
});