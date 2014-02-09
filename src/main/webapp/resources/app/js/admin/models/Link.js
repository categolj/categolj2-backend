define(function (require) {
    var Backbone = require('backbone');

    return Backbone.Model.extend({
        idAttribute: 'id',
        urlRoot: 'api/v1/links',
        url: function () {
            if (this.get('id')) {
                // do not use encodeURIComponent
                return this.urlRoot + '/' + encodeURI(this.get('id'));
            } else {
                return this.urlRoot;
            }
        },
        validation: {
            url: {
                required: true,
                rangeLength: [1, 128],
                pattern: 'url'
            },
            linkName: {
                required: true,
                rangeLength: [1, 128]
            }
        }
    });
});