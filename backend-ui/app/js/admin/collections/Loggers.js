define(function (require) {
    var Backbone = require('backbone');
    var Logger = require('js/admin/models/Logger');
    var Constants = require('js/admin/Constants');

    return Backbone.Collection.extend({
        model: Logger,
        url: function () {
            return Constants.API_ROOT + '/loggers';
        }
    });
});