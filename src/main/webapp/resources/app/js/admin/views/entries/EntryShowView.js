define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var EntryPreviewModalView = require('../../views/entries/EntryPreviewModalView');

    var entryShow = require('text!../../templates/entries/entryShow.hbs');

    return Backbone.View.extend({
        events: {
            'click #btn-entry-delete': '_delete',
            'click #btn-entry-preview': '_preview'
        },

        entryShowTemplate: Handlebars.compile(entryShow),

        initialize: function () {
            this.$el.html(this.entryShowTemplate(
                _.merge(this.model.toJSON(), {show: true})
            ));
        },
        render: function () {
            return this;
        },
        _delete: function () {
            if (confirm('Are you really delete?')) {
                this.model.destroy().done(function () {
                    Backbone.history.navigate('/entries', {trigger: true});
                }).fail(function (response) {
                        console.log(response);
                        alert(response.statusText);
                    });
            }
        },
        _preview: function () {
            var modalView = new EntryPreviewModalView(this.model);
            this.$el.append(modalView.render().el);
            modalView.show();
        }
    });
});