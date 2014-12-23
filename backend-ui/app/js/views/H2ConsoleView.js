define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var h2Console = require('text!js/templates/h2Console.hbs');

    return Backbone.View.extend({
        template: Handlebars.compile(h2Console),

        initialize: function () {
        },
        render: function () {
            this.$el.html(this.template());
            return this;
        }
    });
});