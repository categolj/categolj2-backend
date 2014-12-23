define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var ErrorHandler = require('js/views/ErrorHandler');
    var linkRow = require('text!js/templates/links/linkRow.hbs');

    return Backbone.View.extend(_.extend({
        tagName: 'tr',
        template: Handlebars.compile(linkRow),

        events: {
            'dblclick': '_toggleEdit',
            'keypress': '_finishEdit',
            'click #btn-link-edit': '_enableEdit',
            'click #btn-link-delete': '_delete',
            'click #btn-link-update': '_update'
        },
        bindings: {
            '[name=linkName]': 'linkName'
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
