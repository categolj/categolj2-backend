define(function (require) {
    var Backbone = require('backbone');
    var Constants = require('app/js/admin/Constants');

    return Backbone.Model.extend({
        idAttribute: 'id',
        urlRoot: Constants.API_ROOT + '/links',
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