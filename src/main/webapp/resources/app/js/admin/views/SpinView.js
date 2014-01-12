define(function (require) {
    var Backbone = require('backbone');
    var Spinner = require('spin');
    var $ = require('jquery');
    var _ = require('underscore');

    return Backbone.View.extend({
        initialize: function () {
            this.spinner = new Spinner({
                radius: 60,
                width: 30,
                top: 100,
                position: 'absolute'
            });
        },
        render: function () {
            return this;
        },
        spin: function () {
            this.spinner.spin(this.el);
        },
        stop: function () {
            this.spinner.stop();
        }
    });
});
