define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');
    require('bootstrap');

    var modal = require('text!js/templates/modal.hbs');

    /**
     * @see http://stackoverflow.com/questions/16085852/rendering-bootstrap-modal-using-backbone
     */
    return Backbone.View.extend({
        className: 'modal fade',
        template: Handlebars.compile(modal),

        events: {
            'hidden.bs.modal': 'tearDown'
        },

        initialize: function (opts) {
            this.opts = opts;
        },

        show: function () {
            this.$el.modal('show');
        },

        tearDown: function () {
            this.$el.data('modal', null);
            this.remove();
        },

        render: function () {
            this.$el.html(this.template(this.opts));
            this.$el.modal(_.extend({
                show: false,
                keyboard: true
            }, this.opts)); // don't show modal on instantiation
            return this;
        }
    });
});