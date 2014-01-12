define(function (require) {
    var Backbone = require('backbone');
    var EntryHistory = require('app/js/admin/models/EntryHistory');

    return Backbone.Collection.extend({
        model: EntryHistory,
        url: function () {
            return '/api/entries/' + this.entry.id + '/histories';
        }
    });
});