define(function (require) {
    var Backbone = require('backbone');

    return Backbone.Model.extend({
        idAttribute: 'id',
        urlRoot: 'api/users',
        defaults: {
            'enabled': true,
            'locked': false,
            'roles': [2] // Editor
        },
        validation: {
            username: {
                required: true,
                rangeLength: [1, 128],
                pattern: /^[a-zA-Z0-9]+$/
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
            },
            roles: {
                required: true
            },
            enabled: {
                required: true
            },
            locked: {
                required: true
            }
        }
    });
});