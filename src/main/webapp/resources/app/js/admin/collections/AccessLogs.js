define(function (require) {
    var Backbone = require('backbone');
    var AccessLog = require('app/js/admin/models/AccessLog');
    var Page = require('app/js/admin/collections/Page');

    return Backbone.Collection.extend(_.extend({
        model: AccessLog,
        url: function () {
            return 'api/v1/accesslogs';
        }
    }, Page));
});