define(function (require) {
    var Backbone = require('backbone');
    var Handlebars = require('handlebars');
    var $ = require('jquery');
    var _ = require('underscore');
    var AccessLogs = require('app/js/admin/collections/AccessLogs');
    var AccessLogReports = require('app/js/admin/collections/AccessLogReports');
    require('dynatable');

    var accesslogTable = require('text!app/js/admin/templates/reports/accesslogTable.hbs');

    return Backbone.View.extend({
        template: Handlebars.compile(accesslogTable),
        initialize: function () {
            this.accessLogs = new AccessLogs();
            this.accessLogReports = new AccessLogReports();
            this.listenTo(this.accessLogs, 'sync', this.renderAccessLogs);
            this.listenTo(this.accessLogReports, 'sync', this.renderAccessLogReports);
            this.accessLogs.fetch({
                data: {
                    size: 1000
                }
            });
            this.accessLogReports.fetch({
                data: {
                    byUri: ''
                }
            });
        },
        render: function () {
            this.$el.html(this.template());
            return this;
        },
        renderAccessLogs: function () {
            this.$('#accesslog-table').dynatable({
                dataset: {
                    records: this.accessLogs.toJSON()
                }
            });
            return this;
        },
        renderAccessLogReports: function () {
            this.$('#accesslog-report-table').dynatable({
                dataset: {
                    records: this.accessLogReports.toJSON()
                }
            });
            return this;
        }
    });
});