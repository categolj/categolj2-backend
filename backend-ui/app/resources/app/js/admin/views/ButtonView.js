define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    return Backbone.View.extend({
        events: {
            'click': 'disable'
        },
        initialize: function () {
        },
        render: function () {
            return this;
        },
        disable: function () {
            this.$el.addClass('disabled');
        },
        enable: function () {
            this.$el.removeClass('disabled');
        }
    });
});