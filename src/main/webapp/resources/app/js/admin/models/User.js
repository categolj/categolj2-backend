define(function (require) {
    var Backbone = require('backbone');

    return Backbone.Model.extend({
        idAttribute: 'username',
        urlRoot: 'api/users'
    });
});