define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    return Backbone.View.extend({
        events: {
        },

        initialize: function () {
        },
        render: function () {
            this.$el.load('system/info');
            return this;
        },
        threadDump: function () {
            this.$el.load('system/threadDump');
            return this;
        }
    });
});