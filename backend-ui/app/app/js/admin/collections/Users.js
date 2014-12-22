define(function (require) {
    var Backbone = require('backbone');
    var User = require('app/js/admin/models/User');
    var Page = require('app/js/admin/collections/Page');
    var Constants = require('app/js/admin/Constants');

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