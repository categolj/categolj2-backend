define(function (require) {
    var Backbone = require('backbone');
    var LoginHistory = require('js/admin/models/LoginHistory');
    var Page = require('js/admin/collections/Page');
    var Constants = require('js/admin/Constants');

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