define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var ErrorHandler = require('js/views/ErrorHandler');
    var fileRow = require('text!js/templates/uploads/fileRow.hbs');

    return Backbone.View.extend(_.extend({
        tagName: 'tr',
        template: Handlebars.compile(fileRow),

        events: {
            'click #btn-file-delete': '_delete'
        },
        bindings: {
        },

        initialize: function () {
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSONForView()));
            this.stickit();
            return this;
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
        }
    }, ErrorHandler));
});
