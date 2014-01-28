define(function (require) {
    var Backbone = require('backbone');
    var marked = require('marked');

    return Backbone.Model.extend({
        idAttribute: 'id',
        urlRoot: 'api/entries',
        defaults: {
            'format': 'md'
        },
        validation: {
            title: {
                required: true,
                rangeLength: [1, 512]
            },
            categoryString: {
                required: true
            },
            contents: {
                required: true,
                rangeLength: [1, 65536]
            },
            format: {
                required: true,
                rangeLength: [1, 10]
            }
        },
        parse: function (response) {
            if (response.category && !response.categoryString) {
                var categoryString = _.map(response.category,function (c) {
                    return c.categoryName;
                }).join('::');
                response.categoryString = categoryString;
            }
            return response;
        },
        getFormattedContents: function () {
            var body = this.get('contents');
            if (body) {
                switch (this.get('format')) {
                    case 'md':
                    {
                        body = marked(this.get('contents'));
                        break;
                    }
                }
            }
            return body;
        }
    });
});