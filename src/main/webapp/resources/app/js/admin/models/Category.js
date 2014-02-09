define(function (require) {
    var Backbone = require('backbone');

    var Category = Backbone.Model.extend({
        label: function () {
            return this.get('categoryName').join('::');
        }
    });
    return Category;
});