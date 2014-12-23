define(function (require) {
    var Backbone = require('backbone');
    var AccessLogReport = require('js/models/AccessLogReport');
    var Constants = require('js/Constants');

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