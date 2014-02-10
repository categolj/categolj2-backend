define(function (require) {
    var Backbone = require('backbone');
    var Book = require('app/js/admin/models/Book');

    return Backbone.Collection.extend({
        model: Book,
        url: function () {
            return 'api/v1/books';
        }
    });
});