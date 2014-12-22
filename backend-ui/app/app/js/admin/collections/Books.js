define(function (require) {
    var Backbone = require('backbone');
    var Book = require('app/js/admin/models/Book');
    var Constants = require('app/js/admin/Constants');

    return Backbone.Collection.extend({
        model: Book,
        url: function () {
            return Constants.API_ROOT + '/books';
        }
    });
});