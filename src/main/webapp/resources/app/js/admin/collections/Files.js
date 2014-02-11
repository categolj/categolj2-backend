define(function (require) {
    var Backbone = require('backbone');
    var File = require('app/js/admin/models/File');
    var Page = require('app/js/admin/collections/Page');

    return Backbone.Collection.extend(_.extend({
        model: File,
        url: 'api/v1/files',
        comparator: function (a, b) {
            return a.get('lastModifiedDate') > b.get('lastModifiedDate') ? -1 : 1;
        }
    }, Page));
});