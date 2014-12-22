define(function (require) {
    var Backbone = require('backbone');
    var Link = require('js/admin/models/Link');
    var Constants = require('js/admin/Constants');

    return Backbone.Collection.extend({
        model: Link,
        url: function () {
            return Constants.API_ROOT + '/links';
        }
    });
});