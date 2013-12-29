define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    return Backbone.View.extend({

        initialize: function () {
        },
        render: function () {
            this.$el.html('<p>Entries!</p>');
            return this;
        }
    });
});