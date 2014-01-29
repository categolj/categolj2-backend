define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    return Backbone.View.extend({
        events: {
            'click .close': 'hide'
        },
        initialize: function () {
        },
        render: function () {
            return this;
        },
        showMessage: function (message) {
            this.$el
                .removeClass('hidden')
                .find('p').text(message);
            return this;
        },
        hide: function () {
            this.$el
                .addClass('hidden')
            return false;
        }
    });
});