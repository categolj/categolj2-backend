define(function (require) {
    var Backbone = require('backbone');
    var NavItem = require('../models/NavItem');

    return Backbone.Collection.extend({
        model: NavItem
    });
});