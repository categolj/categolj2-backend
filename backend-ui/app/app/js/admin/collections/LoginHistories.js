define(function (require) {
    var Backbone = require('backbone');
    var LoginHistory = require('app/js/admin/models/LoginHistory');
    var Page = require('app/js/admin/collections/Page');
    var Constants = require('app/js/admin/Constants');

    return Backbone.Collection.extend(_.extend({
        model: LoginHistory,
        url: function () {
            return Constants.API_ROOT + '/loginhistories';
        },
        comparator: function (a, b) {
            return a.get('loginDate') > b.get('loginDate') ? -1 : 1;
        }
    }, Page));
});