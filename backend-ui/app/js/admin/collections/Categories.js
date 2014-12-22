define(function (require) {
    var Backbone = require('backbone');
    var Category = require('js/admin/models/Category');
    var Constants = require('js/admin/Constants');

    var Categories = Backbone.Collection.extend({
        model: Category,
        url: Constants.API_ROOT + '/categories'
    });
    return Categories;
});