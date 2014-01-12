define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    require('backbone.stickit');
    var $ = require('jquery');
    var _ = require('underscore');

    var ButtonView = require('../../views/ButtonView');
    var EntryPreviewModalView = require('../../views/entries/EntryPreviewModalView');

    var entryForm = require('text!../../templates/entries/entryForm.hbs');
    var entryShow = require('text!../../templates/entries/entryShow.hbs');

    return Backbone.View.extend({
        events: {
            'click #btn-entry-confirm': '_confirm',
            'click #btn-entry-back-to-form': 'render',
            'click #btn-entry-create': '_create',
            'click #btn-entry-update': '_update',
            'click #btn-entry-preview': '_preview'
        },
        bindings: {
            '#title': 'title',
            '#categoryString': 'categoryString',
            '#contents': 'contents',
            '#format': 'format',
            '#published': 'published',
            '#updateLastModifiedDate': 'updateLastModifiedDate',
            '#saveInHistory': 'saveInHistory'
        },

        entryFormTemplate: Handlebars.compile(entryForm),
        entryShowTemplate: Handlebars.compile(entryShow),

        initialize: function () {
            if (this.model.id) {
                this.templateOpts = {
                    update: true
                };
            } else {
                this.templateOpts = {
                    create: true
                };
            }
        },
        render: function () {
            this.$el.empty().html(this.entryFormTemplate(
                _.merge(this.model.toJSON(), this.templateOpts)
            ));
            this.stickit();
            return this;
        },
        _confirm: function () {
            this.$el.empty().html(this.entryShowTemplate(
                _.merge(this.model.toJSON(), this.templateOpts)
            ));
            this.buttonView = new ButtonView({
                el: this.$('.btn')
            });
            return false;
        },
        _create: function () {
            this.model.save().done(_.bind(function () {
                Backbone.history.navigate('/entries', {
                    trigger: true
                });
            }, this)).fail(_.bind(this._handleError, this));
            return false;
        },
        _update: function () {
            this.model.save().done(_.bind(function () {
                Backbone.history.navigate('/entries/' + this.model.id, {
                    trigger: true
                });
            }, this)).fail(_.bind(this._handleError, this));
            return false;
        },
        _handleError: function (response) {
            console.log(response);
            this.buttonView.enable();
            this.render();

            // show field errors
            if (response.responseJSON.details) {
                this._showErrors(response.responseJSON.details);
            }
        },
        _showErrors: function (details) {
            _.each(details, function (detail) {
                var $target = this.$('#' + detail.target.split('.')[1]);
                if ($target.length) {
                    $target.parent().parent().addClass('has-error');
                    $('<p>').addClass('text-danger').text(detail.message).appendTo($target.parent());
                }
            });
        },
        _preview: function () {
            var modalView = new EntryPreviewModalView(this.model);
            this.$el.append(modalView.render().el);
            modalView.show();
            if (this.buttonView) {
                this.buttonView.enable();
            }
        }
    });
});