define(function (require) {
    var Backbone = require('backbone');

    return Backbone.Model.extend({
        idAttribute: 'username',
        urlRoot: 'api/users',
        validation: {
            username: {
                required: true,
                rangeLength: [1, 128],
                pattern: /^[a-z]+$/
            },
            password: {
                required: true,
                rangeLength: [6, 256]
            },
            email: {
                required: true,
                rangeLength: [1, 128],
                pattern: 'email'
            },
            firstName: {
                required: true,
                rangeLength: [1, 128]
            },
            lastName: {
                required: true,
                rangeLength: [1, 128]
            }
        }
    });
});