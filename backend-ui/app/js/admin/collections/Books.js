define(function (require) {
    var Backbone = require('backbone');
    var Book = require('js/admin/models/Book');
    var Constants = require('js/admin/Constants');

    return Backbone.Collection.extend({
        model: Book,
        url: function () {
            return Constants.API_ROOT + '/books';
        }
    });
});