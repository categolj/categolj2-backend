define(function (require) {
    var Backbone = require('backbone');
    var EntryHistory = require('app/js/admin/models/EntryHistory');

    return Backbone.Collection.extend({
        model: EntryHistory,
        entry: function (entry) {
            if (entry) {
                this.entry = entry;
                return this;
            } else {
                return this.entry;
            }
        },
        url: function () {
            return '/api/entries/' + this.entry.id + '/histories';
        }
    });
});