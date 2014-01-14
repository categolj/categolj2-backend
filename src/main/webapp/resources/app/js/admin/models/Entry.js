define(function (require) {
    var Backbone = require('backbone');

    return Backbone.Model.extend({
        idAttribute: 'entryId',
        urlRoot: 'api/entries',
        defaults: {
            'format': 'md'
        },
        parse: function (response) {
            if (response.category && !response.categoryString) {
                var categoryString = _.map(response.category,function (c) {
                    return c.categoryName;
                }).join('::');
                response.categoryString = categoryString;
            }
            return response;
        }
    });
});