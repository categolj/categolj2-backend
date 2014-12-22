define(function (require) {
    var Backbone = require('backbone');
    var Entry = require('js/admin/models/Entry');
    var Page = require('js/admin/collections/Page');
    var Constants = require('js/admin/Constants');

    return Backbone.Collection.extend(_.extend({
        model: Entry,
        url: function () {
            return Constants.API_ROOT + '/entries';
        },
        search: function (keyword, data) {
            return this.fetch({
                data: _.extend({
                    keyword: keyword
                }, data || {})
            });
        }
    }, Page));
});