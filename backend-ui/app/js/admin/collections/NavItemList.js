define(function (require) {
    var Backbone = require('backbone');
    var NavItem = require('js/admin/models/NavItem');

    return Backbone.Collection.extend({
        model: NavItem
    });
});