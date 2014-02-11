define(function (require) {
    var Backbone = require('backbone');
    var _ = require('underscore');

    var FilesForm = Backbone.Model.extend({
        validation: {
            files: {
                required: true
            }
        }
    });

    return FilesForm;
});