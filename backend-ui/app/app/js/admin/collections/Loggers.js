define(function (require) {
    var Backbone = require('backbone');
    var Logger = require('app/js/admin/models/Logger');
    var Constants = require('app/js/admin/Constants');

    return Backbone.Collection.extend({
        model: Logger,
        url: function () {
            return Constants.API_ROOT + '/loggers';
        }
    });
});