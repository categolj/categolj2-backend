define(function (require) {
    var Backbone = require('backbone');
    var RecentPost = require('app/js/admin/models/RecentPost');
    var Constants = require('app/js/admin/Constants');

    return Backbone.Collection.extend({
        model: RecentPost,
        url: function () {
            return Constants.API_ROOT + '/recentposts';
        }
    });
});