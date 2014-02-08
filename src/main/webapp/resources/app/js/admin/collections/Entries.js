define(function (require) {
    var Backbone = require('backbone');
    var Entry = require('app/js/admin/models/Entry');
    var Page = require('app/js/admin/collections/Page');

    return Backbone.Collection.extend(_.extend({
        model: Entry,
        url: function () {
            return 'api/v1/entries';
        }
    }, Page));
});