define(function (require) {
    var Backbone = require('backbone');
    var User = require('js/models/User');
    var Page = require('js/collections/Page');
    var Constants = require('js/Constants');

    return Backbone.Collection.extend(_.extend({
        model: User,
        url: function () {
            return Constants.API_ROOT + '/users';
        },
        comparator: function (a, b) {
            return a.get('lastModifiedDate') > b.get('lastModifiedDate') ? -1 : 1;
        }
    }, Page));
});