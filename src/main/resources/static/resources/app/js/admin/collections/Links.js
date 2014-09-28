define(function (require) {
    var Backbone = require('backbone');
    var Link = require('app/js/admin/models/Link');

    return Backbone.Collection.extend({
        model: Link,
        url: function () {
            return 'api/v1/links';
        }
    });
});