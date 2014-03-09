define(function (require) {
    var Backbone = require('backbone');
    var AccessLogReport = require('app/js/admin/models/AccessLogReport');

    return Backbone.Collection.extend({
        model: AccessLogReport,
        url: function () {
            return 'api/v1/accesslogreports';
        }
    });
});