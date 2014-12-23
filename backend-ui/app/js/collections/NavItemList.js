define(function (require) {
    var Backbone = require('backbone');
    var NavItem = require('js/models/NavItem');

    return Backbone.Collection.extend({
        model: NavItem
    });
});