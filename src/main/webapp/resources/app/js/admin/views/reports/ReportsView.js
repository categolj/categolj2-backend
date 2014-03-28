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
        events: {
            'click #btn-refresh-reports': '_onClickRefresh',
            'click .delete-accesslog': '_onClickDeleteAccessLog'
        },
        initialize: function () {
            this.accessLogs = new AccessLogs();
            this.accessLogReports = new AccessLogReports();
            this.listenTo(this.accessLogs, 'sync', this.renderAccessLogs);
            this.listenTo(this.accessLogReports, 'sync', this.renderAccessLogReports);
            this.refresh();
        },
        render: function () {
            this.$el.html(this.template());
            return this;
        },
        refresh: function () {
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
        remoteAddressCellTemplate: Handlebars.compile(
            '<td style="text-align: center">' +
                '<button data-remote-address="{{remoteAddress}}" class="delete-accesslog btn btn-danger btn-xs">{{remoteAddress}}</button>' +
                '</td>'
        ),
        _onClickDeleteAccessLog: function (e) {
            e.preventDefault();
            var $e = $(e.currentTarget),
                remoteAddress = $e.data('remote-address');
            if (confirm('Are you sure to delete access logs from ' + remoteAddress)) {
                this.accessLogs.deleteByRemoteAddress(remoteAddress)
                    .success(_.bind(function () {
                        this.refresh();
                    }, this));
            }
        },
        _onClickRefresh: function (e) {
            e.preventDefault();
            this.refresh();
        },
        accessLogsRowWriter: function (rowIndex, record, columns, cellWriter) {
            var tr = '';
            for (var i = 0, len = columns.length; i < len; i++) {
                var column = columns[i];
                if (column.id == 'remoteAddress') {
                    tr += this.remoteAddressCellTemplate(record);
                } else {
                    tr += cellWriter(columns[i], record);
                }
            }
            return '<tr>' + tr + '</tr>';
        },
        cellWriter: function (column, record) {
            var html = column.attributeWriter(record);
            if (_.contains(['method', 'uri', 'query'], column.id)) {
                html = '<code>' + html + '</code>';
            }
            return  '<td>' + html + '</td>';
        },
        renderAccessLogs: function () {
            var $table = this.$('#accesslog-table');
            var dynatable = $table.data('dynatable');
            if (dynatable) {
                dynatable.settings.dataset.originalRecords = this.accessLogs.toJSON();
                dynatable.process();
            } else {
                $table.dynatable({
                    dataset: {
                        records: this.accessLogs.toJSON()
                    },
                    writers: {
                        _rowWriter: _.bind(this.accessLogsRowWriter, this),
                        _cellWriter: _.bind(this.cellWriter, this)
                    }
                });
            }
            return this;
        },
        renderAccessLogReports: function () {
            var $table = this.$('#accesslog-report-table');
            var dynatable = $table.data('dynatable');
            if (dynatable) {
                dynatable.settings.dataset.originalRecords = this.accessLogReports.toJSON();
                dynatable.process();
            } else {
                $table.dynatable({
                    dataset: {
                        records: this.accessLogReports.toJSON()
                    },
                    writers: {
                        _cellWriter: _.bind(this.cellWriter, this)
                    }
                });
            }
            return this;
        }
    });
});