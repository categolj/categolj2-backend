define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');


    var entryView = require('text!../../templates/entries/entryView.hbs');

    return Backbone.View.extend({
        events: {
        },

        entryViewTemplate: Handlebars.compile(entryView),

        initialize: function () {
            this.$el.html(this.entryViewTemplate(
                _.merge(this.model.toJSON(), {view: true})
            ));
        },
        render: function () {
            return this;
        }
    });
});