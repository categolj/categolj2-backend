define(function (require) {
    var Backbone = require('backbone');
    var LoginHistory = require('app/js/admin/models/LoginHistory');
    var Page = require('app/js/admin/collections/Page');

    return Backbone.Collection.extend(_.extend({
        model: LoginHistory,
        url: function () {
            return 'api/loginhistories';
        },
        comparator: function (a, b) {
            return a.get('loginDate') > b.get('loginDate') ? -1 : 1;
        }
    }, Page));
});