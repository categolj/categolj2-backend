define(function (require) {
    var Backbone = require('backbone');
    var LoginHistory = require('app/js/admin/models/LoginHistory');
    var Page = require('app/js/admin/collections/Page');

    return Backbone.Collection.extend(_.extend(Page, {
        model: LoginHistory,
        url: function () {
            return '/api/loginhistories';
        },
        comparator: function (a, b) {
            return a.get('loginDate') > b.get('logDate') ? -1 : 1;
        }
    }));
});