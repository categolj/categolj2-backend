define(function (require) {
    var Backbone = require('backbone');
    var Category = require('app/js/admin/models/Category');

    var Categories = Backbone.Collection.extend({
        model: Category,
        url: 'api/v1/categories'
    });
    return Categories;
});