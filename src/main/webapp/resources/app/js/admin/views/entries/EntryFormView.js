define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    require('backbone.stickit');
    var $ = require('jquery');
    var _ = require('underscore');

    var EntryHistories = require('app/js/admin/collections/EntryHistories');
    var ButtonView = require('app/js/admin/views/ButtonView');
    var EntryPreviewModalView = require('app/js/admin/views/entries/EntryPreviewModalView');

    var entryForm = require('text!app/js/admin/templates/entries/entryForm.hbs');
    var entryShow = require('text!app/js/admin/templates/entries/entryShow.hbs');
    var entryTable = require('text!app/js/admin/templates/entries/entryTable.hbs');

    return Backbone.View.extend({
        events: {
            'click #btn-entry-confirm': '_confirm',
            'click #btn-entry-back-to-form': 'render',
            'click #btn-entry-update-form': '_updateForm',
            'click #btn-entry-create': '_create',
            'click #btn-entry-update': '_update',
            'click #btn-entry-delete': '_delete',
            'click #btn-entry-preview': '_preview',
            'click #btn-entry-apply-history': '_applyHistory'
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
        entryTableTemplate: Handlebars.compile(entryTable),

        initialize: function () {
            if (this.model.id) {
                this.templateOpts = {
                    update: true
                };
                this.entryHistories = new EntryHistories().entry(this.model);
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
            if (this.entryHistories) {
                this.entryHistories.fetch();
            }
            return this;
        },
        show: function () {
            this.$el.empty().html(this.entryShowTemplate(
                _.merge(this.model.toJSON(), {show: true})
            ));
            return this;
        },
        _appendEntryHistoryTable: function () {
            this.$el.append(this.entryTableTemplate({
                content: this.entryHistories.toJSON(),
                history: true
            }));
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
        _updateForm: function () {
            Backbone.history.navigate('entries/' + this.model.id + '/form');
            this.render();
            return false;
        },
        _create: function () {
            this.model.save()
                .done(_.bind(function () {
                    Backbone.history.navigate('entries', {
                        trigger: true
                    });
                }, this)).fail(_.bind(this._handleError, this));
            return false;
        },
        _update: function () {
            this.model.save()
                .done(_.bind(function () {
                    Backbone.history.navigate('entries/' + this.model.id, {
                        trigger: true
                    });
                }, this)).fail(_.bind(this._handleError, this));
            return false;
        },
        _delete: function () {
            if (confirm('Are you really delete?')) {
                this.model.destroy()
                    .done(_.bind(function () {
                        Backbone.history.navigate('entries', {
                            trigger: true
                        });
                    }, this)).fail(_.bind(function (response) {
                        console.log(response);
                        if (response.responseJSON.details) {
                            this._showErrors(response.responseJSON.details);
                        }
                    }, this));
            }
        },
        _handleError: function (response) {
            console.log(response);
            if (this.buttonView) {
                this.buttonView.enable();
            }
            this.render();

            if (response.responseJSON.details) {
                this._showErrors(response.responseJSON.details);
            }
        },
        _showErrors: function (details) {
            _.each(details, function (detail) {
                var $target = this.$('#' + detail.target.split('.')[1]);
                if ($target.length) {
                    $target.parent().parent()
                        .addClass('has-error');
                    $('<p>')
                        .addClass('text-danger')
                        .text(detail.message)
                        .appendTo($target.parent());
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
        },
        _applyHistory: function (e) {
            var $btn = $(e.currentTarget);
            var history = this.entryHistories.get($btn.data('entry-history-id'));
            this.model.set({
                title: history.get('title'),
                contents: history.get('contents'),
                format: history.get('format')
            });
            return false;
        }
    });
});