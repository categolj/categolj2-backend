define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var userRow = require('text!app/js/admin/templates/users/userRow.hbs');

    return Backbone.View.extend({
        tagName: 'tr',
        template: Handlebars.compile(userRow),

        events: {
            'dblclick': '_startEdit',
            'keypress': '_finishEdit'
        },
        bindings: {
            '[name=username]': 'username',
            '[name=email]': 'email',
            '[name=firstName]': 'firstName',
            '[name=lastName]': 'lastName'
        },

        initialize: function () {
            this.listenTo(this.model, 'change', this.render);
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            this.stickit();
            return this;
        },
        _startEdit: function () {
            this.$el.toggleClass('editing');
        },
        _finishEdit: function (e) {
            if (e.keyCode != 13) return;
            this.$el.removeClass('editing');
        }
    });
});
