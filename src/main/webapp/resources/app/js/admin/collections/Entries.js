define(function (require) {
    var Backbone = require('backbone');
    var Entry = require('app/js/admin/models/Entry');
    var Page = require('app/js/admin/collections/Page');

    return Backbone.Collection.extend(_.extend(Page, {
        model: Entry,
        parse: function (response) {
            this.firstPage = response.firstPage;
            this.lastPage = response.lastPage;
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
            return response.content;
        },
        url: function () {
            return 'api/entries';
        }
    }));
});