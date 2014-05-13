define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var StackTraceModalView = require('app/js/admin/views/system/StackTraceModalView');

    var threadDumpRow = require('text!app/js/admin/templates/system/threadDumpRow.hbs');

    return Backbone.View.extend({
        tagName: 'tr',
        template: Handlebars.compile(threadDumpRow),

        events: {
            'click button.stacktrace': 'showStackTrace'
        },

        initialize: function () {
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSONForView()));
            return this;
        },
        showStackTrace: function () {
            var modalView = new StackTraceModalView(this.model);
            this.$el.append(modalView.render().el);
            modalView.show();
        }
    });
});
