define(function (require) {
    var Backbone = require('backbone');
    var RecentlyPost = require('app/js/admin/models/RecentlyPost');

    return Backbone.Collection.extend({
        model: RecentlyPost,
        url: function () {
            return 'api/v1/recentlyposts';
        }
    });
});