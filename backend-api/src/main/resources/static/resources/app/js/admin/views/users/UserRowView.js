define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var ErrorHandler = require('app/js/admin/views/ErrorHandler');
    var userRow = require('text!app/js/admin/templates/users/userRow.hbs');

    return Backbone.View.extend(_.extend({
        tagName: 'tr',
        template: Handlebars.compile(userRow),

        events: {
            'dblclick': '_toggleEdit',
            'keypress': '_finishEdit',
            'click #btn-user-edit': '_enableEdit',
            'click #btn-user-delete': '_delete',
            'click #btn-user-update': '_update'
        },
        bindings: {
            '[name=username]': 'username',
            '[name=email]': 'email',
            '[name=firstName]': 'firstName',
            '[name=lastName]': 'lastName',
            '[name=roles]': {
                observe: 'roles',
                onGet: function (value) {
                    return _.map(value, String);
                },
                onSet: function (value) {
                    return _.map(value, Number);
                }
            },
            '[name=enabled]': 'enabled',
            '[name=locked]': 'locked'
        },

        initialize: function () {
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            this.stickit();
            return this;
        },
        _toggleEdit: function () {
            if (this.$el.hasClass('editing')) {
                this._finishEdit();
            } else {
                this._enableEdit();
            }
        },
        _finishEdit: function (e) {
            if (e && e.keyCode != 13) return;
            this._update();
        },
        _enableEdit: function () {
            this.$el.addClass('editing');
            this.render();
            this.$('input:checkbox').prop('disabled', false);
        },
        _disableEdit: function () {
            this.$el.removeClass('editing');
            this.render();
            this.$('input:checkbox').prop('disabled', true);
        },
        _delete: function () {
            if (confirm('Are you sure to delete?')) {
                this.model.destroy({wait: true})
                    .success(_.bind(this.remove, this))
                    .fail(_.bind(function (response) {
                        this.showErrors(response.responseJSON);
                    }, this));
            }
            return false;
        },
        _update: function () {
            this.model.save()
                .success(_.bind(this._disableEdit, this))
                .fail(_.bind(function (response) {
                    this.showErrors(response.responseJSON);
                }, this));
            return false;
        }
    }, ErrorHandler));
});
