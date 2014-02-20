define(function (require) {
    var Backbone = require('backbone');
    var Logger = require('app/js/admin/models/Logger');

    return Backbone.Collection.extend({
        model: Logger,
        url: function () {
            return 'api/v1/loggers';
        }
    });
});