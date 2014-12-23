define(function (require) {
    var Backbone = require('backbone');
    var LoginHistory = require('js/models/LoginHistory');
    var Page = require('js/collections/Page');
    var Constants = require('js/Constants');

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