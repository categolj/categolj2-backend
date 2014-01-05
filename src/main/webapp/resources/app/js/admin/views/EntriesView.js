define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    return Backbone.View.extend({
        events: {
            'click #btn-entry-confirm': 'createConfirm'
        },

        initialize: function () {
        },
        render: function () {
            this.$el.load('entries');
            return this;
        },
        createForm: function () {
            this.$el.load('entries?form');
            return this;
        }
    });
});