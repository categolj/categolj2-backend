define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    require('backbone.stickit');
    var $ = require('jquery');
    var _ = require('underscore');

    var entryForm = require('text!../../templates/entries/entryForm.hbs');
    var entryView = require('text!../../templates/entries/entryView.hbs');

    return Backbone.View.extend({
        events: {
            'click #btn-entry-confirm': 'doCreateConfirm',
            'click #btn-entry-back-to-form': 'render',
            'click #btn-entry-create': 'doCreate'
        },
        bindings: {
            '#title': 'title',
            '#categoryString': 'categoryString',
            '#contents': 'contents',
            '#entryForm input[name=format]': 'format',
            '#entryForm input[name=published]': 'published'
        },

        entryFormTemplate: Handlebars.compile(entryForm),
        entryViewTemplate: Handlebars.compile(entryView),

        initialize: function () {
        },
        render: function () {
            this.$el.empty().html(this.entryFormTemplate());
            this.stickit();
            return this;
        },
        doCreateConfirm: function () {
            this.$el.empty().html(this.entryViewTemplate(
                _.merge(this.model.toJSON(), {create: true})
            ));
            return false;
        },
        doCreate: function () {
            this.model.save().done(function () {
                Backbone.history.navigate('/entries', {trigger: true});
            }).fail(function (response) {
                    console.log(response);
                    alert(response.statusText);
                });
            return false;
        }
    });
});