define(function (require) {
    var Backbone = require('backbone');
    var EntryHistory = require('app/js/admin/models/EntryHistory');
    var Constants = require('app/js/admin/Constants');

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
            return Constants.API_ROOT + '/entries/' + this.entry.id + '/histories';
        }
    });
});