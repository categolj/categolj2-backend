define(function (require) {
    var PaginationView = require('js/views/PaginationView');

    var SearchPaginationView = PaginationView.extend({
        goFirst: function (e) {
            e.preventDefault();
            this.collection.page = 0;
            this.collection.search(this.keyword, this.collection.pagingData());
        },
        goTo: function (e) {
            e.preventDefault();
            var $a = $(e.target),
                page = $a.data('page');
            this.collection.page = page;
            this.collection.search(this.keyword, this.collection.pagingData());
        },
        goLast: function (e) {
            e.preventDefault();
            this.collection.page = this.last;
            this.collection.search(this.keyword, this.collection.pagingData());
        }
    });
    return SearchPaginationView;
});