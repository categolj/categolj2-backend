define(function (require) {
    var Backbone = require('backbone');
    var AccessLogReport = require('app/js/admin/models/AccessLogReport');
    var Constants = require('app/js/admin/Constants');

    return Backbone.Collection.extend({
        model: AccessLogReport,
        url: function () {
            return Constants.API_ROOT + '/accesslogreports';
        },
        comparator: function (a, b) {
            return a.get('count') > b.get('count') ? -1 : 1;
        }
    });
});