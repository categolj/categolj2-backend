define(function (require) {
    var Backbone = require('backbone');
    var RecentPost = require('app/js/admin/models/RecentPost');

    return Backbone.Collection.extend({
        model: RecentPost,
        url: function () {
            return 'api/v1/recentposts';
        }
    });
});