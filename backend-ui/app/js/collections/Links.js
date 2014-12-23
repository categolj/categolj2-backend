define(function (require) {
    var Backbone = require('backbone');
    var Link = require('js/models/Link');
    var Constants = require('js/Constants');

    return Backbone.Collection.extend({
        model: Link,
        url: function () {
            return Constants.API_ROOT + '/links';
        }
    });
});