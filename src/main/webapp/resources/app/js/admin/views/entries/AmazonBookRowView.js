define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var ErrorHandler = require('app/js/admin/views/ErrorHandler');
    var amazonBookRow = require('text!app/js/admin/templates/entries/amazonBookRow.hbs');

    return Backbone.View.extend({
        tagName: 'tr',
        template: Handlebars.compile(amazonBookRow),

        events: {
            'click #btn-insert-book': '_insertBook'
        },

        initialize: function () {
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            this.stickit();
            return this;
        },
        _insertBook: function (e) {
            e.preventDefault();
            console.log(this.model);
        }
    });
});
