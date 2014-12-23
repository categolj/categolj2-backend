define(function (require) {
    var Backbone = require('backbone');
    var Logger = require('js/models/Logger');
    var Constants = require('js/Constants');

    return Backbone.Collection.extend({
        model: Logger,
        url: function () {
            return Constants.API_ROOT + '/loggers';
        }
    });
});