define(function (require) {
    var Backbone = require('backbone');
    var Category = require('app/js/admin/models/Category');
    var Constants = require('app/js/admin/Constants');

    var Categories = Backbone.Collection.extend({
        model: Category,
        url: Constants.API_ROOT + '/categories'
    });
    return Categories;
});