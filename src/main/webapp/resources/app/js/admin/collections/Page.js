define(function (require) {
    var Backbone = require('backbone');

    return {
        parse: function (response) {
            this.firstPage = response.firstPage;
            this.lastPage = response.lastPage;
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
            return response.content;
        }
    };
});