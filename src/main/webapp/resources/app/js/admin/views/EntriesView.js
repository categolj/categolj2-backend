define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    require('backbone.stickit');
    var $ = require('jquery');
    var _ = require('underscore');

    var entryCreateConfirm = require('text!../templates/entryCreateConfirm.hbs');

    return Backbone.View.extend({
        events: {
            'click #btn-entry-confirm': 'createConfirm',
            'click #btn-entry-back-to-form': 'createForm',
            'click #btn-entry-create': 'create'
        },
        bindings: {
            '#title': 'title',
            '#categoryString': 'categoryString',
            '#contents': 'contents',
            '#entryForm input[name=format]': 'format',
            '#entryForm input[name=published]': 'published'
        },
        entryCreateConfirmTemplate: Handlebars.compile(entryCreateConfirm),

        initialize: function () {
        },
        render: function () {
            this.$el.load('entries');
            return this;
        },
        createForm: function () {
            this.$el.load('entries?form', _.bind(function () {
                this.stickit();
                this.delegateEvents();

                var csrfToken = this.$("[name='_csrf']").val();
                $(document).ajaxSend(function (event, xhr, options) {
                    xhr.setRequestHeader('X-CSRF-TOKEN', csrfToken);
                });
            }, this));
            return this;
        },
        createConfirm: function () {
            this.$el.html(this.entryCreateConfirmTemplate(this.model.toJSON()));
            return false;
        },
        create: function () {
            this.model.save({
                success: _.bind(function () {
                    Backbone.history.navigate('entries', {trigger: true});
                }, this),
                error: _.bind(function (m, e) {
                    console.log(m, e);
                }, this)
            });
            return false;
        }
    });
});