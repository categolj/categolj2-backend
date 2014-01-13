define(function (require) {
    var Backbone = require('backbone');
    var User = require('app/js/admin/models/User');
    var Page = require('app/js/admin/collections/Page');


    // use the "good" Collection methods to emulate Array.splice
    function hackedSplice(index, howMany /* model1, ... modelN */) {
        var args = _.toArray(arguments).slice(2).concat({at: index}),
            removed = this.models.slice(index, index + howMany);
        this.remove(removed).add.apply(this, args);
        return removed;
    }

    return Backbone.Collection.extend(_.extend(Page, {
        model: User,
        url: function () {
            return '/api/users';
        },
        // Backbone.Collection doesn't support `splice`, yet! Easy to add.
        splice: hackedSplice
    }));
});