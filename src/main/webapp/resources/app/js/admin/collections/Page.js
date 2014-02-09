define(function (require) {
    var Backbone = require('backbone');

    return {
        initialize: function (options) {
            options = _.extend({}, options);
            this.page = Number(options.page) || 0;
            this.pageSize = Number(options.pageSize) || 10;
        },
        parse: function (response) {
            this.firstPage = response.firstPage;
            this.lastPage = response.lastPage;
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
            return response.content;
        },
        pagingData: function () {
            return {
                'page': this.page,
                'size': this.pageSize
            }
        }
    };
});