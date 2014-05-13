define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');

    var ThreadDump = require('app/js/admin/models/ThreadDump');
    var ThreadDumpRowView = require('app/js/admin/views/system/ThreadDumpRowView');

    var systemInfo = require('text!app/js/admin/templates/system/systemInfo.hbs');
    var threadDump = require('text!app/js/admin/templates/system/threadDump.hbs');


    return Backbone.View.extend({
        template: Handlebars.compile(systemInfo),
        threadDumpTemplate: Handlebars.compile(threadDump),
        events: {
        },

        initialize: function () {
        },
        render: function () {
            $.getJSON('management/env')
                .success(_.bind(this.renderSystemInfo, this));
            return this;
        },
        renderSystemInfo: function (env) {
            var html = this.template({env: env});
            this.$el.html(html);
            return this;
        },
        threadDump: function () {
            $.getJSON('management/dump')
                .success(_.bind(this.renderThreadDump, this));
            return this;
        },
        renderThreadDump: function (dump) {
            var html = this.threadDumpTemplate({dump: dump});
            this.$el.html(html);
            var $tbody = this.$('tbody').empty();
            _.each(dump, function (row) {
                $tbody.append(new ThreadDumpRowView({
                    model: new ThreadDump(row)
                }).render().el)
            });
            return this;
        }
    });
});