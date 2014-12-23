define(function (require) {
    var Backbone = require('backbone');
    var EntryHistory = require('js/models/EntryHistory');
    var Constants = require('js/Constants');

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