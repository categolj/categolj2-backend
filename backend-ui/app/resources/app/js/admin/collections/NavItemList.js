define(function (require) {
    var Backbone = require('backbone');
    var NavItem = require('app/js/admin/models/NavItem');

    return Backbone.Collection.extend({
        model: NavItem
    });
});