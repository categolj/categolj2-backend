define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    return Backbone.View.extend({

        initialize: function (opts) {
            this.panelView = opts.panelView;
        },
        render: function () {
            this.$el.empty().html(this.panelView.render().$el);
            return this;
        }
    });
});