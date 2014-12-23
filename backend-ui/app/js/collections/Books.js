define(function (require) {
    var Backbone = require('backbone');
    var Book = require('js/models/Book');
    var Constants = require('js/Constants');

    return Backbone.Collection.extend({
        model: Book,
        url: function () {
            return Constants.API_ROOT + '/books';
        }
    });
});